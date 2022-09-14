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
