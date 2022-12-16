package f3f.dev1.domain.message.application;

import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.trade.dao.TradeRepository;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
메시지룸을 만들고 메시지 만들기

 */
@Service
@RequiredArgsConstructor
public class MessageRoomService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TradeRepository tradeRepository;




}
