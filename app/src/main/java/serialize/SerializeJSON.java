package serialize;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SerializeJSON {
    public static void main(String[] args) {
        //객체 생성
        Member member = new Member("테스트", "test@test.com", 25);

        // member객체를 json으로 변환 
        String json = String.format(
            "{\"name\":\"%s\",\"email\":\"%s\",\"age\":%d}",
            member.getName(), member.getEmail(), member.getAge());

        
        byte[] jsonByte = json.getBytes();
        System.out.println(new String(jsonByte));
        System.out.printf("json (byte size = %s)\n", jsonByte.length);
    }
}