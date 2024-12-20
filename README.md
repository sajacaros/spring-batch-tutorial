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
## SCDF 참고
- SCDF 서버 실행하기
``` 
java -jar spring-cloud-dataflow-server-2.11.5.jar \
  --spring.datasource.url={db url} 
  --spring.datasource.hikari.schema={if postgresql, write schema name, else skip} \
  --spring.datasource.username={username} \
  --spring.datasource.password={password} \
  --spring.datasource.driver-class-name={jdbc driver} \
  --spring.cloud.dataflow.features.streams-enabled=false 
```
- SCDF가 관리하는 테이블과 연동 필요
  - spring batch가 버전업이 되면서 스키마가 변경 됨
  - SCDF는 sync를 맞추기 위해 boot3 prefix가 붙은 테이블을 별도로 관리함
  - ![SCDF](https://github.com/sajacaros/spring-batch-tutorial/blob/main/docs/images/SCDF_meta.png)
- spring batch에 적용 방법
  - `spring-cloud-starter-task` 설치
  - spring batch가 바라보는 TABLE 명 변경(아래 설정 참고)
  - @enableTask 추가(아래 설정 참고)
  ``` 
  @Configuration
  @EnableTask
  @EnableBatchProcessing(tablePrefix = "boot3_batch_")
  public class SCDFConfiguration {
  }
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
## 6주차
- 요약
  - [jpa](https://github.com/sajacaros/spring-batch-tutorial/blob/main/docs/06_JPA.md)
- 실습
  - [jpa 실습](https://github.com/sajacaros/spring-batch-jpa)
## 7주차
- 요약
  - [mybatis](https://github.com/sajacaros/spring-batch-tutorial/blob/main/docs/07_MyBatis.md)
- 실습
  - [mybatis 실습](https://github.com/sajacaros/spring-batch-mybatis)
## 8주차
- 요약
  - [composite](https://github.com/sajacaros/spring-batch-tutorial/blob/main/docs/08_Composite.md)
- 실습
  - [Composite 실습](https://github.com/sajacaros/spring-batch-jpa)
## 9주차
- 요약
  - [CustomItemReader/Writer](https://github.com/sajacaros/spring-batch-tutorial/blob/main/docs/09_Custom.md)
- 실습
  - [CustomItemReader/Writer 실습](https://github.com/sajacaros/spring-batch-jpa)
## 10주차
- 요약
  - [FlowControll](https://github.com/sajacaros/spring-batch-tutorial/blob/main/docs/10_Flow.md)
  