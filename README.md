# Portal
**Spring Boot Rest API + Vue.Js SPA**

게시판 별 접근 권한을 동적으로 적용하는 다중 게시판 입니다.

<img width="1000" alt="Image" src="https://github.com/kinggora/portal/assets/61868949/93d48958-9c85-40b3-8f6c-60968b830e1f" />

## 개발 환경
- **Backend**: Java 11, SpringBoot(2.x), Spring Security 6
- **Frontend**: JavaScript, HTML5, CSS3, Vue.js 3, Vuetify
- **Test**: Postman, JUnit5
- **Database**: MariaDB, MyBatis
- **Storage**: Amazon S3
- **Library**: JJWT, Tika 

## 주요 기능
### Auth
- Spring Security 필터 인증/인가, 예외 처리
- 로그인 시 JWT 발급하여 인증
- 게시판별 권한 정책에 따라 인가 처리

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
- 파일 유효성 검증, 업로드/다운로드 권한 인가

### Account
- 로그인/회원가입, 회원정보 수정, 작성 게시글/댓글 관리, 회원 탈퇴

## ERD
<img width="1730" height="812" alt="Image" src="https://github.com/user-attachments/assets/6df301bb-47ff-4139-b9d4-11f97fde990d" />

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

### 게시글 작성/수정
- 비밀글 허용 게시판의 경우, 비밀글 여부 표시
- 사진, 파일 첨부 가능
<table>
    <tr>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/48c12c2d-8a9c-47d7-8db0-971c0fd53e90" /></td>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/41146ea2-b59b-4fa6-8e10-c6ce5c1f28c5" /></td>
    </tr>
    <tr>
        <td>자유게시판 게시글 작성</td>
        <td>Q&A 게시판 게시글 작성(비밀글)</td>
    </tr>
</table>

### 게시글 상세
- 목록 버튼 클릭 시 이전에 접근했던 목록 페이지로 이동
- 로그인 이용자만 댓글 입력, 첨부파일 다운로드 가능
- 비밀글은 작성자, 관리자만 조회 가능
- 관리자 권한이 있는 경우, Q&A 게시글 답변 작성 버튼 표시
<table>
    <tr>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/d1614c42-1913-4a7f-943c-d6ba93172114" /></td>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/644cb180-7c33-4444-872b-6d7b57450463" /></td>
    </tr>
    <tr>
        <td>일반 게시글(로그인 상태 - 댓글 작성 가능)</td>
        <td>일반 게시글(미로그인 상태 - 댓글 작성 불가)</td>
    </tr>
    <tr>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/1c49c8fc-6427-4b37-9898-4ba9b00c3dec" /></td>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/f104e942-6f7d-4040-90d3-710fdfc1e7dd" /></td>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/3e7eeb4e-32f2-4990-a628-a80c6445cf03" /></td>
    </tr>
    <tr>
        <td>Q&A 게시글 - 미답변</td>
        <td>Q&A 게시글 - 답변 작성</td>
        <td>Q&A 게시글 - 답변 완료</td>
    </tr>
</table>

### 댓글 목록
- 계층형 댓글 구조
    - 등록: depth가 0인 댓글을 root 댓글이라고 할 때, root의 하위 댓글 그룹에 대해 다음과 같은 순서로 정렬
        - 자식 댓글의 depth는 부모의 depth + 1
        - 자식 댓글은 부모의 바로 하위에 위치
    - 삭제: 자식 댓글 유무에 따라 다르게 처리
        - 자식 댓글이 있는 경우: 숨김 처리 (“삭제된 댓글입니다.”)
        - 자식 댓글이 없는 경우: 조상 중 숨김 처리된 댓글이 있는지 재귀적으로 확인하고, 있다면 함께 삭제
<table>
    <tr>
        <td rowspan="5"><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/45613e1f-9246-4d9d-b966-6b815e38f914" /></td>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/ed641d7d-643b-45dd-80e9-b586bf0b7b05" /></td>
    </tr>
    <tr>
        <td>댓글 삭제1 - 하위 댓글 메뉴</td>
    </tr>
    <tr>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/2d1d5722-1d78-4118-961e-a3c872b09b05" /></td>
    </tr>
    <tr>
        <td>댓글 삭제2 - 하위 댓글 삭제 실행</td>
    </tr>
    <tr>
        <td><img width="1857" height="887" alt="Image" src="https://github.com/user-attachments/assets/e7d42070-d2ef-4efe-a9dd-7b24c7e4a999" /></td>
    </tr>
    <tr>
        <td>댓글 목록</td>
        <td>댓글 수정</td>
    </tr>
</table>
