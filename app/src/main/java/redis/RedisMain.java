package redis;

import java.util.Set;
import java.util.logging.Logger;

import lombok.Builder;
import lombok.Data;
import redis.clients.jedis.Jedis;

public class RedisMain {
        private static Logger log = Logger.getLogger(RedisMain.class.getName());

    private static Jedis jedis;

    public static void main(String[] args) {
        log.info("=== hello world redis ===");

        RedisConnectionInfo redisConnectionInfo = RedisConnectionInfo.builder()
                .host("localhost")
                .port(6379)
                .build();

        try {
            jedis = connect(redisConnectionInfo);

            // 키, 값 세팅
            setKeyValue("test111", "test222");

            // 전체 키 목록
            Set<String> allKeys = getAllKeys();
            allKeys.stream().forEach(log::info);

            // 특정 키의 값 확인
            String value = getValue("test111");
            log.info(value);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(jedis);
        }

    }

    // Inner Class
    @Builder
    @Data
    public static class RedisConnectionInfo {
        // 접속 주소
        private String host;
        // 접속 포트
        private Integer port;
    }

    // CONNECTION METHOD

    /**
     * Redis 연결
     * 
     * @return
     */
    public static Jedis connect(RedisConnectionInfo redisConnectionInfo) {
        return new Jedis(
                "localhost",
                6379);
    }

    /**
     * Redis 종료
     * 
     * @param jedis
     */
    public static void close(Jedis jedis) {
        if (jedis != null && jedis.isConnected()) {
            log.info("Jedis Close");
            jedis.close();
        }
    }

    // GET METHOD

    /**
     * 전체 Key 조회
     * 
     * @param jedis
     * @return
     */
    public static Set<String> getAllKeys() {
        return jedis.keys("*");
    }

    /**
     * 특정 키 값 확인
     * 
     * @param jedis
     * @param key
     * @return
     */
    public static String getValue(String key) {
        return jedis.get(key);
    }

    // SET METHOD
    /**
     * 키, 값 세팅
     * 
     * @param key
     * @param value
     */
    public static void setKeyValue(String key, Object value) {
        jedis.set(key, value.toString());
    }
}
