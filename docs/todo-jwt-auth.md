# JWT 인증 학습 Todo

## 1. JWT 기본 구조 설계

### 구현할 내용
-[x] 토큰에 담을 클레임 결정
    > - Access Token:
    >   - sub: memberId
    >   - email
    >   - role: USER / ADMIN
    >   - iat
    >   - exp
    > 
    > - Refresh Token:
    >   - sub: memberId
    >   - iat
    >   - exp
    >   - jti
-[x] Access Token과 Refresh Token의 만료 시간 결정
  - Access Token: 실무 일반 15분~1시간, 현재 프로젝트 학습용 5분
  - Refresh Token: 실무 일반 며칠~한 달, 현재 프로젝트 학습용 24시간
-[x] JWT secret/key 설정 방식을 결정
  - 알고리즘은 HS256 사용
  - secret은 코드에 하드코딩하지 않고 application.properties 또는 환경 변수로 관리
  - 현재 프로젝트에서는 openssl rand -base64 32로 생성한 256-bit 랜덤 값을 사용
  - 실무에서는 저장소에 커밋하지 않고 환경 변수, secret manager 등으로 관리


### 확인 내용
- JWT 내용 정리 : https://llee-ss.tistory.com/43
  > - JWT는 header, payload, signature로 구성된다.
  >   - `header` : 토큰의 타입과 서명에 사용할 알고리즘 정보가 담겨 있다. (alg, typ)
  >   - `payload` : 서버가 전달하고자 하는 사용자 정보와 데이터가 담겨 있다.
  >     - `sub` claim에 어떤 값을 넣을지 결정해야 한다.
  >     - claim(key-value 형태)이 들어가지만 민감한 정보는 넣지 않는다.
  >     - 예: 사용자 ID, 권한(role), 토큰 만료 시간 등
  >   - `signature` : 토큰이 서버에 의해 정상적으로 발급되었고, 중간에 변조되지 않았음을 검증하기 위한 서명 값이다.
  >     - Header와 Payload를 인코딩한 뒤, 지정된 알고리즘과 비밀키를 이용해 생성된다.
- Access Token은 요청 인증에 사용하고, Refresh Token은 Access Token 재발급에 사용한다.
- secret/key는 코드에 직접 하드코딩하지 않고 설정값으로 관리하는 것이 좋다.


## 2. JWT 라이브러리 및 설정 추가
### 구현할 내용
-[x] `build.gradle`에 JWT 라이브러리 추가
  - 참고 : https://github.com/jwtk/jjwt#gradle
-[x] `application.properties` : JWT secret, Access Token 만료 시간, Refresh Token 만료 시간을 추가
  - JWT secret key는 application.properites에 직접 넣거나
  - 지금처럼 Java 코드에 적는 게 아니라, 애플리케이션을 실행하는 “환경”에 등록한다.
    > - 1. openssl rand -base64 32로 secret을 한 번 생성한다.
    > - 2. IntelliJ Run -> Edit Configurations -> Environment variables에 JWT_SECRET=생성값 을 등록한다.
    > - 3. application.properties에는 jwt.secret=${JWT_SECRET}만 적는다.
-[x] JWT 설정값을 주입받을 수 있는 구조 생성
  - JwtTokenProvider

### 확인 내용
- JWT 라이브러리로 토큰 생성, 서명, 검증을 처리한다.
- secret은 충분히 긴 값을 사용해야 한다.
- 운영 환경에서는 secret을 환경 변수나 외부 설정으로 관리하는 것이 좋다.


## 3. 토큰 응답 DTO 설계
### 구현할 내용
-[x] 로그인 성공 시 반환할 토큰 응답 DTO를 만든다.
  - TokenResponse
  - 응답에 Access Token, Refresh Token, token type, 만료 시간 정보를 포함

### 확인 내용
- 토큰을 응답 body로 내려줄지, header나 cookie로 내려줄지 선택할 수 있다.
  > - `cookie`
  >   - XSS보안을 고려해 클라이언트가 쿠키에 저장할 수 있도록 토큰을 쿠키에 넣어 클라이언트에 보낼 수는 있지만(set-cookie),
  >    클라이언트가 토큰을 쿠키에 저장한다고 서버에서도 쿠키에 저장할 필요는 없다.
  >   - 또한, Storage에 Cookie 저장이 되지 않는 이슈가 있다.
  >   - 대부분은 `body`에 담아서 응답한다.
  > 
  > - 참고 : https://velog.io/@nathan29849/JWT%EB%A5%BC-Header%EC%97%90-Body%EC%97%90


## 4. JWT 발급 로직 구현
### 구현할 내용
-[x] `JwtTokenProvider` 생성
-[x] 기존 로그인 검증 로직을 재사용해 로그인 성공 시 토큰을 발급한다.
  - TokenResponse 사용
### 확인 내용
- 토큰 발급은 로그인 검증과 분리하면 역할이 명확해진다.
- Access Token에는 인증에 필요한 최소 정보만 담는다.
- Refresh Token에는 식별 가능한 최소 claim과 만료 시간 정도만 담는 것이 좋다.


## 5. Refresh Token 엔티티 및 저장 구조 구현
### 구현할 내용
-[x] Refresh Token 엔티티, Repository 생성
-[x] Member와 Refresh Token의 관계 결정
-[x] Refresh Token 문자열과 만료 시간을 DB에 저장
  - 회원 정보(member), 토큰 값, 만료 시간 저장
-[x] 재로그인 시 기존 Refresh Token 처리 정책 구현
  - 현재 프로젝트에서는 재로그인 시 기존 Refresh Token을 새 값으로 교체하는 방향으로 구현
  - 회원 1명당 Refresh Token 1개 정책으로 정함
### 확인 내용
- Refresh Token을 DB에 저장하면 서버에서 재발급 가능 여부를 제어할 수 있다.
- 재로그인 시 기존 Refresh Token을 삭제하거나 새 값으로 교체할 수 있다.
- Refresh Token 만료 시간은 JWT 자체의 만료 시간과 DB 저장 만료 시간이 일치해야 한다.


## 6. JWT 인증 필터 구현
### 구현할 내용
-[x] JWT 인증 필터 JwtAuthenticationFilter 생성
  - Authorization 헤더에서 Bearer Token을 추출
  - Access Token 검증
  - 검증 성공 시 현재 사용자 정보를 `SecurityContextHolder`에 설정
-[x] Security Filter Chain에 JWT 인증 필터를 등록한다.

### 확인 내용
- JWT 인증 필터의 역할
  > HTTP 요청의 Authorization 헤더에 들어온 JWT를 검증하고,
  > Spring Security가 이해할 수 있는 인증 객체로 바꿔서 등록한다.
- JWT 인증 필터 흐름
  > Authorization 헤더에서 JWT 추출
  -> JWT 검증
  -> sub에서 memberId 추출
  -> UserDetailsService로 회원 조회
  -> UserDetails 생성
  -> UsernamePasswordAuthenticationToken 생성
  -> 요청 부가 정보 설정
  -> SecurityContextHolder에 인증 등록
  -> 다음 필터로 진행
- JWT 인증은 요청마다 Access Token을 검증하는 방식이다.
- `Authorization: Bearer {token}` 형식을 사용한다.
- Spring Security에서 인증된 사용자 정보는 `SecurityContextHolder`에 저장된다.
- 필터에서 발생한 예외는 `GlobalExceptionHandler`로 바로 처리되지 않을 수 있다.


## 7. 현재 로그인 사용자 조회 API 구현
### 구현할 내용
-[x] 내 정보 조회하는 API 구현
  - MemberController.getMyInfo();
  - Authentication로 구현
### 확인 내용
- 컨트롤러에서 인증된 사용자 정보를 받는 방법
  > Authentication 사용, authentication.getName()으로 memberId 추출
- 토큰의 subject만 믿을지, DB에서 회원 상태를 다시 확인할지
  > MemberService에서 findByIdAndStatus(id, ACTIVE)로 재확인
- 탈퇴 또는 비활성화 회원 처리
  > ACTIVE가 아니면 MemberNotFoundException 흐름으로 처리


## 8. Access Token 재발급 API 구현
### 구현할 내용
-[x] Access Token 재발급 요청 DTO를 만든다.
  - Access Token 재발급 요청 시, requestBody, header, cookie로 구현 가능
  - 실무에서는 Refresh Token을 HttpOnly Cookie에 담는 방식을 많이 사용하지만
  - 현재 프로젝트의 TokenResponse body 방식은 쿠키가 아니기 때문에 요청 DTO로 구현
-[x] 요청으로 받은 Refresh Token을 검증한다.
-[x] DB에 저장된 Refresh Token과 비교한다.
-[x] Refresh Token이 유효하면 새 Access Token을 발급한다.
### 확인 내용
- Refresh Token은 JWT 검증만으로 끝내지 않고 DB 저장 값과 비교한다.
- DB에 없거나 값이 다르면 재발급을 거부한다.
- Refresh Token이 만료되면 다시 로그인해야 한다.
- Access Token만 새로 발급할지, Refresh Token도 함께 회전할지 정책을 정할 수 있다.
  > 현재 프로젝트는 재발급 시 Access Token과 Refresh Token을 모두 새로 발급한다. (RefreshToken rotation)
  기존 Refresh Token은 DB에서 새 값으로 교체되어 재사용할 수 없다.
  > 따라서 이전 Refresh Token을 다시 사용하면 REFRESH_TOKEN_MISMATCH가 발생한다.


## 9. 로그아웃 API 구현
### 구현할 내용
-[x] 로그아웃 API 구현
  - 요청으로 받은 Refresh Token을 검증
  - DB에 저장된 Refresh Token과 비교 후 로그아웃 시 DB에 저장된 Refresh Token row를 삭제한다.
-[x] 로그아웃 후 같은 Refresh Token으로 access Token 재발급 X
  - 현재 프로젝트에서는 학습을 위해 먼저 Refresh Token 삭제 방식으로 구현한다.
  - 삭제 방식에서는 로그아웃 후 DB에 Refresh Token 정보가 남지 않는다.
  - 추후 고도화 단계에서 `revoked`, `revokedAt` 필드를 추가해 삭제 대신 무효화 방식으로 변경한다.

### 확인 내용
- JWT Access Token은 서버가 상태를 저장하지 않기 때문에 즉시 폐기하기 어렵다.
- Refresh Token은 DB에서 삭제하거나 무효화하면 추가 재발급을 막을 수 있다.
  > `삭제 방식`
  > - 단순하고 Refresh Token 제어 흐름을 이해하기 쉽다.
  > - 삭제 방식은 로그아웃 이력이 남지 않는다는 한계가 있다.
  > 
  > `무효화 방식`
  > - `revoked`, `revokedAt` 같은 상태를 남겨 로그아웃 이력과 토큰 상태를 추적할 수 있다.
  > - 무효화 방식은 삭제 방식 구현 후 고도화 학습 항목으로 남긴다.
- Access Token까지 즉시 차단하려면 Redis 또는 DB 기반 블랙리스트를 사용할 수 있지만, 현재 프로젝트에서는 고도화 단계로 남긴다.
- Redis 블랙리스트 방식은 로그아웃된 Access Token을 남은 만료 시간만큼 저장하고, 요청마다 블랙리스트 여부를 확인하는 방식이다.


## 10. 예외 처리 연결
### 구현할 내용
-[x] JWT 관련 `ErrorCode`를 추가한다.
-[x] 토큰 없음, 토큰 만료, 토큰 위조/서명 불일치 예외를 구분한다.
-[x] Refresh Token 없음, Refresh Token 만료, Refresh Token 불일치 예외를 구분한다.
-[x] JWT 필터에서 발생한 인증 실패를 기존 `ErrorResponse` 형식과 연결한다.
### 확인 내용
- 인증 실패는 일반 비즈니스 예외와 처리 위치가 다를 수 있다.
- Spring Security의 `AuthenticationEntryPoint`를 사용할 수 있다.
- 공통 응답 구조를 유지하면 클라이언트가 실패 응답을 일관되게 처리할 수 있다.


## 11. 테스트 및 Swagger/HTTP 클라이언트 확인
### 확인 내용
- 로그인 후 Access Token과 Refresh Token 발급을 확인한다.
- Access Token으로 현재 로그인 사용자 조회를 확인한다.
- Access Token 만료 후 Refresh Token으로 재발급을 확인한다.
- 재발급된 Access Token으로 다시 내 정보 조회를 확인한다.
- 로그아웃 후 Refresh Token 재사용 실패를 확인한다.
