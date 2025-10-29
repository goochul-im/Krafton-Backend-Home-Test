# 크래프톤 Internal Platform Backend Engineer 사전 과제
이 github 레포에는 CI가 적용되어 있습니다.
PR을 생성하거나, main에 push가 완료되면 github action을 통해 자동 테스트를 진행합니다.

# ERD

<img width="841" height="373" alt="image" src="https://github.com/user-attachments/assets/e4dd82cd-2c93-4332-b114-093457e2ab94" />

# 구현된 요구사항
1. 북마크 관리 REST API
2. 테스트 코드 (단위 테스트)
3. API 명세 (swagger)
4. 태그 기능
5. 검색 기능
6. 페이지네이션
7. 전역 예외 처리
8. CI (github action)
9. 인증 (Spring Security)
   - 회원가입
   - 로그인
   - 로그아웃
   - 본인 계정의 북마크/태그만 조회/수정/삭제 가능

# 빌드/실행/테스트 방법
현재 프로젝트는 h2 인메모리 데이터베이스를 사용 중이므로, 별다른 DB 연결 없이 사용 가능합니다. <br>
bookmark/src/main/java/krafton/bookmark/common/DataInitializer 에서 더미 데이터를 추가하였으므로 바로 사용이 가능합니다. <br>
## Windows
- 빌드: .\gradlew clean build
- 실행: .\gradlew bootRun
- 테스트: .\gradlew test
## MacOS
- Permission denied: chmod +x ./gradlew
- 빌드: ./gradlew clean build
- 실행: ./gradlew bootRun
- 테스트: ./gradlew test
## 테스트 로그인
- /auth/login
  - username: user
  - password: 1111

# 프로젝트 구조 및 패키지 구성
```
📁 [프로젝트 루트]
│
├── 📂 api: 외부와 연결되는 엔드포인트
│   └── 📂 dto : 외부 응답이나 요청을 받는 DTO
│
├── 📂 application: 비즈니스 로직을 처리하는 서비스 레이어 및 관련 dto, 예외
│   ├── 📂 dto
│   ├── 📂 exception
│   └── 📂 service
│
├── 📂 common: 특정 도메인에 종속되지 않은 인프라나 보안 등 구성
│   ├── 📂 config: 환경 설정 구성
│   ├── 📂 exception: 전역 예외를 처리
│   └── 📂 security: 스프링 시큐리티로 인증/인가 담당
│
├── 📂 domain: 애플리케이션 도메인
│    ├── 🧩 Bookmark
│    ├── 🧩 Member
│    └── 🧩 Tag
│
└── 📂 infrastructure: DB와 통신하는 클래스 담당

```
# DB 확인
내부 데이터를 확인하고 싶으면 다음 링크에 접속하세요.<br>
http://localhost:8080/h2-console <br>
JDBC URL : jdbc:h2:mem:testdb <br>
username : sa <br>
password : <없음> <br>

# API 명세서
애플리케이션을 시작한 후 다음 링크에 접속하세요.<br>
http://localhost:8080/swagger-ui.html

# 설계 방식
1. 스프링 시큐리티 사용
   - 비즈니스 로직이나 컨트롤러에서 인증/인가를 구현할 필요가 없어 관심사의 분리 가능
   - 보안 관련 로직이 한군데에 모여 있어 확장, 변경이 수월함 (예를 들면 세션 -> JWT의 변환, api 엔드포인트 추가 시 securityConfig만 설정하면 됨)
   - 보안 관련 통합 예외 처리 가능 (AuthenticationEntryPoint, AccessDeniedHandler 등)
2. 예외 처리 통합
   - @ControllerAdvice를 사용하여 비즈니스 로직에서 발생하는 예외를 통합 처리함
   - ErrorResponse, ErrorCode 등을 사용해 클라이언트와 약속된 에러 코드를 전달할수 있음.
3. 구현 클래스가 아닌 인터페이스 의존
   - 인터페이스에만 의존하여 결합도를 낮추려고 함
4. 패키지 구조
   - 서로 관련있는 클래스들로만 구성하여 응집도를 높임
  

# 개선할 점
1. 현재 BookmarkRepository의 북마크 검색을 담당하는 findBookmarkPagesByQuery는 검색 문자열을 포함하는 모든 레코드를 찾기 위해 와일드카드를 사용 중.
   - 이는 데이터셋이 많아질 경우 심각한 성능 저하를 초래할 수 있음.
2. 스프링 시큐리티에서 사용하는 CustomUserDetails는 getAuthorities 사용 시 항상 ROLE_USER만 반환.
   - 현재 애플리케이션은 로그인/비로그인만 구분할 수 있음.
   - 추후 확장을 고려하면 Member 엔티티에 ROLE을 추가한 다음 getAuthorities에서 Member의 ROLE을 반환하도록 하여 권한에 따른 인가 가능.
3. 커밋이 바로바로 이루어지지 않고, 작업이 쌓이면 커밋을 하는 일이 많았었기 때문에 각 커밋으로 체크아웃하면 애플리케이션이 제대로 작동하지 않을 수 있음.
4. application.yml이 현재 그대로 외부에 노출되어 있음
   - 민감한 정보가 들어있다면 유출되어 위험할 수 있으므로, github secret과 github action을 사용하여 개선할 수 있음


