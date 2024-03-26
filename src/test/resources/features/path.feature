Feature: 지하철 경로 검색
#    /* 교대역   ---   강남역
#         |             |
#       남부터미널역 --- 양재역
#     */
  Background:
    Given 지하철 역들을 생성한다
      | 교대역    |
      | 강남역    |
      | 양재역    |
      | 남부터미널역 |
    And 지하철 노선들을 생성한다
      | 이름   | 색상     | 출발역 | 도착역 | 거리 | 소요시간 | 추가요금 |
      | 이호선  | green  | 교대역 | 강남역 | 10 | 3    | 500  |
      | 신분당선 | red    | 강남역 | 양재역 | 14 | 5    | 900  |
      | 삼호선  | orange | 양재역 | 교대역 | 23 | 10   | 0    |
    And "삼호선"노선에 "양재역"에서 "남부터미널역"까지 거리가 5, 소요 시간이 6인 지하철 구간을 추가한다

  Scenario: 최단 거리 경로를 조회한다.
    Given 지하철역이 등록되어 있다
    And 지하철 노선이 등록되어 있다
    And 지하철 노선에 지하철역이 등록되어있다
    When "교대역"부터 "양재역"까지의 "최단 거리" 경로를 조회하면
    Then "교대역, 남부터미널역, 양재역" 경로와 거리 23km, 소요시간 10분으로 응답한다.

  Scenario: 최소 시간 경로를 조회한다.
    Given 지하철역이 등록되어 있다
    And 지하철 노선이 등록되어 있다
    And 지하철 노선에 지하철역이 등록되어있다
    When "교대역"부터 "양재역"까지의 "최소 시간" 경로를 조회하면
    Then "교대역, 강남역, 양재역" 경로와 거리 24km, 소요시간 8분으로 응답한다.

  Scenario: 두 역의 최단 거리 경로를 조회
    Given 지하철역이 등록되어 있다
    And 지하철 노선이 등록되어 있다
    And 지하철 노선에 지하철역이 등록되어있다
    When "교대역"부터 "양재역"까지의 "최단 거리" 경로를 조회하면
    Then "교대역, 남부터미널역, 양재역" 경로와 거리 23km, 소요시간 10분으로 응답한다.
    And 지하철 이용 요금 1550원도 함께 응답한다
# 10(1250) + 13 < 5 * 3 (+300) => 1550원

  Scenario: 추가된 요금 정책을 적용한 최소 시간 경로를 조회
    Given 지하철역이 등록되어 있다
    And 지하철 노선이 등록되어 있다
    And 지하철 노선에 지하철역이 등록되어있다
    And 지하철 노선에 추가요금이 등록되어있다
    When "교대역"부터 "양재역"까지의 "최소 시간" 경로를 조회하면
    Then "교대역, 강남역, 양재역" 경로와 거리 24km, 소요시간 8분으로 응답한다.
    And 지하철 이용 요금 2450원도 함께 응답한다
#    1,250 + 300 + 900(신분당선 추가요금)
