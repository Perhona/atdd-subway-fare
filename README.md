# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션
---
# 🚀 실습 - Cucumber 전환

## 요구사항
- [x] 경로 조회 인수 테스트 cucumber 전환하기
  - [x] features 작성 (DataTable / 파라미터 / 공유 객체 활용)
  - [x] cucumber 환경에서의 데이터 초기화 설정<br>
    `Before과 Background 모두 각 시나리오 전에 수행된다는 점은 같다.`
    - Before : 메서드 형식으로 사용하기 때문에 보다 명시적으로 보여주기 위해 Background 사용을 권장한다.
    - Background : 각 시나리오 마다 수행하지만 시점은 Before 이후이며 feature 파일에 작성한다.
```
PathAcceptanceTest의 경로 조회 인수 테스트를 cucumber 기반으로 작성합니다.
station.feature를 참고하여 진행하세요.
cucumber가 제공하는 기능을 적절히 활용해보세요.
```
---
# 🚀 1단계 - 경로 조회 타입 추가

## 요구사항
- [x] 최소 시간 경로 타입 추가(기존의 최단 거리 기준에서 추가된다)
  - [x] 경로 조회 시 최소 시간 기준으로 조회할 수 있도록 기능 추가
  - [x] 노선추가 & 구간 추가 시 `거리`와 함께 `소요시간` 정보 추가
  - [x] 작은 단위로 커밋 진행

## 프로그래밍 요구사항
- 인수 테스트 주도 개발 프로세스에 맞춰서 기능 구현
  - [x] 인수 조건 정의 > 인수 테스트 작성 > 기능 구현
- [x] 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성
---
## Feedback 24.03.10

- [x] 중복되는 사전조건(지하철 역들/노선들을 생성한다)은 background를 활용해도 좋을 것 같다.
  - Background 설정
  - AcceptanceContext에서 station객체와 line 객체를 각각 관리 
- [x] 패키지 구조의 게층이 잘 그려지지 않는 문제. subway 하위에 도메인으로 나뉠지, 계층별로 나뉠지 통일이 필요해 보임
  - `section`과 `path`패키지를 `subway`하위로 이동
- [ ] PathFinder 객체의 이름이 PathFinder 보다는 Path나 SubwayMap과 같이 바라볼 수 있을 것 같음. 고민 필요
  - Path vs PathFinder / 명확한 답을 내리기 어렵다
- [x] 검증 시 resopnse.jsonPath().getList("...")와 같은 부분의 각 단계를 메서드로 분리하면 재사용도 가능하고 시나리오가 더욱 명확해질 수 있다.
- [x] 거리의 타입이 int 대신 long 인 이유는?
  - 표현할 수 있는 값의 범위가 큰 것이 무조건적인 장점일까? 저장할 데이터의 최소/최대 범위를 고려해서 자료형을 정해야 한다.
---
# 🚀 2단계 - 요금 조회

## 요구사항 - 경로 조회 시 요금 정보 포함
- [x] 경로 조회 결과에 요금 정보를 포함한다.
  - 지하철 운임은 거리비례제로 책정됩니다. (실제 경로가 아닌 최단거리 기준)
  - 최단 거리가 계산되면 요금을 책정한다.
```
기본운임(10㎞ 이내) : 기본운임 1,250원
이용 거리초과 시 추가운임 부과
10km초과∼50km까지(5km마다 100원)
50km초과 시 (8km마다 100원)
```

## 프로그래밍 요구사항
- [x] 인수 테스트 주도 개발 프로세스에 맞춰 기능을 구현한다.
  - 인수 조건 정의 -> 인수 테스트 작성 -> 인수 테스트 충족 기능 구현 -> 리팩터링
  - 인수 조건을 주석으로 작성한다
  - 인수 테스트 이후 기능 구현은 TDD로 진행
---
## Feedback 24.03.12
- [x] `Path`는 경로 자체를 의미하니, 노선 목록 보다는 구간 목록이 자연스러워 보임
  - `Path`가 `Section`을 갖도록 변경
- [ ] `Path`가 구간 목록을 상태로 가진다면, 객체의 응집도를 고려했을 때 요금 계산은 `Path`가 갖는 것이 자연스러워 보임
  - 요금 계산 로직의 내용이 다소 많게 느껴져 고민
- [x] 요금 계산 하는 로직을 메서드를 분리해 볼 것
- [x] 스텝 메서드의 배치와 분리를 고민해보기
  - Station | Line | Path 기준으로 각각 분리

---
# 🚀 3단계 - 요금 정책 추가

## 요구사항 - 스펙 추가하기
- [ ] 추가된 요금 정책 반영하기
```
1. 노션별 추가 요금
- 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가한다.
  ex) 신분당선 추가요금 900원 / 8km 이용 / 1,250 -> 2,150원(최종)
  ex) 신분당선 추가요금 900원 / 12km 이용 / 1,350 -> 2,250원(최종)
- 경로 중 추가 요금이 있는 노선을 환승하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
  ex) 추가요금 0원, 500원, 900원 노선 경유 / 8km 이용 / 1,250 -> 2,150원(최종)
```
```
2. 로그인 사용자 연령별 요금 계산
- 청소년: 운임에서 350원을 공제한 금액의 20% 할인
  (13세 이상 ~ 19세 미만)
- 어린이: 운임에서 350원을 공제한 금액의 50% 할인
  (6세 이상 ~ 13세 미만)
```

## 프로그래밍 요구사항
- [ ] 인수테스트 주도 개발 프로세스에 맞춰 기능 구현
- [ ] 인수 테스트 이후 기능 구현은 TDD로 진행
  - 도메인 레이어 테스트는 필수
  - 서비스 레이어 테스트는 선택
