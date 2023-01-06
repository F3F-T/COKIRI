package f3f.dev1.domain.message.exception;

public class CanNotDeleteMessage extends IllegalArgumentException{
    public CanNotDeleteMessage(){super("본인만 메시지를 삭제할 수 있습니다.");}
}
