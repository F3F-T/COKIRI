package f3f.dev1.domain.message.exception;

public class NoMessageRoomException extends IllegalArgumentException{
    public NoMessageRoomException(){super("존재하지 않은 채팅방입니다.");}
}
