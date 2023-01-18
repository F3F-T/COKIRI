package f3f.dev1.domain.member.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import f3f.dev1.domain.address.dto.AddressDTO.AddressInfoDTO;
import f3f.dev1.domain.address.dto.QAddressDTO_AddressInfoDTO;
import f3f.dev1.domain.member.dto.QMemberDTO_UserDetail;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static f3f.dev1.domain.address.model.QAddress.address;
import static f3f.dev1.domain.member.dto.MemberDTO.UserDetail;
import static f3f.dev1.domain.member.dto.MemberDTO.UserInfoWithAddress;
import static f3f.dev1.domain.member.model.QMember.member;
import static f3f.dev1.domain.scrap.model.QScrap.scrap;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public UserInfoWithAddress getUserInfo(Long userId) {

        return UserInfoWithAddress.builder().userDetail(getUserDetail(userId)).address(getUserAddress(userId)).build();
    }

    @Override
    public List<AddressInfoDTO> getUserAddress(Long userId) {
        return queryFactory.select(new QAddressDTO_AddressInfoDTO(address.id, address.addressName, address.postalAddress, address.latitude, address.longitude)).from(address)
                .join(address.member, member).on(address.member.id.eq(member.id))
                .where(member.id.eq(userId)).fetch();
    }

    @Override
    public UserDetail getUserDetail(Long userId) {
        return queryFactory.select(new QMemberDTO_UserDetail(member.id, scrap.id, member.userName, member.imageUrl, member.nickname, member.description, member.phoneNumber, member.email, member.birthDate, member.userLoginType)).from(member)
                .join(member.scrap, scrap).on(scrap.member.id.eq(member.id))
                .where(member.id.eq(userId)).fetchOne();
    }

//    @Override
//    public Page<GetUserPost> getUserPost(Long userId, Pageable pageable) {
//        QueryResults<GetUserPost> getUserPostQueryResults = queryFactory.select(new QMemberDTO_GetUserPost(post.id, post.title, trade.tradeStatus, category.name, scrapPost.scrap.count().nullif(0L)))
//                .from(post)
//                .leftJoin(post.scrapPosts, scrapPost).on(scrapPost.post.id.eq(post.id))
//                .join(post.trade, trade).on(trade.post.id.eq(post.id))
//                .join(post.wishCategory, category).on(post.wishCategory.id.eq(category.id))
//                .where(post.author.id.eq(userId))
//                .groupBy(post.id)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();
//
//        return new PageImpl<>(getUserPostQueryResults.getResults(), pageable, getUserPostQueryResults.getTotal());
//    }
}
