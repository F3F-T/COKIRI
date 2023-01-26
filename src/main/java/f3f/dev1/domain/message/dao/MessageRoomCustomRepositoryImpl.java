package f3f.dev1.domain.message.dao;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import f3f.dev1.domain.member.dto.MemberDTO.GetUserMessageRoom;
import f3f.dev1.domain.member.dto.QMemberDTO_GetUserMessageRoom;
import f3f.dev1.domain.member.model.QMember;
import f3f.dev1.domain.message.model.QMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static f3f.dev1.domain.message.model.QMessage.message;
import static f3f.dev1.domain.message.model.QMessageRoom.messageRoom;
import static f3f.dev1.domain.post.model.QPost.post;

@RequiredArgsConstructor
@Slf4j
public class MessageRoomCustomRepositoryImpl implements MessageRoomCustomRepository {

    private final JPAQueryFactory queryFactory;
    private static final QMember buyer = new QMember("buyer");
    private static final QMember seller = new QMember("seller");
    private static final QMember author = new QMember("author");

    @Override
    public Page<GetUserMessageRoom> findUserMessageRoom(Long userId, Pageable pageable) {
        QMessage newMsg = new QMessage("newMsg");

        QueryResults<GetUserMessageRoom> getUserMessageRoomQueryResults = queryFactory.select(
                        new QMemberDTO_GetUserMessageRoom(
                                messageRoom.id,
                                author.id,
                                message.content,
                                message.createDate,
                                buyer.nickname,
                                seller.nickname,
                                buyer.imageUrl,
                                seller.imageUrl
                        )
                )
                .from(messageRoom)
                .join(messageRoom.messages, message).on(message.messageRoom.id.eq(messageRoom.id))
                .join(messageRoom.buyer, buyer).on(messageRoom.buyer.id.eq(buyer.id))
                .join(messageRoom.seller, seller).on(messageRoom.seller.id.eq(seller.id))
                .join(messageRoom.post, post).on(messageRoom.post.id.eq(post.id))
                .join(post.author, author).on(post.author.id.eq(author.id))
                .where(messageRoom.seller.id.eq(userId)
                        .or(messageRoom.buyer.id.eq(userId))
                        .and(message.createDate.eq(
                                JPAExpressions
                                        .select(newMsg.createDate.max())
                                        .from(newMsg)
                                        .where(newMsg.messageRoom.id.eq(messageRoom.id))
                                        .groupBy(newMsg.messageRoom.id)
                        )))
                .groupBy(messageRoom.id, message.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(getUserMessageRoomQueryResults.getResults(), pageable, getUserMessageRoomQueryResults.getTotal());
    }


}
