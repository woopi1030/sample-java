# map_to_pojo
---

- 무엇인가?
    - Map<Stirng, Object>과 Pojo를 비교하고, 안의 Key값과, 멤버변수, 값이 같은지 확인하는 로직
- 왜 필요한가?
    - 회사에서 리펙토링 내용 중에 AS-IS 소스가 전부 Map<String, Object> 로 반환하고 있었는데,
    이것을 Dto로 변환하는 작업을 진행하려고 한다.
    - 서비스도 바꾸기 때문에 기존 응답 데이터와의 정합성을 확인하기 위해서 Map<String, Object> 와 Dto(Pojo)를 비교함으로서 서비스가 제대로 개발되었는 지 확인하는 로직을 짜려고 한다. 