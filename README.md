# spring-batch-tutorial
## 1주차
- [설치 및 스키마 설명](docs/01_introduction.md)
## 2주차
- [tasklet](docs/02_tasklet.md)
- 추가 구현 내용
  - api 호출할때마다 tasklet 실행
    ``` 
    ### Run tasklet
    GET localhost:8010/run
    ```
## 3주차
- [chunk](docs/03_chunk.md)
- 추가 구현 내용
  - api 호출할때마다 입력한 정보로 db의 metadata를 excel에 저장
  - DB는 PostgreSQL만 구현함
    ```
    ### fetch metadata => save to excel
    POST localhost:8010/metadata
    Accept: application/json
    Content-Type: application/json
    
    {
        "url": "jdbc:postgresql://{ip}:{port}/{db name}",
        "username": "{username}",
        "password": "{password}",
        "filePath": "{filePath}"
    }
    ```
