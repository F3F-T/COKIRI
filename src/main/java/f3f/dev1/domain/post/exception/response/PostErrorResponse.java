package f3f.dev1.domain.post.exception.response;

import f3f.dev1.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PostErrorResponse {
    public static final ResponseEntity<ErrorResponse> NOT_MATCHING_AUTHOR = new ResponseEntity<>(
                    ErrorResponse.builder().status(HttpStatus.NOT_FOUND).message("요청자와 로그인한 사용자가 일치하지 않습니다.").build(),
                    HttpStatus.NOT_FOUND);

    public static final ResponseEntity<ErrorResponse> NOT_FOUND_PRODUCT_CATEGORY_NAME = new ResponseEntity<>(
            ErrorResponse.builder().status(HttpStatus.NOT_FOUND).message("지정한 이름의 상품 카테고리가 존재하지 않습니다.").build(),
            HttpStatus.NOT_FOUND);

    public static final ResponseEntity<ErrorResponse> NOT_FOUND_WISH_CATEGORY_NAME = new ResponseEntity<>(
            ErrorResponse.builder().status(HttpStatus.NOT_FOUND).message("지정한 이름의 희망 카테고리가 존재하지 않습니다.").build(),
            HttpStatus.NOT_FOUND);

    public static final ResponseEntity<ErrorResponse> NOT_FOUND_POST_LIST_BY_AUTHOR = new ResponseEntity<>(
            ErrorResponse.builder().status(HttpStatus.NOT_FOUND).message("해당 사용자가 작성한 게시글이 존재하지 않습니다.").build(),
            HttpStatus.NOT_FOUND);

    public static final ResponseEntity<ErrorResponse> NOT_CONTAIN_AUTHOR_INFO = new ResponseEntity<>(
            ErrorResponse.builder().status(HttpStatus.NOT_FOUND).message("게시글 수정에는 사용자 정보가 반드시 포함되어야 합니다.").build(),
            HttpStatus.NOT_FOUND);
}
