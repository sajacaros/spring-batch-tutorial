# spring-batch-tutorial
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
