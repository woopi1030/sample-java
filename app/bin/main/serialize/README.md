# serialize
---

- 무엇인가?
    - 자바 직렬화란 자바 시스템 내부에서 사용되는 객체 또는 데이터를 외부의 자바 시스템에서도 사용할 수 있도록 바이트(byte) 형태로 데이터 변환하는 기술과 바이트로 변환된 데이터를 다시 객체로 변환하는 기술(역직렬화)을 아울러서 이야기함.시스템적으로 이야기하자면 JVM(Java Virtual Machine 이하 JVM)의 메모리에 상주(힙 또는 스택)되어 있는 객체 데이터를 바이트 형태로 변환하는 기술과 직렬화된 바이트 형태의 데이터를 객체로 변환해서 JVM으로 상주시키는 형태를 같이 이야기함.
- 왜 필요한가?
    - CSV, JSON, 프로토콜 버퍼 등은 시스템의 고유 특성과 상관없는 대부분의 시스템에서의 데이터 교환 시 많이 사용됨.하지만 “자바 직렬화 형태의 데이터 교환은 자바 시스템 간의 데이터 교환을 위해서 존재한다.”고 생각해야함.
- 자바 직렬화의 장점
    - 자바 직렬화는 자바 시스템에서 개발에 최적화되어 있음. 복잡한 데이터 구조의 클래스의 객체라도 직렬화 기본 조건만 지키면 큰 작업 없이 바로 직렬화, 역직렬화 가능. 당연하게 보이는 장점 중에 하나지만 데이터 타입이 자동으로 맞춰지기 때문에 관련 부분을 큰 신경을 쓰지 않아도 됨.그렇게 역직렬화가 되면 기존 객체처럼 바로 사용할 수 있게 됨. 개발자 입장에서 상당히 편한 부분.
- 자바 직렬화는 언제(when) 어디서(where) 사용될까?
    - JVM의 메모리에서만 상주되어있는 객체 데이터를 그대로 영속화(Persistence)가 필요할 때 사용됨. 시스템이 종료되더라도 없어지지 않는 장점을 가지며 영속화된 데이터이기 때문에 네트워크로 전송도 가능. 그리고 필요할 때 직렬화된 객체 데이터를 가져와서 역직렬 화하여 객체를 바로 사용할 수 있게 됨. 그런 특성을 살린 자바 직렬화는 많은 곳에서 이용됨.