# spring-batch-tutorial
## 설정
- db 테이블 생성
  - resources에 ddl/schema-postrgresql.sql 을 통해 db table 생성
  - 다른 DB라면 [링크](https://github.com/spring-projects/spring-batch/tree/main/spring-batch-core/src/main/resources/org/springframework/batch/core)의 ddl문을 통해 db table 생성
- resources에 secret.yml 생성
``` 
spring:
  datasource:
    hikari:
      schema: {if postgresql, schema name}
    url: {db url}
    username: {username}
    password: {password}
    driver-class-name: {jdbc driver}
```

## 1주차
- [설치 및 스키마 설명](https://github.com/sajacaros/spring-batch-tutorial/blob/main/docs/01_intro.md)
## 2주차
- [tasklet](https://github.com/sajacaros/spring-batch-tutorial/blob/main/docs/02_tasklet.md)
- 추가 구현 내용
  - api 호출할때마다 tasklet 실행
    ``` 
    ### Run tasklet
    GET localhost:8010/tasklet
    
    ### Run tasklet aync
    GET localhost:8010/tasklet-async
    ```
## 3주차
- [chunk](https://github.com/sajacaros/spring-batch-tutorial/blob/main/docs/03_chunk_tasklet_model.md)
- 추가 구현 내용
  - api 호출할때마다 입력한 정보로 db의 metadata를 excel에 저장
  - DB는 mysql, oracle, postgresql 지원
    ```
    ### fetch metadata => save to excel
    POST localhost:8010/metadata
    Accept: application/json
    Content-Type: application/json
    
    {
        "url": "jdbc:{dbms name}://{ip}:{port}/{db name}",
        "username": "{username}",
        "password": "{password}",
        "filePath": "{filePath}",
        "schema": "{schema name}"
    }
    ```
## 4주차
- [flat file](https://github.com/sajacaros/spring-batch-tutorial/blob/main/docs/04_flat_file_practice.md)
## 5주차
- [JdbcPagingItemReader/Writer](https://github.com/sajacaros/spring-batch-tutorial/blob/main/docs/05_JdbcPaging.md)