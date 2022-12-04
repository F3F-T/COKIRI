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