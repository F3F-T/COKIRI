package f3f.dev1.domain.message.exception;

public class CanNotSendMessageByTradeStatus extends IllegalArgumentException{
    public CanNotSendMessageByTradeStatus(){ super("메시지를 보낼 수 없습니다."); }
}
