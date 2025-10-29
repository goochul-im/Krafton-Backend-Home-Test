# 사용 AI 도구
1. gemini
2. gemini-cli

# 주요 활용 방식
1. 에러 메세지 분석
   - 주로 테스트 시 Assertion에 관련된 에러가 생소하여 사용했습니다. 
2. jpql 분석
3. DataJpa 테스트 구현 (test/java/krafton/bookmark/domain/bookmark/BookmarkRepositoryTest.java)
   - 아직 @DataJpaTest를 사용한 테스트 경험이 적어 테스트를 구현하고, 설명해달라고 gemini-cli에게 요청했습니다.
   - @Query로 jpql을 작성한 메소드는 잘못 작성된 부분은 없는지 평가해달라고 요청했습니다.
   - 프롬프트
     - 예) @BookmarkRepository.java 의 findBookmarkPagesByQuery 메소드를
       평가해주고, 이 메소드에 대한 테스트를 만들어줘.
   - 필요한 엔티티의 생성 방식이 잘못되어 수정
   - 추후 테스트를 확장할 수 있도록 셋업 데이터에 들어가는 필드들을 상수로 수정
4. 더미 데이터셋 생성
   - common/DataInitializer
   - 최소한 두명의 유저를 만들어 사용할수 있도록 요청했습니다.
5. API 컨트롤러와 DTO에 swagger 어노테이션 생성
6. @ControllerAdvice의 핸들링 메소드 작성
   - 공통적으로 발생하는 예외를 핸들링할수 있도록 작성해달라고 요청했습니다.
7. Validation 관련
   - 컨트롤러에서 사용하는 DTO에서 @Pattern에 대한 정규식을 작성해달라고 요청했습니다.
8. ci.yml 작성
   - 코드를 병합할 때 자동으로 수행하는 github action 파일 생성
