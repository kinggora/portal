# 다중 게시판 사이트
Spring Boot API + Vue.Js SPA

## 개발 환경
`Java 11` `JavaScript`
- **IDE**: IntelliJ IDEA 2022.3.2 (Ultimate)
- **Framework**: SpringBoot(2.x), Vue.js 3
- **Database**: MariaDB
- **SQL Mapper**: MyBatis
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
- 로그인/회원가입, 회원정보 수정, 작성 게시글/댓글 관리, 회원 탈퇴

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
    ├─static : vue.js build result
    └─templates
```

## 상세 기능
### 로그인/회원가입
<table>
    <tr>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/715ade92-479a-47e8-a988-bc697efa6d43" /></td>
        <td><img width="1858" height="887" alt="Image" src="https://github.com/user-attachments/assets/53acf043-0cc2-4d99-9b52-9bdbba360754" /></td>
    </tr>
    <tr>
        <td>회원 등록 폼</td>
        <td>회원 등록 - 실패(비밀번호, 닉네임 형식 오류)</td>
    </tr>
    <tr>
        <td><img width="1858" height="886" alt="Image" src="https://github.com/user-attachments/assets/0978d183-8091-46f6-a598-9e3a37abc746" /></td>
        <td><img width="1857" height="885" alt="Image" src="https://github.com/user-attachments/assets/506b4fa1-cd00-416c-bef5-3ada8e852fab" /></td>
    </tr>
    <tr>
        <td>로그인 폼</td>
        <td>로그인 - 실패(비밀번호 미입력)</td>
    </tr>
    <tr>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/768af223-0090-42ac-9d22-4e2749308b3b" /></td>
        <td><img width="1857" height="886" alt="Image" src="https://github.com/user-attachments/assets/563562cb-5316-4094-8929-69f9fe11e3cb" /></td>
    </tr>
    <tr>
        <td>로그인 - 실패(잘못된 아이디 또는 비밀번호)</td>
        <td>로그인 - 실패(탈퇴한 회원)</td>
    </tr>
</table>

### 마이페이지
- 작성 글/댓글 관리
- 회원 정보 수정
- 회원 탈퇴
<table>
    <tr>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/39760981-0286-48dd-8769-6e18a5900364" /></td>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/02d8838e-72a9-4771-8b45-3f362b09cdd2" /></td>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/85082916-7802-42c9-842f-405490f7f779" /></td>
    </tr>
    <tr>
        <td>작성 글 목록</td>
        <td>작성 댓글 목록</td>
        <td>작성 댓글 삭제</td>
    </tr>
    <tr>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/1959e175-3a1c-4b96-bbf2-e65e696e2e86" /></td>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/07e42b76-c6da-4d7e-ba6e-eedd7e204787" /></td>
        <td></td>
    </tr>
    <tr>
        <td>회원 정보 수정 - 닉네임 변경</td>
        <td>회원 정보 수정 - 비밀번호 변경</td>
        <td></td>
    </tr>
</table>

### 게시글 목록
- BoardType에 따라 목록 형태 상이
    - LIST: 이미지, 첨부파일 유무, 댓글 수 표시
    - IMAGE: 이미지 썸네일, 댓글 수 표시
    - QUESTION: 비밀글 여부, 답글 존재 여부 표시
- 검색 조건: 등록일, 카테고리, 검색어
- 제목 길이가 일정 바이트를 넘으면 단축하고 접미사(…)를 붙임
<table>
    <tr>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/f956b3a0-76a9-4a3c-a202-29713d82ed8c" /></td>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/8254fa23-3f42-4f49-bf1b-f909dbd490f7" /></td>
    </tr>
    <tr>
        <td>게시글 목록 (BoardType: LIST)</td>
        <td>게시글 목록 (BoardType: IMAGE)</td>
    </tr>
    <tr>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/a0ebc3d2-5088-491d-99e9-5efc2dd6e1b2" /></td>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/8e81d20f-4b32-4041-b914-e3810097da65" /></td>
    </tr>
    <tr>
        <td>게시글 목록 (BoardType: QUESTION)</td>
        <td>게시글 목록 - 검색 결과</td>
    </tr>
</table>

  
