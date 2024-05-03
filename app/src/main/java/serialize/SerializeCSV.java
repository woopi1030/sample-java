package serialize;

public class SerializeCSV {
    public static void main(String[] args) {
        //객체 생성
        Member member = new Member("테스트", "test@test.com", 25);

        // member객체를 csv로 변환 
        String csv = String.format("%s,%s,%d",member.getName(), member.getEmail(), member.getAge()); 
        System.out.println(csv);
    }
}