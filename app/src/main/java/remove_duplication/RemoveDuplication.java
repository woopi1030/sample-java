package remove_duplication;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * RemoveDuplication
 */
public class RemoveDuplication {

     public static void main(String[] args) {


        // 배열 준비
        String[] arr = { "W", "O", "O", "P", "I", "I", "I", "i"};

        // 1. Array <-> Set (순서 보장 X)
        System.out.println("RemoveDuplicate_1 : " + Arrays.toString(RemoveDupplicate_1(arr)));

        // 2. Array <-> LinkedSet (순서 보장 O)
        System.out.println("RemoveDuplicate_2 : " + Arrays.toString(RemoveDupplicate_2(arr)));

        // 3. Stream, distinct()
        System.out.println("RemoveDuplicate_3 : " + Arrays.toString(RemoveDupplicate_3(arr)));

        // 4. 임시 배열을 사용하여 중복 제거 1
        System.out.println("RemoveDuplicate_4 : " + Arrays.toString(RemoveDupplicate_4(arr, arr.length)));

        // 5. 임시 배열을 사용하여 중복 제거 2
        System.out.println("RemoveDuplicate_5 : " + Arrays.toString(RemoveDupplicate_5(arr, arr.length)));
    }

    /**
     * 1. Array <-> Set (순서 보장 X)
     */
    public static String[] RemoveDupplicate_1 (String[] arr) {

        // 배열을 HashSet으로 변환 (중복이 제거됨)
        HashSet<String> hashSet = new HashSet<>(Arrays.asList(arr));
        // HashSet을 배열로 변환 (중복(Dup)이 제거된 배열)
        arr = hashSet.toArray(new String[0]);

        return arr;
    }

    /**
     * 2. Array <-> LinkedSet (순서 보장 O)
     */
    public static String[] RemoveDupplicate_2 (String[] arr) {

        // 배열을 HashSet으로 변환 (중복이 제거됨)
        LinkedHashSet<String> linkedhashSet = new LinkedHashSet<>(Arrays.asList(arr));
        // HashSet을 배열로 변환 (중복(Dup)이 제거된 배열)
        arr = linkedhashSet.toArray(new String[0]);

        return arr;
    }

    /**
     * 3. Stream, distinct()
     */
    public static String[] RemoveDupplicate_3 (String[] arr) {

        // 배열 -> Stream -> distinct (중복제거) -> 배열
        arr = Arrays.stream(arr).distinct().toArray(String[]::new);

        return arr;
    }

    /**
     * 4. 임시 배열을 사용하여 중복 제거 sorting을 한 후 중복을 제거하기 때문에 순서보장 X
     */
    public static String[] RemoveDupplicate_4 (String[] arr, int n) {

        // 배열 sorting
        //Arrays.sort(arr);

        // 임시 배열 선언
        String[] tempArr = new String[n]; 

        // 중복 제거 후 array의 길이
        int j = 0;  

        // 임시 배열 구성
        for (int i=0; i<n-1; i++){  
            if (arr[i] != arr[i+1]){  
                tempArr[j++] = arr[i];  
            }
        }
        tempArr[j++] = arr[n-1];       

        // 중복 제거된 배열 구성
        arr = new String[j];
        for (int i=0; i<j; i++){  
            arr[i] = tempArr[i];  
        }

        return arr;
    }

    /**
     * 5. 중복데이터를 치환 후 걸러네어 중복 제거
     */
    public static String[] RemoveDupplicate_5 (String[] arr, int n) {

        // 중복 제거 후 array의 길이
        int k = 0;  

        // 중복되는 데이터 "" 변환
        for (int i=0; i<n-1; i++){  
            for (int j=i+1;j<n;j++) {
                if (!arr[i].equals("") && arr[i].equals(arr[j])) {
                    arr[j] = "";
                    k++;
                }
            }
        }

        String[] resultArr = new String[n-k];
        int tmpindex = 0;

        //새로운 배열 생성
        for (String str : arr) {
            if (!"".equals(str)) {
                resultArr[tmpindex] = str;
                tmpindex++;
            }
        }

        return resultArr;
    }
}