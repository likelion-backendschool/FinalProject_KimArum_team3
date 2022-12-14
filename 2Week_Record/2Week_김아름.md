# [2Week] 김아름

## 미션 요구사항 분석 & 체크리스트

### 1. 요구사항 분석
#### - 장바구니
- 상품 = 도서
- 장바구니에 도서를 추가/삭제 가능
- 장바구니를 통해 주문을 만들 수 있다

#### - 주문/결제
- 주문을 하면 초기상태는 준비상태  
  
  - 결제 또는 주문 취소 가능
- 결제 시 캐시 사용 또는 페이먼츠 이용 가능
- 결제 완료 시 MyBook 페이지에서 구매한 도서 열람 가능

### 2. 개발 체크리스트
#### 1. 장바구니
- [x] 장바구니에 상품 추가
- [x] 장바구니에서 상품 삭제
- [x] 장바구니 상품 확인

#### 2. 예치금
- [x] 예치금 관련 기록 테이블 CashLog

#### 3. 주문 및 결제
- [x] 예치금을 통한 결제 가능
- [x] 토스 페이먼츠 연동
- [x] 토스 페이먼츠 이용한 카드결제
- [x] 결제 후 MyBook에서 구매한 도서 확인 가능  
<br/>  

- [x] 주문 리스트
- [ ] 주문 취소 (결제하지 않은 주문)
- [ ] 환불 (결제 완료한 주문)
- [x] 장바구니에서 주문 생성

---

## 2주차 미션 요약

### [접근 방법]
- 장바구니 -> 예치금 -> 주문 -> 결제 -> MyBook

#### 1. 장바구니

- 구매자, 상품 정보를 담은 CartItem 테이블
- addItem()  
  상품을 장바구니에 추가해주는 메서드
  ![img.png](img/img.png)

  상품 상세페이지에서 **장바구니 담기** 버튼을 통해 product.id POST 전송  
</br>

- removeItem()  
  장바구니에서 상품을 삭제해주는 메서드
  ![img_1.png](img/img_1.png)   
</br>  

- hasItem()  
  장바구니에 특정 상품이 존재하는지 확인하는 메서드
  
#### 주문/결제
- 토스 페이먼츠 연동 관련 부분은 수업에서 진행했던 부분 참고  
- showDetail()  
  order의 id를 통해 order에 대한 정보를 찾아 상세 내용 표시  
  예치금 결제를 위해 사용자의 restCash 값 넘겨줌  
  ![img_2.png](img/img_2.png)  
  
  로그인 사용자와 주문자가 일치하는지 검증  
  ![img_3.png](img/img_3.png)
 </br>
 
- payByRestCashOnly()  
  예치금만 사용하여 결제 진행할 경우
 
- createOrder()  
  장바구니에 담긴 상품들로 주문을 생성
  ![img_4.png](img/img_4.png)
 </br>  

- showOrderList  
  생성한 주문들의 리스트 메서드  
  ![img_5.png](img/img_5.png)
   
 </br>

#### MyBook  
Member, Product를 FK로 하여 회원이 구매한 도서 정보 저장  
- addMyBook()
  orderId를 통해 주문에 포함된 productList 생성    
  구매자와 product를 이용해 MyBook 빌드, 저장  
  ![img.png](img/img_6.png)
 </br>

- refundedMyBook()  
  주문 환불 시 MyBook에서 정보 삭제  
   ![img.png](img/img_7.png)  
  </br>





    

### [특이사항]
- createOrder() 단계에서 이미 구매한 상품을 제외하고 주문을 생성할 수 있도록 변경해야 함    
- MyBook에 저장한 내용을 이용해 구매도서 페이지 구현 필요
