# 크래프톤 Internal Platform Backend Engineer 사전 과제

# 프로젝트 구조 및 패키지 구성
TODO: [ERD 사진]
api : 외부와 연결되는 엔드포인트
    - auth
    - dto
application : 비즈니스 로직을 처리하는 서비스 레이어 및 관련 dto, 예외
    - dto
    - exception
    - service
common : 특정 도메인에 종속되지 않은 인프라나 보안 등 구성
    - config : 환경 설정 구성
    - exception : 전역 예외를 처리
    - security : 스프링 시큐리티로 인증/인가 담당
domain : 애플리케이션 도메인.
    - Bookmark
    - Member
    - Tag
    

