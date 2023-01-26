package f3f.dev1.domain.post.exception.handler;

import f3f.dev1.domain.category.exception.NotFoundProductCategoryNameException;
import f3f.dev1.domain.category.exception.NotFoundWishCategoryNameException;
import f3f.dev1.domain.post.exception.NotContainAuthorInfoException;
import f3f.dev1.domain.post.exception.NotFoundPostListByAuthorException;
import f3f.dev1.domain.post.exception.NotMatchingAuthorException;
import f3f.dev1.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static f3f.dev1.domain.post.exception.response.PostErrorResponse.*;

@Slf4j
@RestControllerAdvice
public class PostExceptionHandler {
    // 게시글 수정과 삭제에서 발생하는 오류
    /*
        TODO
         댓글도 여기에 영향을 받는다. 수정해야 할듯.
         그리고 잘못된 예외임. return 값 바꿔줘야 할듯.
     */
    @ExceptionHandler(NotMatchingAuthorException.class)
    protected final ResponseEntity<ErrorResponse> handleNotMatchingAuthorException(
            NotMatchingAuthorException notMatchingAuthorException, WebRequest request) {
        log.debug("Not matching author :: {}, detection time = {}", request.getDescription(false));
        return NOT_MATCHING_AUTHOR;
    }

    // 게시글 생성과 수정에서 발생하는 오류
    @ExceptionHandler(NotFoundProductCategoryNameException.class)
    protected final ResponseEntity<ErrorResponse> handleNotFoundProductCategoryNameException(
            NotFoundProductCategoryNameException notFoundProductCategoryNameException, WebRequest webRequest) {
        log.debug("Not found product category name :: {}, detection time = {}", webRequest.getDescription(false));
        return NOT_FOUND_PRODUCT_CATEGORY_NAME;
    }

    // 게시글 생성과 수정에서 발생하는 오류
    @ExceptionHandler(NotFoundWishCategoryNameException.class)
    protected final ResponseEntity<ErrorResponse> handleNotFoundWishCategoryNameException(
            NotFoundProductCategoryNameException notFoundWishCategoryNameException, WebRequest webRequest) {
        log.debug("Not found wish category name :: {}, detection time = {}", webRequest.getDescription(false));
        return NOT_FOUND_WISH_CATEGORY_NAME;
    }

    // 고민되는 예외. 생각해보면 유저가 작성한 게시글이 없으면 그냥 빈 리스트를 반환해주면 되는거 아닌가??
    // 일단 현재 작성된 컨트롤러에서는 이 예외가 터질 일은 없다.
    @ExceptionHandler(NotFoundPostListByAuthorException.class)
    protected final ResponseEntity<ErrorResponse> handleNotFoundPostListByAuthorException(
            NotFoundPostListByAuthorException ex, WebRequest webRequest) {
        log.debug("해당 유저가 작성한 게시글이 없음", webRequest.getDescription(false));
        return NOT_FOUND_POST_LIST_BY_AUTHOR;
    }

    @ExceptionHandler(NotContainAuthorInfoException.class)
    protected final ResponseEntity<ErrorResponse> handleNotContainAuthorInfoException(
            NotContainAuthorInfoException ex, WebRequest webRequest) {
        log.debug("게시글 수정에 사용자 정보가 존재하지 않음", webRequest.getDescription(false));
        return NOT_CONTAIN_AUTHOR_INFO;
    }


}
