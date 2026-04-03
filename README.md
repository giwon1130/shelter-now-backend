# shelter-now-backend

현재 위치 기준으로 가까운 대피소와 쉼터를 탐색할 수 있게 만드는 백엔드 MVP다.
현재는 서울, 경기, 강원, 충청, 전라, 경상, 제주를 포함한 전국 권역 샘플 데이터를 함께 제공한다.

## 현재 구현 범위
- 대피소/쉼터 목록 조회
- 유형 필터
- 자치구 필터
- 운영 상태 필터
- 정렬(거리순/수용인원순/이름순)
- 현재 위치 기준 거리 계산
- 지도용 그룹 응답
- 대피소 상세 조회

## API
- `GET /api/v1/shelters`
- `GET /api/v1/shelters?query=강남`
- `GET /api/v1/shelters?shelterType=무더위쉼터&district=강남구`
- `GET /api/v1/shelters?openStatus=운영중&sortBy=capacity`
- `GET /api/v1/shelters?latitude=37.5665&longitude=126.9780`
- `GET /api/v1/shelters/map`
- `GET /api/v1/shelters/{shelterId}`

## 다음 단계
- 전국 단위 원본 대피소/쉼터 데이터 수집 및 적재 파이프라인 구축 (다음 단계)
- 전국 데이터 정규화(행정구역/좌표/운영 상태) 후 조회 API 전환
- 실시간 운영 상태 반영
- 지도 탐색 UI 고도화
