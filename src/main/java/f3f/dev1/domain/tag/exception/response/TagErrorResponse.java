package f3f.dev1.domain.tag.exception.response;

import f3f.dev1.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TagErrorResponse {
    public static final ResponseEntity<ErrorResponse> DUPLICATE_TAG = new ResponseEntity<>(
            ErrorResponse.builder().status(HttpStatus.NOT_FOUND).message("이미 존재하는 태그가 중복 추가되었습니다.").build(),
            HttpStatus.NOT_FOUND);

    public static final ResponseEntity<ErrorResponse> NOT_FOUND_BY_POST_AND_TAG = new ResponseEntity<>(
            ErrorResponse.builder().status(HttpStatus.NOT_FOUND).message("해당 게시글에 태그가 없습니다.").build(),
            HttpStatus.NOT_FOUND);

    public static final ResponseEntity<ErrorResponse> NOT_FOUND_BY_TAG_NAME = new ResponseEntity<>(
            ErrorResponse.builder().status(HttpStatus.NOT_FOUND).message("해당 태그명이 존재하지 않습니다.").build(),
            HttpStatus.NOT_FOUND);
}
