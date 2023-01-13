package f3f.dev1.domain.message.exception;

import f3f.dev1.domain.message.model.Message;

public class MessageException extends IllegalArgumentException{
    public MessageException (String m){ super(m); }
}
