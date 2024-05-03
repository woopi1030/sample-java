package file_read;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileReadCsv {
    public static void main(String[] args) {
        List<List<String>> CSVlists = readCSV();
        System.out.println(CSVlists.toString());
        CSVlists.stream().forEach(ul -> ul.stream().forEach(CSVList -> System.out.println(CSVList.toString())));
    }

    /**
     * CSV 파일 읽고, 리스트 반환
     * 
     * @param args
     * @return List<List<String>>
     */
    public static List<List<String>> readCSV() {

        // 패키지 명을 가져오기 위한 처리
        Class c = null;
        /* 
        Class c2 = new FileReadCSV().getClass();
        System.out.println(c2.getName());
        System.out.println(c2.getPackage().getName()); 
        */

        try {
            c = Class.forName("file_read.FileReadCsv");
            System.out.println("Class : " + c.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 자신을 클래스를 가져올 때의 처리
        // FileReadCSV myClass = new FileReadCSV();
        // System.out.println(myClass.getC().getPackageName());
        // System.out.println(myClass.getC().getSimpleName());

        String filename = "csv_sample.csv";
        Path pathTofile = Paths.get("app\\src\\main\\java\\file_read").toAbsolutePath();
        System.out.println("path to file : " + pathTofile);

        List<List<String>> ret = new ArrayList<List<String>>();
        BufferedReader br = null;

        // CSV파일 불러오기
        try {
            System.out.println(pathTofile + "\\" + filename);
            br = Files.newBufferedReader(
                    Paths.get(pathTofile + "\\" + filename),
                    Charset.forName("UTF-8"));

            String line = "";

            // 파일에서 한줄씩 읽어온다
            while ((line = br.readLine()) != null) {
                // CSV 각 행을 (임시) 저장하는 리스트
                List<String> tmpList = new ArrayList<String>();
                // split : ','기준으로 나눈 문자열을 배열에 저장아하여 리턴함
                String array[] = line.split(",");
                // 배열에서 리스트로 변환
                tmpList = Arrays.asList(array);
                // 변환된 리스트를 리스트에 추가
                ret.add(tmpList);
            }
            return ret;
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }

        return ret;

    }
}
