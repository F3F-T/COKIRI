package f3f.dev1.domain.category.exception;

public class CanNotDeleteCategoryException extends IllegalArgumentException{
    public CanNotDeleteCategoryException(){super("카테고리를 지울 수 없습니다. : 하위카테고리 존재");}
}
