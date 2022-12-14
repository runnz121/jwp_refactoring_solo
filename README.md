# 키친포스

## 요구 사항

### Product(상품)
- 상품은 **상품명**과 **가격**으로 구성된다.
- 상품을 생성 할 수 있다
  - 상품의 가격은 0 이상 이어야 한다.
- 상품 목록을 조회할 수 있다

### MenuGroup(메뉴 그룹)
- 메뉴 그룹은 **메뉴그룹명**으로 구성된다.
- 메뉴 그룹 생성 할 수 있다
- 메뉴 그룹 목록을 조회 할 수 있다.

### Menu(메뉴)
- 메뉴는 **메뉴명**, **메뉴가격**, **속한 메뉴그룹**, **구성 상품 리스트**로 구성된다.
- 메뉴를 생성 할 수 있다.
  - 메뉴의 가격은 0 이상 이어야 한다.
  - 메뉴의 가격은 메뉴의 구성 상품들의 가격 총합보다 작거나 같다.
  - 속한 메뉴그룹이 반드시 존재한다.
- 메뉴 목록을 조회 할 수 있다.

### OrderLineItem(주문항목)
- 주문 항목은 **주문**, **주문한 메뉴**, **주문한 메뉴의 수량** 으로 구성된다.

### OrderTable(주문 테이블)
- 주문 테이블은 **단체 주문 테이블**, **손님의 수**, **빈 테이블 여부** 로 구성된다.
- 주문 테이블을 생성 할 수 있다.
- 주문 테이블 목록을 조회 할 수 있다.
- 테이블의 비어있는 상태를 변경 할 수 있다.
  - 단체로 주문 테이블이 묶여 있으면 빈 테이블로 상태를 변경할 수 없다.
  - 주문 테이블의 주문 상태가 `식사중`이거나, `요리중`인 경우 상태를 변경할 수 없다.
- 주문 테이블의 손님의 수를 변경 할 수 있다.
  - 손님의 수는 0 이상 이어야 한다.
  - 변경할 주문 테이블이 존재해야 한다.
  - 주문 테이블이 빈 테이블이면 손님의 수를 변경할 수 없다.

### Order(주문)
- 주문은 **주문한 테이블**, **주문상태**, **주문시각**, **주문 항목 리스트**로 구성된다.
- 주문을 생성 할 수 있다.
  - 주문 항목은 반드시 존재해야 한다.
  - 주문 메뉴 하나당 주문 항목이 한개씩 존재해야 한다.
  - 주문을 요청한 테이블이 존재해야 한다.
  - 주문을 요청할 수 없는 테이블에서는 주문할 수 없다.
    - 빈 테이블인 경우
    - 유효하지 않은 주문테이블인 경우
- 주문 목록을 조회 할 수 있다.
- 주문 상태를 변경 할 수 있다.
  - 이미 완료되었거나, 주문이 생성되어 있지 않은 경우 상태를 변경할 수 없다

### TableGroup(단체)
- 단체는 **단체지정 시각**, **단체의 주문 테이블 리스트**로 구성된다.
- 단체로 지정 할 수 있다.
  - 단체로 지정할 주문 테이블이 존재해야 한다.
  - 단체로 지정할 주문 테이블은 2개 이상이어야 한다.
  - 단체로 지정할 주문 테이블이 빈 테이블이 아니거나  
  이미 다른 단체로 지정되어 있다면, 단체로 지정할 수 없다.

## 용어 사전

| 한글명      | 영문명              | 설명                            |
|----------|------------------|-------------------------------|
| 상품       | product          | 메뉴를 관리하는 기준이 되는 데이터           |
| 메뉴 그룹    | menu group       | 메뉴 묶음, 분류                     |
| 메뉴       | menu             | 메뉴 그룹에 속하는 실제 주문 가능 단위        |
| 메뉴 상품    | menu product     | 메뉴에 속하는 수량이 있는 상품             |
| 금액       | amount           | 가격 * 수량                       |
| 주문 테이블   | order table      | 매장에서 주문이 발생하는 영역              |
| 빈 테이블    | empty table      | 주문을 등록할 수 없는 주문 테이블           |
| 주문       | order            | 매장에서 발생하는 주문                  |
| 주문 상태    | order status     | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정    | table group      | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목    | order line item  | 주문에 속하는 수량이 있는 메뉴             |
| 매장 식사    | eat in           | 포장하지 않고 매장에서 식사하는 것           |

## 1단계 - 테스트를 통한 코드 보호

### 기능 목록
- [x] 키친포스 시스템의 요구사항을 작성
  - [x] table create 문 분석 후 각 domain 간 관계를 찾아본다 (가능하면, ERD를 직접 그려볼 것)
  ![ERD.png](images%2FERD.png)
  - [x] 각 도메인의 관계를 분석하여, `Bounded Context`를 설정 해 본다.
    - 메뉴
    - 주문
  - [x] 분석한 도메인 간의 관계를 말로 잘 풀어서 요구사항을 정리 해 본다.
- [x] 키친포스의 요구사항을 토대로 테스트 코드를 작성
  - [x] `@SpringBootTest`로 통합 테스트 코드 작성
    - [x] 메뉴 통합테스트
    - [x] 주문 통합테스트
  - [x] `@ExtendWith(MockitoExtension.class)` 를 이용해 단위 테스트 코드 작성
    - [x] `Product`
      - [x] create 단위 테스트 작성 
      - [x] list 단위 테스트 작성
    - [x] `MenuGroup`
      - [x] create 단위 테스트 작성
      - [x] list 단위 테스트 작성
    - [x] `Menu`
      - [x] create 단위 테스트 작성
      - [x] list 단위 테스트 작성
    - [x] `Table`
      - [x] create 단위 테스트 작성
      - [x] list 단위 테스트 작성
      - [x] changeEmpty 단위 테스트 작성
      - [x] changeNumberOfGuests 단위 테스트 작성
    - [x] `Order`
      - [x] create 단위 테스트 작성
      - [x] list 단위 테스트 작성
      - [x] changeOrderStatus 단위 테스트 작성
    - [x] `TableGroup`
      - [x] create 단위 테스트 작성
      - [x] ungroup 단위 테스트 작성
  - [x] 인수 테스트 코드 작성
    - [x] 메뉴 인수 테스트 작성
    - [x] 주문 인수 테스트 작성

### 1단계 피드백
- [x] 제약조건은 `~한다` 로, 가능한 행위는 `~할 수 있다`로 작성
- [x] 테스트용 리소스를 생성하는 역할은 `TestFixture`라는 명칭으로 주로 명명
- [x] 라이브러리 의존성은 더이상 추가하지 않는 것을 권장
- [x] 테스트코드의 setup 메서드 내에서 조건을 설정하는 것을 분리
  - 객체의 상태를 각각의 테스트 코드에서 변경하면 `테스트의 독립성`이 보장되지 않음

## 2단계 - 서비스 리팩터링

### 기능목록 작성
- [ ] 각 도메인에 `Spring Data JPA Entity`로 수정할 것
  - JPA를 사용하는 근본적인 이유는 객체지향.
- [ ] Dao를 없애고, Repository 구현 


### 요구사항
- 단위 테스트 하기 어려운 코드와 가능한 코드를 분리
  - 단위 테스트 구현
- Spring Data JPA 사용 시 ```spring.jpa.hibernate.ddl-auto=validate``` 옵션을 필수로 준다.(Flyway 적용)
- 데이터베이스 스키마 변경 및 마이그레이션 필요 시 참고
  - [DB도 형상관리 해보자](https://meetup.toast.com/posts/173)

### 프로그래밍 요구사항
- Lombok 없이 미션 진행
- 객체지향 생활 체조 원칙 준수

### 비즈니스 로직의 위치에 대한 고민
- TDD, OOP 를 적용하려면 핵심 비즈니스 로직을 도메인 객체가 담당하도록 구현하는 것
- 테스트하기 쉬운 코드로 변경하고, 지속적인 리팩터링

힌트 
- 모델에 비즈니스 로직을 최대한 모은다
  - 프레임워크나 외부 종속관계가 줄어들기 때문에 테스트하기가 매우 수월해진다
- 반복적인, 단계적인 설계
- 모델에 setter 를 무조건적으로 추가하지 않기
  - setter는 도메인의 핵심 개념이나 의도를 코드에서 사라지게 함
  - setter는 도메인 객체를 생성할 때 완전한 상태가 아닐 수도 있다는 것(생성할 때 완전한 상태로 만들 것)