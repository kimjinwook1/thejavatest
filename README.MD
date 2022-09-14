다음 세 가지만 알면 Mock을 활용한 테스트를 쉽게 작성할 수 있다.
1. Mock을 만드는 방법
2. Mock이 어떻게 동작해야 하는지 관리하는 방법
3. Mock의 행동을 검증하는 방법

Mock 객체 Stubbing
모든 Mock 객체의 행동
- Null을 리턴한다(Optional 타입은 Optional.emtpy)
- Primitive 타입은 기본 Primitive 값
- 컬렉션은 비어있는 컬렉션
- Void 메소드는 예외를 던지지 않고 아무런 일도 발생하지 않는다

Mockito BDD 스타일 API  
BDD: 애플리케이션이 어떻게 **"행동"해야 하는지**에 대한 공통된 이해를 구성하는 방법으로, TDD 에서 창안됐따.

행동에 대한 스펙
- Title
- Narrative
  - As a/ I want/ so that
- Acceptance criteria
  - Given/ When/ Then
  
## 도커와 테스트
### TestContainers 소개
테스트에서 도커 컨테이너를 실행할 수 있는 라이브러리
- https://www.testcontainers.org/
- 테스트 실행 시 DB를 설정하거나 별도의 프로그램 또는 스크립트를 실행할 필요가 없다.
- 보다 Production에 가까운 테스트를 만들 수 있다.
- 테스트가 느려진다.

@Testcontainers
- JUnit 5 확장팩으로 테스트 클래스에 @Container를 사용한 필드를 찾아서 컨테이너 라이프사이클 관련 메서드를 실행해준다.

@Container
- 인스턴스 필드에 사용하면 모든 테스트 마다 컨테이너를 재시작 하고, 스태틱 필드에 사용하면 클래스 내부 모든 테스트에서 동일한 컨테이너를 재사용한다.  

### Testcontainers 기능 살펴보기
컨테이너 만들기
- New GenericContainer(String imageName)

네트워크
- withExposedPorts(int ...)
- getMappedPort(int)

환경 변수 설정
- withEnv(key, value)

명령어 실행
- withCommand(String cmd...)

사용할 준비가 됐는지 확인하기
- waitingFor(Wait)
- Wait.forHttp(String url)
- Wait.forLogMessage(String message)

로그 살펴보기
- getLogs()
- followOutput()
