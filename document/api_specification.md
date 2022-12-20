# API Specification

> API 명세서 작성 예시      
> 
> ex) 게시판 조회 - GET
> 
> `/board?post_id=1`  
> `/board/{post_id}`
> 
> ex) 게시글 등록 - POST
> 
> `/board`      
> HTTP Message Body
> ```
> {"title":"명절 선물로 받은 샴푸입니다", "content":"쿨거래 환영합니다"}
> ```
> 
> ex) 게시글 수정 - PUT, PATCH
>
> `/board?post_id=1`  
> `/board/{post_id}`   
> HTTP Message Body
> ```
> {"content":"거래가 잘 안되서 라면 한봉지 얹어드릴게요"}
> ```

## User

| Description | HTTP Method | URL | Query string | Message body                                | Return data                                                                                                                                                                                                  |
| --- | --- | --- | --- |---------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 회원가입 | POST | /user/signup |  | {"userName":"김동준","nickname":"nickname","address":{"addressName":"정자동","postalAddress":"13556","latitude":"37.49455","longitude":"127.12170"},"birthDate":"990128","phoneNumber":"01012345678","email":"test@email.com","password":"12345678"} | CREATE                                                                                                                                                                                                       |
| 로그인 | POST | /user/login |  | {"email":"test@email.com", "password":"12345678"}                     | OK                                                                                                                                                                                                           |
| 로그아웃 | DELETE | /user/logout |  |                                            | OK                                                                                                                                                                                                           |
| 회원 정보 조회 | GET | /user |  |                                             | {"userName": "김동준","nickname": "nickname","address": {"addressName": "정자동","postalAddress": "13556","latitude": "37.49455","longitude": "127.12170"},"phoneNumber": "01012345678","email": "test@email.com"} |
| 회원 정보 수정 | PATCH | /user |  | {"nickname":"updatenickname","address":{"addressName":"바뀐 주소","postalAddress":"999","latitude":"37.49455","longitude":"127.12170"}}                   | UPDATE                                                                                                                                                                                         |
| 회원 삭제 | DELETE | /user |  |                                             | DELETE                                                                                                                                                                                            |
| 아이디 찾기 | POST | /user/find/email |  | {value from external api:~~}                | {email:~~~}                                                                                                                                                                                                  |
| 비밀번호 찾기 | POST | /user/find/password |  | {email:~~~,username: ~~~,phone: ~~}         | {password:~~}                                                                                                                                                                                                |
| 비밀번호 변경 | PATCH | /user/password |  | {"oldPassword":"12345678","newPassword":"56781234"} | UPDATE                                                                                                                                                                                              |

## Post

| Description | HTTP Method | URL | Query string | Message Body                         | Return data               |
| --- | --- | --- | --- |--------------------------------------|---------------------------|
| 게시글 전체 조회 | GET | /post | category, tag |                                      | {posts:[~~]}              |
| 게시글 작성 | POST | /post |  | {title:~~,content: ~~,token: ~~~, …} | {title:~~, content: ~~,…} |
| 게시글 정보 조회 | GET | /post/{post_id} |  |                                      | {title:~~,content: ~~, …} |
| 게시글 정보 조회 - 작성자로 | GET | /post/{memberId} |  |                                      | {title:~~,content: ~~, …} |
| 게시글 정보 수정 | PATCH | /post |  | {content:~~, token: ~~, …}           | {title:~~, content: ~~,…}    |
| 게시글 삭제 | DELETE | /post/{post_id} |  | {token:~~,… }                        | {removed_post:~~}         |

## Comment

| Description | HTTP Method | URL | Query string | Message body | Return data |
| --- | --- | --- | --- | --- | --- |
| 게시글 댓글 조회 | GET | /post/{post_id}/comments |  |  | { comments:[~~]} |
| 게시글 댓글 작성 | POST | /post/{post_id}/comments |  | {content:~~, token: ~~~, …} | { content: ~~} |
| 게시글 댓글 수정 | PATCH | /post/{post_id}/comments/{comment_id} |  | {content:~~, token: ~~~, …} | { content: ~~} |
| 게시글 댓글 삭제 | DELETE | /post/{post_id}/comments/{comment_id} |  | {token: ~~~, …} | {removed_comment:~~} |

## Message

| Description | HTTP Method | URL | Query string | Message body                                  | Return data |
| --- | --- | --- | --- |-----------------------------------------------| --- |
| 채팅방 전체 조회 | GET | /user/{user_id}/messageRooms | token |                                               | { messageRooms:[~~]} |
| 채팅방 쪽지 전체 조회 | GET | /user/{user_id}/messageRooms/{messageRoom_id} | post_id, token |                                               | {messages:[~~]} |
| 메시지 전송 | POST | /user/{user_id}/messageRooms/{messageRoom_id} |  | {sender_id:~~,receiver_id: ~~, token: ~~~, …} | { message:~~} |

## Trade

| Description | HTTP Method | URL | Query string | Message body | Return data |
| --- | --- | --- | --- | --- | --- |
| 게시물 거래 상태 조회 | GET | /post/{post_id}/trade |  |  | {trade:[~~]} |
| 거래 상태 수정 | PATCH | /post/{post_id}/trade |  | {tradeStatus:~~} | {tradeStatus:~~} |

## Category

| Description | HTTP Method | URL | Query string | Message body | Return data |
| --- | --- | --- | --- | --- | --- |
| 전체 카테고리 조회 | GET | /category |  |  | {categories:[~~]} |

## Scrap

| Description | HTTP Method | URL | Query string | Message body                 | Return data |
| --- | --- | --- | --- |------------------------------| --- |
| 스크랩 조회 | GET | /user/{user_id}/scrap | token |                              | {posts:[~~]} |
| 관심 포스트 등록 | POST | /user/{user_id}/scrap |  | {post_id:~~~, token: ~~~, …} | {post_id:~~} |
| 관심 포스트 삭제 | DELETE | /user/{user_id}/scrap |  | {post_id:~~, token: ~~, ..}  | {post_id:~~} |

