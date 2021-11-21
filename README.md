# GRAPHQL-KOTLIN

## 평가항목

- 리졸버, 필드등을 손쉽게 정의할 수 있는가?
- 의존주입이 손쉬운가?
- API 문서를 손쉽게 내보낼 수 있는가
- 테스트 용이성
- Spring Security 적용성
- 트랜잭션 적용이 용이한가
- 데이터 로더 적용이 쉬운가
- 레퍼런스가 많은가
- 필드리졸버는 적용할 수 잇는가?
- ConnectionField는?

## 내용

- Spring MVC로 하면 동작하지 않는다. 그렇다고 suspend function을 강제하진 않음
- 기본제공 api
  - /graphql -> graphql endpoint
  - /playground -> 플레이그라운드
  - /sdl -> 스키마 파일
- Query, Mutation resolver를 컨트롤러처럼 사용가능
- field 리졸버
  - 가능함
  - lazy 하게 동작시킬 수 있음
  - 다만 Entity에서까지 lazy하게 동작하는 것은 쉽지 않아보임
    - Eager로딩을 사용하면 조회할 수 있으나 결국 lazy하게 동작하지 않기 때문에 똑같음
- Transactional 잘동작함
- 데이터로더
  - 개별 데이터 로더 가능
- 배치로더
  - 데이터로더로 해결 가능
- 테스트코드
  - WebClient를 통해서 함
  - 불편
- ConnectionField
  - 별도로 제공안함
- 시큐리티
  - 별도로 제공안함
