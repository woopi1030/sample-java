package file_read;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FileReadJson {
    public static void main(String[] args) {
        readJson();
    }

    /**
     * Json파일 읽고 반환
     * @return Map<String, String>
     */
    public static void readJson() {

        Class c = new FileReadJson().getClass();
        System.out.println(c.getName());
        System.out.println(c.getPackage().getName()); 

        //파일경로
        String filename = "json_sample.json";     //파일명
        Path pathTofile = Paths.get("app\\src\\main\\java\\file_read").toAbsolutePath();
        System.out.println("path to file : " + pathTofile);  

        try {

            // ObjectMapper 객체 생성
            ObjectMapper mapper = new ObjectMapper();

            // JSON 파일 읽어오기
            Object jsonObject = mapper.readValue(new FileReader(pathTofile + "\\" + filename), Object.class);
            System.out.println(jsonObject);


            //파일을 읽어온다. 
            //1. filereader, BufferedReader   (char단위)
            //2. inputstreamReader , BufferedReader  (byte단위)

            //FileInputStream fis = new FileInputStream(Paths.get(pathTofile.toAbsolutePath().toString()) + filename);    
            FileInputStream fis = new FileInputStream(pathTofile + "\\" + filename);    
            BufferedInputStream bis = new BufferedInputStream(fis);    
            InputStreamReader isr = new InputStreamReader(bis,Charset.forName("UTF-8"));   //UTF-8 charset
            BufferedReader br = new BufferedReader(isr);


            try {
                int i = 0;      //3 : 한 바이트를 담을 변수
                
                // while(i != -1) {
                //     i = br.read(); //한 바이트 읽기
                //     System.out.print((char)i);  //한 바이트로 받아서 출력    
                // }

                //byte배열에 담아서 String객체로 변환
                byte[] readByteArr = new byte[bis.available()];     //파일 내용을 담을 byte array선언, fis.available() : 남은 바이트 수를 리턴해줌, 처음에 선언하면 전체 바이트 수 가 리턴됨으로 전체 길이의 byte array가 생성됨
                //read하면서, 인자로 들어간 byte array에 1바이트씩 들어간다.
                while (bis.read(readByteArr) != -1) {
                }
                System.out.println(new String(readByteArr, Charset.forName("UTF-8")));
                
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                    isr.close();
                    bis.close();
                    fis.close();
                } catch (Exception e) {

                }
            }
            
            // ObjectMapper objectMapper = new ObjectMapper();

            // Map<?, ?> tmpMap= objectMapper.convertValue(obj, Map.class);

            // System.out.println("obj : " + obj);
            // System.out.println("tmpMap : " + tmpMap);

        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
