# sample-java
---

- 다양한 자바 기능을 테스트 하기위한 로직 모음
- jdk 버전 : 21
- gradle 버전 : 8.7

- 항목
    - change_int_byte
        - Int, Byte 변환
        - TCP 통신을 위해 전문 길이부(int)를 구한 후 이를 byteArray로 바꾸어줘야 하는 경우가 있다.
    - compare_db_tables
        - 두개의 db를 비교
        - 환경(dev, test, stage, prod 등)에 따른 db 테이블들을 비교하고자 만듦