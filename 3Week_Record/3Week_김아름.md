# [3Week] 김아름

## 미션 요구사항 분석 & 체크리스트

### 1. 요구사항 분석
#### - 관리자  

- authLevel을 7로 하여 일반 회원을 `ADMIN` 회원으로 지정
- 정산  
  
  - 건별 정산 처리가 가능
  - 전체 정산 처리 가능
  - 정산비율은 5:5
  - pg수수료 0원

### 2. 개발 체크리스트
#### - 관리자
- [x] 관리자 회원 지정 -> user2를 관리자로 지정
- [x] 관리자 홈페이지 생성
- [x] 정산 데이터 생성
- [x] 건별 정산 처리
- [x] 전체 정산 처리
- [x] 정산 데이터 페이지 생성 
<br/>

#### -추가과제
- [ ] 정산데이터를 배치로 생성
- [ ] 출금신청기능(사용자기능)
- [ ] 출금처리기능(관리자기능)

---

## 3주차 미션 요약

### [접근 방법]

2주차의 해설 코드를 기반으로 3주차 미션 시작 - enum 활용  
음원결제 프로젝트 참고하여 구현

#### - 관리자 회원 지정  
user2 회원을 관리자로 지정

- _InitData.java_  
member join 이후 user2에 대해 AuthLevel 변경
  ```java
  member2.setAuthLevel(AuthLevel.ADMIN);
  memberRepository.save(member2);
  ```

- _Member.genAuthorities()_  
AuthLevel이 AuthLevel.ADMIN인 경우 ADMIN authority 추가
  ```java
  if (authLevel==AuthLevel.ADMIN) {
          authorities.add(new SimpleGrantedAuthority("ADMIN"));
  }
  ```  

<br/>

#### - 정산 데이터 생성
- _RebateService.java_  
  해당하는 월의 orderItem 정보를 얻어서 정산용 RebateOrderItem 데이터로 변환  
  변환된 데이터를 DB에 저장, 이용하여 정산 데이터 페이지 구현
  ```java
  public void makeDate(String yearMonth){
          ...생략...
          //orderItem 데이터 가져오기
          List<OrderItem> orderItems = orderService.findAllByPayDateBetween(fromDate, toDate);
  
          //데이터 변환
          List<RebateOrderItem> rebateOrderItems = orderItems
          .stream()
          .map(this::toRebateOrderItem)
          .collect(Collectors.toList());
  
          //저장
          rebateOrderItems.forEach(this::makeRebateOrderItem);
  }
  ```  
- _rebateOrderItemList_  
저장된 RebateOrderItem 데이터를 조회하여 보여줌
  ```java
  @GetMapping("/rebateOrderItemList")
  @PreAuthorize("hasAuthority('ADMIN')")
  public String showRebateOrderItemList(String yearMonth, Model model) {
          if (yearMonth == null) {
              yearMonth = "2022-10";
          }
  
          List<RebateOrderItem> items = rebateService.findRebateOrderItemsByPayDateIn(yearMonth);
  
          model.addAttribute("items", items);
  
          return "adm/rebate/rebateOrderItemList";
  }
  ```  

<br/>

#### - 정산
###### 0. 수수료 및 정산 비율
정산비율 5:5, pg수수료 = 0 적용  
![img.png](img/img.png)

![img_1.png](img/img_1.png)
###### 1. 건별 정산  

- _RebateService.java_  
정산 가능 상태인지 확인  
정산하는 주문의 금액을 상품 판매자에게 예치금으로 지급 후 CashLog에 저장  
  ```java
  if (rebateOrderItem.isRebateAvailable() == false) {
              return RsData.of("F-1", "정산을 할 수 없는 상태입니다.");
  }
  
  int calculateRebatePrice = rebateOrderItem.calculateRebatePrice();

        CashLog cashLog = memberService.addCash(
                rebateOrderItem.getProduct().getAuthor(),
                calculateRebatePrice,
                "정산__%d__지급__예치금".formatted(rebateOrderItem.getOrderItem().getId())
        ).getData().getCashLog();

        rebateOrderItem.setRebateDone(cashLog.getId());

        return RsData.of(
                "S-1",
                "주문품목번호 %d번에 대해서 판매자에게 %s원 정산을 완료하였습니다.".formatted(rebateOrderItem.getOrderItem().getId(), calculateRebatePrice),
                Ut.mapOf(
                        "cashLogId", cashLog.getId()
                )
        );
  ```

###### 2. 선택 정산 (전체 정산)
정산아이템 리스트 페이지에서 체크박스 구현하여 처리하고자 하는 주문의 id값을 얻어옴  

- _AdmRebateController.java_  
하나의 문자열로 전달된 id값들을 `,`를 기준으로 나누어 배열 형태로 저장  
배열에 포함된 id들을 rebate() 처리
  ```java
  public String rebate(String ids, HttpServletRequest req) {
  
          String[] idsArr = ids.split(",");
  
          Arrays.stream(idsArr)
                  .mapToLong(Long::parseLong)
                  .forEach(id -> {
                      rebateService.rebate(id);
                  });
  
          String referer = req.getHeader("Referer");
          String yearMonth = Ut.url.getQueryParamValue(referer, "yearMonth", "");
  
          return Rq.redirectWithMsg("/adm/rebate/rebateOrderItemList?yearMonth=%s".formatted(yearMonth), "%d건의 정산품목을 정산처리하였습니다.".formatted(idsArr.length));
      }
  ```
  
<br/>

#### - 출금
###### 1. 출금 신청
```java
public RsData<Withdraw> apply(long memberId, String bankName, String bankAccountNo, long price) {
        Member member = memberService.findById(memberId).orElse(null);

        if(member==null){
            return RsData.of("F-1", "존재하지 않는 회원입니다.");
        }

        if(memberService.getRestCash(member)<price){
            return RsData.of("F-2", "출금 금액 초과입니다.");
        }

        CashLog cashLog = memberService.addCash(
                member,
                price * -1,
                "출금신청__예치금"
        ).getData().getCashLog();

        Withdraw withdraw = Withdraw.builder()
                .member(member)
                .bankName(bankName)
                .bankAccountNo(bankAccountNo)
                .price(price)
                .withdrawCashLog(cashLog)
                .build();

        withdrawRepository.save(withdraw);

        return RsData.of("S-1", "출금 신청이 완료되었습니다.", withdraw);
    }
```  

###### 2. 출금 처리
```java
public RsData withdraw(Long withdrawApplyId) {
        Withdraw withdraw = withdrawRepository.findById(withdrawApplyId).orElse(null);

        if (withdraw == null) {
            return RsData.of("F-1", "출금신청 데이터를 찾을 수 없습니다.");
        }

        if (withdraw.withdrawAvailable() == false) {
            return RsData.of("F-2", "이미 처리되었습니다.");
        }

        CashLog cashLog = memberService.addCash(
                withdraw.getMember(),
                0,
                "출금__%d__지급__현금".formatted(withdraw.getId())
        ).getData().getCashLog();

        withdraw.setWithdrawDone(cashLog.getId());

        return RsData.of(
                "S-1",
                "출금신청(%d번) 처리완료. %s원이 출금되었습니다.".formatted(withdraw.getId(), Ut.nf(withdraw.getPrice())),
                Ut.mapOf(
                        "cashLogId", cashLog.getId()
                )
        );
    }
```  

</br>

### [이후개발진행]
1. 전체 출금 처리
2. 출금 취소(거절)처리
3. 출금 신청 / 처리 시 CashLog를 두번 만들지 않고 하나로 처리할 수 있는 방법 고민  

### [특이사항]
- enum 활용 및 AuthLevel 변경 시 authorities가 변화되는 로직에 대한 이해가 부족한 것 같음
