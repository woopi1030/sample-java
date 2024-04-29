package compare_db_tables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CompareDbTables {
    // TODO: Db Connection Class는 따로 만들 예정
    public static void main(String[] args) {
        // Init DB Connection
        List<String> sameTables = new ArrayList<>();
        List<String> compareTables1 = new ArrayList<>();
        List<String> compareTables2 = new ArrayList<>();
        try {
            // 비교 대상 1번 테이블 리스트 조회
            DatabaseConnectInfo databaseConnectInfo_1 = DatabaseConnectInfo.builder()
                    .databaseType("")     // ORACLE, MARIADB
                    .url("")
                    .schema("")
                    .userName("")
                    .password("")
                    .encodingType("")
                    .build();
            DatabaseQueryMap databaseQueryMap_1 = DatabaseQueryMap.builder()
                    .databaseConnectInfo(databaseConnectInfo_1)
                    .build();
            Statement statement_1 = getSchemaStatement(databaseConnectInfo_1);
            ResultSet resultSet_1 = statement_1.executeQuery(databaseQueryMap_1.getTableListQuery());
            
            while (resultSet_1.next()) {
                //배치테이블 제외
                if(!resultSet_1.getString("TABLE_NAME").startsWith("BATCH")) {
                    compareTables1.add(resultSet_1.getString("TABLE_NAME"));
                }
            }


            // 비교 대상 2번 테이블 리스트 조회 및 비교 테이블 리스트 생성
            DatabaseConnectInfo databaseConnectInfo_2 = DatabaseConnectInfo.builder()
                    .databaseType("")
                    .url("")
                    .schema("")
                    .userName("")
                    .password("")
                    .encodingType("")
                    .build();
            DatabaseQueryMap databaseQueryMap_2 = DatabaseQueryMap.builder()
                    .databaseConnectInfo(databaseConnectInfo_2)
                    .build();
            Statement statement_2 = getSchemaStatement(databaseConnectInfo_2);
            ResultSet resultSet_2 = statement_2.executeQuery(databaseQueryMap_2.getTableListQuery());
            
            while (resultSet_2.next()) {
                if (compareTables1.contains(resultSet_2.getString("TABLE_NAME"))) {
                    sameTables.add(resultSet_2.getString("TABLE_NAME"));
                    compareTables1.remove(resultSet_2.getString("TABLE_NAME"));
                } else {
                    //배치테이블 제외
                    if (!resultSet_2.getString("TABLE_NAME").startsWith("BATCH")){
                        compareTables2.add(resultSet_2.getString("TABLE_NAME"));    
                    }
                }
            }
            
            for (String table : sameTables) {

                // 이중 for문으로 하나씩 비교(속성까지)
                // 컬럼 비교
                List<TableColumn> compareColumns1 = new ArrayList<>();
                List<TableColumn> compareColumns2 = new ArrayList<>();

                System.out.println("==========================================================");
                System.out.println("테이블 명 : " + table);
                
                resultSet_1 = statement_1.executeQuery(databaseQueryMap_1.getColumnList(table));
                resultSet_2 = statement_2.executeQuery(databaseQueryMap_2.getColumnList(table));
                
                while (resultSet_1.next()) {
                    TableColumn columnsDto = new TableColumn(
                            resultSet_1.getString("COLUMN_NAME"),
                            resultSet_1.getString("COLUMN_TYPE"),
                            resultSet_1.getString("COLUMN_DEFAULT_VALUE"),
                            resultSet_1.getString("COLUMN_IS_NULL"),
                            resultSet_1.getString("COLUMN_LENGTH"));
                    compareColumns1.add(columnsDto);
                }

                while (resultSet_2.next()) {
                    TableColumn columnsDto = new TableColumn(
                            resultSet_2.getString("COLUMN_NAME"),
                            resultSet_2.getString("COLUMN_TYPE"),
                            resultSet_2.getString("COLUMN_DEFAULT_VALUE"),
                            resultSet_2.getString("COLUMN_IS_NULL"),
                            resultSet_2.getString("COLUMN_LENGTH"));
                    compareColumns2.add(columnsDto);
                }
                
                compareColumn(compareColumns1, compareColumns2);
            }

            System.out.println("========================= 1번 스키마에만 있는 테이블 =========================");
            compareTables1.stream().forEach(System.out::println);

            System.out.println("========================= 2번 스키마에만 있는 테이블 =========================");
            compareTables2.stream().forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Inner Class

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DatabaseConnectInfo {

        private String databaseType;

        private String url;

        private String schema;

        private String userName;

        private String password;

        private String encodingType;

        // Method
        public String getFullConnectionUrl() {
            return this.url
                    + (this.schema.isEmpty() ? "" : "/" + this.schema)
                    + (this.encodingType.isEmpty() ? "" : "?characterEncoding=" + this.encodingType);
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DatabaseQueryMap {

        private DatabaseConnectInfo databaseConnectInfo;

        // Method
        public String getTableListQuery() {
            String databaseType = this.databaseConnectInfo.getDatabaseType();

            if ("MARIADB".equals(databaseType)) {
                return "SHOW TABLES";
            } else if ("ORACLE".equals(databaseType)) {
                return String.format("SELECT TABLE_NAME FROM ALL_TABLES WHERE OWNER = '%s' order by TABLE_NAME",
                        this.databaseConnectInfo.getUserName().toUpperCase());
            } else {
                return "";
            }
        }

        public String getColumnList(String tableName) {
            String databaseType = this.databaseConnectInfo.getDatabaseType();

            if ("MARIADB".equals(databaseType)) {
                return String.format(
                        """
                                SELECT *
                                FROM information_schema.columns
                                WHERE table_name = '%s'
                                """, tableName);
            } else if ("ORACLE".equals(databaseType)) {
                return String.format(
                        """
                                SELECT
                                       COLUMN_NAME   AS COLUMN_NAME
                                     , DATA_TYPE     AS COLUMN_TYPE
                                     , DATA_DEFAULT  AS COLUMN_DEFAULT_VALUE
                                     , NULLABLE      AS COLUMN_IS_NULL
                                     , CHAR_LENGTH   AS COLUMN_LENGTH
                                FROM ALL_TAB_COLUMNS
                                WHERE TABLE_NAME = '%s'
                                AND OWNER = 'CMS'
                                ORDER BY COLUMN_NAME
                                                       """, tableName);
            } else {
                return "";
            }
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TableColumn {

        @Builder.Default
        private String columnName = "";

        @Builder.Default
        private String dataType = "";

        @Builder.Default
        private String columnDefault = "";

        @Builder.Default
        private String isNullable = "";

        @Builder.Default
        private String length = "";

        // Method
        public Boolean checkSame(String table,
                                 String columnName,
                                 String dataType,
                                 String columnDefault,
                                 String isNullable,
                                 String length) {

            if (!Objects.equals(this.columnName, columnName)
                || !Objects.equals(this.dataType, dataType)
                || !Objects.equals(this.columnDefault, columnDefault)
                || !Objects.equals(this.isNullable, isNullable)
                || !Objects.equals(this.length, length)) {
                return false;
            }
            return true;
        }
    }

    public static void compareColumn(List<TableColumn> compareColumns1, List<TableColumn> compareColumns2) {
        Map<String, TableColumn> columnMap1 = compareColumns1.stream()
                .collect(Collectors.toMap(TableColumn::getColumnName, column -> column));

        Map<String, TableColumn> columnMap2 = compareColumns2.stream()
                .collect(Collectors.toMap(TableColumn::getColumnName, column -> column));

        for (String columnName : columnMap1.keySet()) {
            if (columnMap2.containsKey(columnName)) {
                TableColumn column1 = columnMap1.get(columnName);
                TableColumn column2 = columnMap2.get(columnName);

                if (columnsAreEqual(column1, column2)) {
                    System.out.println("'" + columnName + "' 이상무.");
                }

            } else {
                System.out.println(" * '" + columnName + "' 첫번째 스키마의 테이블에만 존재.");
            }
        }

        for (String columnName : columnMap2.keySet()) {
            if (!columnMap1.containsKey(columnName)) {
                System.out.println(" * '" + columnName + "' 두번째 스키마의 테이블에만 존재.");
            }
        }
    }
    
    public static boolean columnsAreEqual(TableColumn column1, TableColumn column2) {
        if (!Objects.equals(column1.getDataType(), column2.getDataType())) {
            System.out.println(" * '" + column1.columnName + "'.'dataType'가 다름 : " + column1.getDataType() + " vs " + column2.getDataType());
            return false;
        }
        if (column1.getColumnDefault() != null && column2.getColumnDefault() != null) {
            if (!Objects.equals(column1.getColumnDefault().trim(), column2.getColumnDefault().trim())) {
                System.out.println(" * '" + column1.columnName + "'.'columnDefault'가 다름 : " + column1.getColumnDefault() + " vs " + column2.getColumnDefault());
                return false;    
            }
        }
        if (!Objects.equals(column1.getIsNullable(), column2.getIsNullable())) {
            System.out.println(" * '" + column1.columnName + "'.'isNullable'가 다름 : " + column1.getIsNullable() + " vs " + column2.getIsNullable());
            return false;
        }
        if (!Objects.equals(column1.getLength(), column2.getLength())) {
            System.out.println(" * '" + column1.columnName + "'.'length'가 다름 : " + column1.getLength() + " vs " + column2.getLength());
            return false;
        }
        return true;
    }

    // Private Method

    /**
     * Get Database Connection
     * 
     * @param databaseConnectInfo
     * @return
     * @throws SQLException
     */
    private static Connection getSchemaConnection(DatabaseConnectInfo databaseConnectInfo) throws SQLException {
        Connection connection = DriverManager.getConnection(
                databaseConnectInfo.getFullConnectionUrl(),
                databaseConnectInfo.getUserName(),
                databaseConnectInfo.getPassword());
        if (connection.isValid(0)) {
            return connection;
        } else {
            throw new SQLException("DB Connection Not Valid");
        }
    }

    private static Statement getSchemaStatement(DatabaseConnectInfo databaseConnectInfo) throws SQLException {
        return getSchemaConnection(databaseConnectInfo).createStatement();
    }
}
