package serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class SerializeString {
    public static void main(String[] args) {
        String encodedSerializeMember;
        //직렬화
        encodedSerializeMember = serialize();
        //역직렬화
        deserialize(encodedSerializeMember);
    }

    //직렬화
    public static String serialize() {
        Member member = new Member("테스트", "test@test.com", 25);
        byte[] serializedMember = null;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(member);
                // serializedMember -> 직렬화된 member 객체 
                serializedMember = baos.toByteArray();
                   
                System.out.println(member.toString());
                System.out.println("Encoded : " + Base64.getEncoder().encodeToString(serializedMember));
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(serializedMember);
    }
    //역직렬화
    public static void deserialize(String encodedSerializeMember) {
        // 직렬화 예제에서 생성된 base64 데이터 
        byte[] serializedMember = Base64.getDecoder().decode(encodedSerializeMember);

        System.out.printf("serializedMember (byte size = %s) \n", serializedMember.length);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(serializedMember)) {
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                // 역직렬화된 Member 객체를 읽어온다.
                Object objectMember = ois.readObject();
                Member member = (Member) objectMember;
                System.out.println("decoded : " + member);
            } catch (IOException ie) {
                ie.printStackTrace();
            } catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
            }
        } catch(IOException ie) {
            ie.printStackTrace();
        }
    }
}