package com.study.springbatch.util;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DynamicDataConfig {

    private static final Map<String, String> DB_TYPE_TO_DRIVER_MAP = new HashMap<>();

    static {
        DB_TYPE_TO_DRIVER_MAP.put("postgresql", "org.postgresql.Driver");
        DB_TYPE_TO_DRIVER_MAP.put("mysql", "com.mysql.cj.jdbc.Driver");
        DB_TYPE_TO_DRIVER_MAP.put("oracle", "oracle.jdbc.OracleDriver");
        // Add more mappings as needed
    }


    // Method to extract DB type from URL
    public static String extractDbType(String url) {
        String regex = "^jdbc:(\\w+):.*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }


    // Dynamic DataSource creation method
    public static DataSource createDataSource(String url, String username, String password) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        String dbType = extractDbType(url);
        if (dbType != null) {
            String driverClassName = DB_TYPE_TO_DRIVER_MAP.get(dbType);
            if (driverClassName != null) {
                dataSource.setDriverClassName(driverClassName);
            } else {
                throw new IllegalArgumentException("Unsupported DB type: " + dbType);
            }
        } else {
            throw new IllegalArgumentException("Invalid JDBC URL: " + url);
        }

        return dataSource;
    }

    // 동적으로 SqlSessionFactory 생성
    public static SqlSessionFactory createSqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        // MyBatis XML Mapper 설정이 있다면 추가
        sessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/*.xml")
        );

        return sessionFactoryBean.getObject();
    }
}