package change_int_byte;

import java.nio.ByteBuffer;

/**
 * ChangeIntByteArray
 */
public class ChangeIntByteArray {
    public static void main(String[] args) {
        
        int testValue = 123456789;
        /**
         * type
         * 1: 0xFF 포함
         * 2: 0xFF 미포함
         */
        int type = 1;
        byte[] result = new byte[4];
        
        // 1. Int to ByteArray
        result = ConvertIntToByteArray(testValue, type);

        // 2. ByteArray To Int
        ConvertByteArrayToInt(result);
        
    }

    /**
     * Int To Byte
     */
    public static byte[] ConvertIntToByteArray(int testValue, int type) {
        byte[] byteArray = new byte[4];

        // TCP 통신을 위해 전문 길이부(int)를 구한 후 이를 byteArray로 바꾸어줘야 하는 경우가 있다.
        // 0xFF = 11111111 : 논리 연산을 통해서 8비트씩 자른다.(안써도 적용은 된다.)
        // 0xFF는 비트 AND 연산(& 0xFF)을 위한 값 
        // https://emflant.tistory.com/133
        if (type == 1) {
            byteArray[0] = (byte) ((testValue >> 24) & 0xFF);
            byteArray[1] = (byte) ((testValue >> 16) & 0xFF);
            byteArray[2] = (byte) ((testValue >> 8) & 0xFF);
            byteArray[3] = (byte) ((testValue) & 0xFF);
    
            System.out.println("1 : [" + new String(byteArray) + "]");
        }
        if (type == 2) {
            byteArray[0] = (byte) ((testValue >> 24));
            byteArray[1] = (byte) ((testValue >> 16));
            byteArray[2] = (byte) ((testValue >> 8));
            byteArray[3] = (byte) ((testValue));
    
            System.out.println("2 : [" + new String(byteArray) + "]");
        }
        // ByteBuffer를 활용하여 int to byteArray
        if (type == 3) {
            byteArray = ByteBuffer.allocate(4).putInt(testValue).array();
    
            System.out.println("3 : [" + new String(byteArray) + "]");
        }

        return byteArray;
    }

    /**
     * Byte To Int
     */
    public static void ConvertByteArrayToInt (byte[] byteArray) {
        int resultInt;

        //ByteBuffer를 이용하여 변환
        resultInt = ByteBuffer.wrap(byteArray).getInt();

        System.out.println("1 : [" + resultInt + "]");

        resultInt = ((byteArray[0] & 0xFF) << 24) |
                    ((byteArray[1] & 0xFF) << 16) |
                    ((byteArray[2] & 0xFF) << 8) |
                    ((byteArray[3] & 0xFF) << 0);

        System.out.println("2 : [" + resultInt + "]");



    }


}