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
- [ ] 정산 데이터 생성
- [ ] 건별 정산 처리
- [ ] 전체 정산 처리
- [ ] 정산 데이터 페이지 생성 
<br/>

---

## 2주차 미션 요약

### [접근 방법]

2주차의 해설 코드를 기반으로 3주차 미션 시작

#### - 관리자 회원 지정  
user2 회원을 관리자로 지정

- _InitData_  
member join 이후 user2에 대해 AuthLevel 변경
```java
member2.setAuthLevel(AuthLevel.ADMIN);
memberRepository.save(member2);
```

- _CustomUserDetailService.java, Member.genAuthorities()_  
AuthLevel이 AuthLevel.ADMIN인 경우 ADMIN authority 추가
```java
if (authLevel==AuthLevel.ADMIN) {
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
}
```  
> authorites.add를 둘 중 어느 부분에 해줘야 맞는지 이해가 부족한 것 같음  
> 


<br/>

### [특이사항]
- enum 활용 및 AuthLevel 변경 시 authorities가 변화되는 로직에 대한 이해가 부족한 것 같음