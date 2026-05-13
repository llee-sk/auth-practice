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

### 확인 내용


## 4. JWT 발급 로직 구현
### 구현할 내용
### 확인 내용


## 5. Refresh Token 엔티티 및 저장 구조 구현
### 구현할 내용
### 확인 내용


## 6. JWT 인증 필터 구현
### 구현할 내용
### 확인 내용


## 7. 현재 로그인 사용자 조회 API 구현
### 구현할 내용
### 확인 내용

## 8. Access Token 재발급 API 구현
### 구현할 내용
### 확인 내용


## 9. 로그아웃 API 구현
### 구현할 내용
### 확인 내용


## 10. 예외 처리 연결
### 구현할 내용
### 확인 내용

## 11. 테스트 및 Swagger/HTTP 클라이언트 확인
### 구현할 내용
### 확인 내용

