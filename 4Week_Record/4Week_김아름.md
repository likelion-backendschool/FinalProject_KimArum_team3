# [4Week] 김아름

## 미션 요구사항 분석 & 체크리스트

### 1. 요구사항 분석

- REST API 구현

    - JWT 로그인 구현(POST /api/v1/member/login)
    - 내 도서 리스트 구현(GET /api/v1/myBooks)
    - 내 도서 상세정보 구현(GET /api/v1/myBooks/{id})
    - 로그인 한 회원의 정보 구현(GET /api/v1/member/me)
    - Srping Doc 으로 API 문서화(크롬 /swagger-ui/index.html )

### 2. 개발 체크리스트
#### 필수과제
- [x] JWT 로그인 구현(POST /api/v1/member/login)
- [ ] 내 도서 리스트 구현(GET /api/v1/myBooks) (△)
- [ ] 내 도서 상세정보 구현(GET /api/v1/myBooks/{id}) (△)
- [x] 로그인 한 회원의 정보 구현(GET /api/v1/member/me)
- [x] Srping Doc 으로 API 문서화(크롬 /swagger-ui/index.html )

#### 추가과제
- [ ] 엑세스 토큰 화이트리스트 구현(Member 엔티티에 accessToken 필드 추가)
- [ ] 리액트에서 작동되도록

---

## 4주차 미션 요약

### [접근 방법]

- DB 공유를 위해 기존 프로젝트에 RestController를 추가하여 구현
- RsData를 활용해 resultCode, msg, data를 담은 json 형태로 응답 리턴


###### 1. MemberRestController
기존에 MemberController가 존재하고 있어서 JWT 로그인과
사용자의 상세 정보 제공 기능을 담당할 RestController를 따로 추가했다.

- POST /api/v1/member/login  
  로그인 페이지

  ```java
    @PostMapping("/login")
    public ResponseEntity<RsData> login(@RequestBody LoginForm loginForm) {
        if (loginForm.isNotValid()) {
            return Ut.spring.responseEntityOf(RsData.of("F-1", "로그인 정보가 올바르지 않습니다."));
        }

        Member member = memberService.findByUsername(loginForm.getUsername()).orElse(null);

        if (member == null) {
            return Ut.spring.responseEntityOf(RsData.of("F-2", "일치하는 회원이 존재하지 않습니다."));
        }

        if (passwordEncoder.matches(loginForm.getPassword(), member.getPassword()) == false) {
            return Ut.spring.responseEntityOf(RsData.of("F-3", "비밀번호가 일치하지 않습니다."));
        }

        log.debug("Util.json.toStr(member.getAccessTokenClaims()) : " + Ut.json.toStr(member.getAccessTokenClaims()));

        String accessToken = memberService.genAccessToken(member);

        return Ut.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "로그인 성공, Access Token을 발급합니다.",
                        Ut.mapOf(
                                "accessToken", accessToken
                        )
                ),
                Ut.spring.httpHeadersOf("Authentication", accessToken)
        );
    }
  ```
  - username과 password를 입력 받을 수 있도록 LoginForm을 생성
  - username/password가 비어있을 경우, 입력된 데이터로 db에 저장된 member가 조회되지 않을 경우,
  비밀번호가 일치하지 않을 각각의 경우에 오류코드, 오류내용을 응답 본문에 담아 return
  - 로그인에 성공할 경우 Jwt AccessToken 생성
  - AccessToken을 응답 본문에 담아  return  

</br>

- GET /api/v1/member/me  
  사용자 상세 정보 페이지

  ```java
    @GetMapping("/me")
    public ResponseEntity<RsData> me(@AuthenticationPrincipal MemberContext memberContext){
        if(memberContext == null){
            return Ut.spring.responseEntityOf(RsData.failOf(null));
        }

        return Ut.spring.responseEntityOf(RsData.successOf(memberContext));
    }
  ```
  - MemberContext를 이용해 현재 로그인되어 있는 사용자정보 확인, return

</br>

###### 2. MyBookController

- GET /api/v1/myBooks  
  사용자가 구매한 도서들의 리스트 페이지

  ```java
    @GetMapping("")
    public ResponseEntity<RsData> list(@AuthenticationPrincipal MemberContext memberContext){
        Member member = memberContext.getMember();
        List<MyBook> myBooks = myBookService.findAllByOwnerId(member.getId());

        return Ut.spring.responseEntityOf(
                RsData.successOf(
                        Ut.mapOf(
                                "myBooks", myBooks
                        )
                )
        );
    }
  ```
  - MemberContext를 이용해 현재 로그인되어 있는 사용자정보 확인
  - 로그인된 사용자의 id를 이용해 myBook 조회
  - 조회된 myBook 리스트를 응답 본문에 담아 return

  > **500 에러 발생**  
  > No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor and no properties discovered to create BeanSerializer

  - **원인**  
  `@ManyToOne` 컬럼의 `fetch=LAZY`로 인한 JSON 오류  
  DB에서 엔티티 정보를 가져올 때 매핑되어 있는 다른 엔티티의 정보를 어느 시점에
  가져올지 정하는 옵션    
  
  - **수정 사항 및 궁금증**  
    오류가 발생하는 부분 (member, product, postkeyword 등)에 `@JsonIgnore` 어노테이션 추가
    - JSON 오류는 발생하지 않지만 Ignore 처리한 엔티티에 대한 정보를 불러오지 못함
    - 불러오지 못하는 정보들 또한 필요한 데이터여서 문제가 해결되지 않음

</br>

- Get /api/v1/myBooks/{id}  
  구매 도서 상세 페이지
  ```java
    @GetMapping("/{myBookId}")
    public ResponseEntity<RsData> detail(@PathVariable long myBookId){
        MyBook myBook = myBookService.findById(myBookId).orElse(null);

        if(myBook == null){
            return Ut.spring.responseEntityOf(RsData.of("F-1", "도서 정보가 올바르지 않습니다."));
        }

        return Ut.spring.responseEntityOf(
                RsData.successOf(
                        Ut.mapOf(
                                "myBook", myBook
                        )
                )
        );
    }
  ```
  - myBookId를 이용해 DB 조회
  
  > **500 에러 발생**  
  > No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor and no properties discovered to create BeanSerializer

  - **원인**  
    `@ManyToOne` 컬럼의 `fetch=LAZY`로 인한 JSON 오류  
    DB에서 엔티티 정보를 가져올 때 매핑되어 있는 다른 엔티티의 정보를 어느 시점에
    가져올지 정하는 옵션  
    PostKeyword 엔티티에서 오류 발생

  - **수정 사항 및 궁금증**  
    오류가 발생하는 부분에 `@JsonIgnore` 어노테이션 추가
    - PostKeyword를 Ignore처리하면 구매한 도서에 포함되는 Post에 대한 정보 또한 조회할 수 없는 문제


### [이후개발진행]

### [특이사항]

