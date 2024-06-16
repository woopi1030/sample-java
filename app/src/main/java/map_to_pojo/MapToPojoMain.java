package map_to_pojo;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MapToPojoMain {

    public static void main(String[] args) {
        System.out.println("Hello MapToPojo !!");

        // 예시 테스트
        Map<String, Object> map = new HashMap<>();
        map.put("name", "홍길동");
        map.put("age", "30");

        Person person = new Person("홍길동", 30);

        boolean result = compare(map, person);
        System.out.println("Map과 Pojo 값 비교 결과: " + result);
    }

    public static boolean compare(Map<String, Object> map, Object pojo) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Map을 Pojo 타입으로 변환
            Object convertedPojo = mapper.convertValue(map, pojo.getClass());

            // Pojo 객체들 간의 equals() 비교
            if (!convertedPojo.equals(pojo)) {
                return false;
            }

            // 키별 값 비교 및 타입 검사
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                // Pojo 객체의 필드 값 추출
                Object pojoFieldValue = getFieldValue(pojo, key);

                // 값 비교
                if (!value.equals(pojoFieldValue)) {
                    return false;
                }

                // 타입 비교
                if (!value.getClass().equals(pojoFieldValue.getClass())) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            // 변환 또는 비교 오류 발생 시 false 반환
            return false;
        }
    }

    private static Object getFieldValue(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = obj.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }
    
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Person {
    private String name;
    private int age;

    // 생성자, getter, setter 등 생략
}
