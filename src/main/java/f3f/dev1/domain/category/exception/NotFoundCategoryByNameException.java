package f3f.dev1.domain.category.exception;

public class NotFoundCategoryByNameException extends IllegalArgumentException {
    public NotFoundCategoryByNameException(){super ("해당 이름의 카테고리를 찾을 수 없습니다.");}
}
