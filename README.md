# 다중 게시판 사이트
Spring Boot API + Vue.Js SPA

## 개발 환경
`Java 11` `JavaScript`
- **IDE**: IntelliJ IDEA 2022.3.2 (Ultimate)
- **Framework**: SpringBoot(2.x), Vue.js 3
- **Database**: MariaDB
- **ORM**: MyBatis
- **Storage**: Amazon S3

## 주요 기능
### Auth
- Spring Security 필터 인증/인가, 예외 처리
- 로그인 시 JWT 발급하여 로그인 유지
- 게시판마다 다른 권한 인가 처리 (LIST, READ, WRITE, REPLY, COMMENT 등)

||LIST|READ|WRITE|REPLY-WRITE|REPLY-READ|...|
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
|공지|ALL|ALL|ADMIN|NONE|NONE||
|뉴스|ALL|USER|ADMIN|NONE|NONE||
|자료실|ALL|USER|ADMIN|NONE|NONE||
|자유게시판|ALL|USER|USER|NONE|NONE||
|갤러리|ALL|USER|USER|NONE|NONE||
|QnA|ALL|USER|USER|ADMIN|USER||
### Boards
- 게시판 형태: 목록형, 갤러리형, Q&A형
- 검색: 게시 일자(from, to), 카테고리, 검색어
- 대댓글: max depth 5
### Account
- 로그인/회원가입, 회원정보 수정, 작성 글 목록

## 프로젝트 구조
```bash
├─java
│  └─kinggora
│      └─portal
│          ├─config    
│          ├─domain 
│          │  └─type
│          │      └─typehandler            
│          ├─exception   
│          ├─mapper  
│          ├─model
│          │  ├─data
│          │  │  ├─request     
│          │  │  └─response      
│          │  └─error      
│          ├─repository  
│          ├─security
│          │  ├─auth
│          │  ├─exception
│          │  │  └─handler   
│          │  └─user      
│          ├─service   
│          ├─util  
│          └─web
│              ├─controller
│              ├─converter
│              ├─evaluator
│              └─validation
│                  └─validator                
└─resources
    ├─kinggora
    │  └─portal
    │      └─mapper          
    ├─messages 
    ├─static : front build result
    └─templates
```
## Preview
|메인 페이지|목록형 게시판|
|:---:|:---:|
|<img src='https://github.com/kinggora/portal/assets/61868949/93d48958-9c85-40b3-8f6c-60968b830e1f'>|<img src='https://github.com/kinggora/portal/assets/61868949/7703d29e-a4cd-4556-903a-cc350f6cc0b0'>|

|갤러리형 게시판|Q&A형 게시판|
|:---:|:---:|
|<img src='https://github.com/kinggora/portal/assets/61868949/d484fb86-f6dd-4b1e-a781-cc010ebb9fa5'>|<img src='https://github.com/kinggora/portal/assets/61868949/da86d3d8-9ef1-49c2-a9fb-8cd6e5408723'>|




