# 🧑‍🚀 우주인 (우리는 주식인)
## 1. 목표와 기능

### 1.1 목표
- AI 기반으로 최신 주식 트렌드와 관련 종목 및 뉴스를 제공
- 개인 포트폴리오에 맞춘 추천 종목을 확인할 수 있는 종합 주식 플랫폼

### 1.2 기능
- AI의 오늘의 키워드 추천
- 오늘의 키워드를 기반으로 AI 추천 종목과 최신 뉴스 제공
- 주식 포트폴리오 등록 후 차트로 한눈에 확인
- 주식 포트폴리오 기반 AI의 추천 종목 제공


## 2. 개발 환경 및 배포 URL
### 2.1 개발 환경
- Web Framework: ```Spring Boot```
- DB: ```AWS RDS (MySQL)```
- Backend: ```Spring Boot REST API```
- Server: ```AWS EC2```
- Frontend: ```React```
- Deploy Environment: ```Github Actions```, ```Amazon S3```, ```AWS codeDeploy```
- IDE: ```IntelliJ```, ```VsCode```

### 2.2 시스템 구조도
![Frame 3](https://github.com/user-attachments/assets/c60e182c-dd4b-4fca-9751-cc6ad88f8363)

### 2.3 배포 URL


### 2.3 URL 구조
- main, login

| App       | URL      | Views Function | Note     |
|-----------|----------|----------------|----------|
| main      | '/'      | Main           | 홈 화면    |
| login     | '/login' | Login          | 로그인 화면 |


- keyword, portfolio

| App        | URL                   | Views Function      | Note                 |
|------------|-----------------------|---------------------|----------------------|
| keyword    | 'keyword/'            | KeywordPage         | 키워드 화면             |
| keyword    | 'keyword/{keywordId}' | KeywordDetails      | 키워드상세 화면          |
| portfolio  | 'portfolio/'          | Portfolio           | 포트폴리오 화면          |
| portfolio  | 'portfolio/recommend' | PortfolioRecommend  | 포트폴리오 추천 종목 화면  |

## 3. 프로젝트 관리와 개발 일정
### 3.1 팀 구성
- 팀장: 이슬기
- 팀원: 박주형, 최지윤

### 3.2 개발 기간
- 개발 기간: 2024년 11월 22일 ~ 2024년 12월 19일
- 최종 수정 및 문서화: 2024년 12월 19일

### 3.3 WBS
- [Github Project](https://github.com/orgs/ESTsoft-Backend-6th-Astronaut/projects/2)


## 4. 요구사항 명세와 기능 명세
### 4.1 요구사항 명세
![요구사항](https://github.com/user-attachments/assets/90ced9ff-b676-4bf3-b9d8-f47127e67b6b)

## 5. 프로젝트
### 5.1 프로젝트 구조
<details>
<summary>접기/펼치기</summary>
  📦astronaut-be   
  ├── README.md  
  ├── appspec.yml  
  ├── build.gradle  
  ├── gradle  
  ├── gradlew  
  ├── gradlew.bat  
  ├── pull_request_template.md  
  ├── scripts  
  │   └── deploy.sh  
  ├── settings.gradle  
  ├── src  
  │   ├── main  
  │   │   ├── java  
  │   │   │   └── com  
  │   │   │       └── estsoft  
  │   │   │           └── astronautbe  
  │   │   │               ├── AstronautBeApplication.java  
  │   │   │               ├── config  
  │   │   │               │   ├── ApiConstants.java  
  │   │   │               │   ├── JasyptConfigAES.java  
  │   │   │               │   ├── Scheduler.java  
  │   │   │               │   ├── SecurityConfig.java  
  │   │   │               │   └── WebConfig.java  
  │   │   │               ├── controller  
  │   │   │               │   ├── AuthController.java  
  │   │   │               │   ├── keyword  
  │   │   │               │   │   ├── GetKeywordController.java  
  │   │   │               │   │   ├── KeywordController.java  
  │   │   │               │   │   └── PopularKeywordController.java  
  │   │   │               │   ├── keywordNews  
  │   │   │               │   │   ├── AllenAIController.java  
  │   │   │               │   │   ├── KeywordNewsController.java  
  │   │   │               │   │   └── NewsApiController.java  
  │   │   │               │   ├── loading  
  │   │   │               │   │   └── LoadingPageController.java  
  │   │   │               │   ├── portfolio  
  │   │   │               │   │   └── PortfolioController.java  
  │   │   │               │   └── stock  
  │   │   │               │         ├── KrxApiController.java  
  │   │   │               │         └── StockController.java  
  │   │   │               ├── domain  
  │   │   │               │   ├── FamousQuote.java  
  │   │   │               │   ├── Keyword.java  
  │   │   │               │   ├── KeywordNews.java  
  │   │   │               │   ├── Portfolio.java  
  │   │   │               │   ├── RecommendKeywordStock.java  
  │   │   │               │   ├── RecommendPortfolioStock.java  
  │   │   │               │   ├── SearchVolume.java  
  │   │   │               │   ├── Stock.java  
  │   │   │               │   ├── Token.java  
  │   │   │               │   ├── Users.java  
  │   │   │               │   └── dto  
  │   │   │               │      ├── RecommendKeywordStockDTO.java  
  │   │   │               │      ├── RecommendKeywordStockRequestDTO.java  
  │   │   │               │      ├── RecommendKeywordStockResponseDTO.java  
  │   │   │               │      ├── RecommendStockAnswer.java  
  │   │   │               │      ├── SearchVolumeRequestDTO.java  
  │   │   │               │      ├── SearchVolumeResponseDTO.java  
  │   │   │               │      ├── SearchVolumeWithStockDTO.java  
  │   │   │               │      ├── keywordNews  
  │   │   │               │      │   └── KeywordNewsResponseDTO.java  
  │   │   │               │      ├── portfolio  
  │   │   │               │      │   ├── PortfolioPriceResponseDTO.java  
  │   │   │               │      │   ├── PortfolioRequestDto.java  
  │   │   │               │      │   ├── PortfolioResponseDto.java  
  │   │   │               │      │   └── PortfolioStockResponseDTO.java  
  │   │   │               │      └── stock  
  │   │   │               │          ├── StockDetailResponseDTO.java  
  │   │   │               │          └── StockResponseDTO.java  
  │   │   │               ├── repository  
  │   │   │               │   ├── KeywordNewsRepository.java  
  │   │   │               │   ├── KeywordRepository.java  
  │   │   │               │   ├── PortfolioRepository.java  
  │   │   │               │   ├── QuoteRepository.java  
  │   │   │               │   ├── RecommendKeywordStockRepository.java  
  │   │   │               │   ├── RecommendPortfolioStockRepository.java  
  │   │   │               │   ├── SearchVolumeRepository.java  
  │   │   │               │   ├── StockRepository.java  
  │   │   │               │   └── UsersRepository.java  
  │   │   │               ├── service  
  │   │   │               │   ├── GetKeywordService.java  
  │   │   │               │   ├── JwtService.java  
  │   │   │               │   ├── KakaoService.java  
  │   │   │               │   ├── KeywordService.java  
  │   │   │               │   ├── KrxApiService.java  
  │   │   │               │   ├── PopularKeywordService.java  
  │   │   │               │   ├── PortfolioService.java  
  │   │   │               │   ├── QuoteService.java  
  │   │   │               │   ├── StockService.java  
  │   │   │               │   └── keywordNews  
  │   │   │               │        ├── AllenAIService.java  
  │   │   │               │        ├── KeywordNewsService.java  
  │   │   │               │        └── NaverNewsApiService.java  
  │   │   │               └── util  
  │   │   │                      └── JsonParserUtil.java  
  │   │   └── resources  
  │   │        └── application.properties  
  │   └── test  
  │     └── java  
  │       └── com  
  │         └── estsoft  
  │           └── astronautbe  
  │            └── AstronautBeApplicationTests.java

</details>

### 5.2 데이터베이스 구조
- [ErdCloud](https://www.erdcloud.com/d/cMAwgbM6iTbCD663C)

### ERD
![image](https://github.com/user-attachments/assets/ffbce0ce-2d4d-4b84-acec-0218cef7560e)

## 6. 와이어프레임

### 6.1 와이어프레임
| 화면 이름          | 화면 이미지                                                                                                  | 설명                                                                                                                                                |
|----------------|---------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| 메인 화면          | <img src="https://github.com/user-attachments/assets/a1deba33-1a1f-4feb-86f6-46d9d3e23bbe" width="50%"> | - AI 이슈 포착: AI가 추천하는 최신 주식 트렌드 키워드 버블 차트 <br> - 키워드 랭킹: AI 추천 키워드의 랭킹 <br> - 나의 주식 포트폴리오: 보유 주식 포트폴리오를 파이차트로 확인 <br> - 실시간 주가 정보: 보유 주식의 최신 주가 정보 |
| 로그인 화면         | <img src="https://github.com/user-attachments/assets/e3b1496c-756b-42de-9d8c-1b0c5e2c9056" width="50%"> | - 소셜 로그인: 카카오 로그인                                                                                                                                 |
| 키워드 상세 화면      | <img src="https://github.com/user-attachments/assets/d4d6e4c1-5b76-4d2b-aced-a11800b000e6" width="50%"> | - 키워드 분석: 키워드에 대한 AI의 분석 결과 <br> - 키워드 뉴스: 키워드 관련 최신 뉴스 <br> - 종목 검색량: 키워드 관련 AI의 추천 종목의 최근 검색량 그래프 <br> - AI 추천 종목: 키워드 관련 AI의 추천 종목             |
| 포트폴리오 화면       | <img src="https://github.com/user-attachments/assets/44cafcd2-5bce-409c-948a-bffa8923f07f" width="50%"> | - 포트폴리오 차트: 보유한 주식 포트폴리오를 파이차트로 확인 <br> - 포트폴리오 테이블: 주식 포트폴리오 정보를 등록, 수정 및 삭제                                                                     |
| 포트폴리오 추천 종목 화면 | <img src="https://github.com/user-attachments/assets/def343bd-5225-4e26-8fec-c02b0f9ff9b2" width="50%"> | - 포트폴리오 기반 추천 종목: 포트폴리오를 기반으로 AI의 추천 종목                                                                                                           |
| 로딩 화면          | <img src="https://github.com/user-attachments/assets/c0761cfe-b45d-44ef-8722-e0b614e36e79" width="50%"> | - 랜덤 명언: 주식 관련 명언을 랜덤으로 보여줌                                                                                                                       |

## 7. 에러 및 트러블슈팅 히스토리 

## 8. 참고 자료
### 8.1 참고 기능 및 디자인
- [씽크풀](https://m.thinkpool.com/advisor)

### 8.2 사용한 외부 API
- [KRX 유가증권 일별매매정보](http://openapi.krx.co.kr/contents/OPP/USES/service/OPPUSES002_S2.cmd?BO_ID=JvJFzlAENzZlPBDNGAWC)
- [코스닥 일별매매정보](http://openapi.krx.co.kr/contents/OPP/USES/service/OPPUSES002_S2.cmd?BO_ID=hZjGpkllgCBCWqeTsYFj)
- [앨런 AI](https://kdt-api-function.azurewebsites.net/docs#/)
- [네이버 뉴스](https://developers.naver.com/docs/serviceapi/search/news/news.md#%EB%89%B4%EC%8A%A4)
- [네이버 검색어 트렌드](https://developers.naver.com/docs/serviceapi/datalab/search/search.md#%ED%86%B5%ED%95%A9-%EA%B2%80%EC%83%89%EC%96%B4-%ED%8A%B8%EB%A0%8C%EB%93%9C)