package f3f.dev1.domain.post.dao;

import com.querydsl.core.QueryFactory;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import f3f.dev1.domain.message.model.QMessageRoom;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.post.model.QScrapPost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.querydsl.core.util.StringUtils.*;
import static f3f.dev1.domain.message.model.QMessageRoom.*;
import static f3f.dev1.domain.post.dto.PostDTO.*;
import static f3f.dev1.domain.post.model.QPost.*;
import static f3f.dev1.domain.post.model.QScrapPost.*;
import static f3f.dev1.domain.tag.model.QPostTag.*;
import static f3f.dev1.domain.tag.model.QTag.*;

@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // DTO로 처리하기에는 내부적으로 2차 처리되는 필드가 너무 많다. 그냥 객체로 바로 받아오겠다.
    @Override
    public Page<Post> findPostsByCondition(SearchPostRequestExcludeTag request, Pageable pageable) {
            QueryResults<Post> results = jpaQueryFactory
                .selectFrom(post)
                .where(productCategoryNameFilter(request.getProductCategory()),
                        wishCategoryNameFilter(request.getWishCategory()),
                        priceFilter(request.getMinPrice(), request.getMaxPrice()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

            List<Post> responseList = results.getResults();
            long total = results.getTotal();
            return new PageImpl<>(responseList, pageable, total);
    }

    @Override
    public Page<PostInfoDtoForGET_PreProcessor> findPostDTOByConditions(SearchPostRequestExcludeTag requestExcludeTag, Pageable pageable) {
        QueryResults<PostInfoDtoForGET_PreProcessor> results = jpaQueryFactory
                .select(Projections.constructor(PostInfoDtoForGET_PreProcessor.class,
                        post.id,
                        post.title,
                        post.content,
                        post.author.nickname,
                        post.messageRooms,
                        post.scrapPosts
                        ))
                .from(post)
                .leftJoin(post.messageRooms, messageRoom)
                .leftJoin(post.scrapPosts, scrapPost)
//                .where(messageRoom.post.id.eq(post.id))
//                .where(scrapPost.post.id.eq(post.id))
                .fetchJoin()
//                .groupBy(post.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<PostInfoDtoForGET_PreProcessor> responseList = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(responseList, pageable, total);
    }

    @Override
    // 조인때문에 (select 결과로 얻지 못한 필드는 조인에서 사용할 수 없음) DTO로 바로 뱉는건 힘들거같다.
    public Page<Post> findPostsByTags(List<String>tagNames, Pageable pageable) {
        QueryResults<Post> results = jpaQueryFactory
                .selectFrom(post)
                .leftJoin(post.postTags, postTag).fetchJoin()
                .where(postTag.tag.name.in(tagNames))
                .groupBy(post.id)
                .having(post.id.count().eq((long) tagNames.size()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Post> responseList = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(responseList, pageable, total);
    }

//tag1,tag2,tag3
    private BooleanExpression productCategoryNameFilter(String productCategoryName) {

//        deprecated
//        if(StringUtils.isEmpty(productCategoryName)) {
//            return null;
//        }
//        return post.productCategory.name.eq(productCategoryName);
        return StringUtils.hasText(productCategoryName) ? post.productCategory.name.eq(productCategoryName) : null;
    }

    private BooleanExpression wishCategoryNameFilter(String wishCategoryName) {
        return StringUtils.hasText(wishCategoryName) ? post.wishCategory.name.eq(wishCategoryName) : null;
    }

    private BooleanExpression priceFilter(String minPrice, String maxPrice) {
        if(isNullOrEmpty(minPrice) && isNullOrEmpty(maxPrice)) {
            return null;
        }
        // 둘중 하나만 값이 있는 경우를 처리해주겠음
        if(isNullOrEmpty(minPrice) && !isNullOrEmpty(maxPrice)) {
            return post.price.loe(Long.parseLong(maxPrice));
        } else if(!isNullOrEmpty(minPrice) && isNullOrEmpty(maxPrice)) {
            return post.price.goe(Long.parseLong(minPrice));
        }
        // 어느 조건문에도 걸리지 않으면 둘 다 존재하는 경우. between으로 처리함.
        return post.price.between(Long.parseLong(minPrice), Long.parseLong(maxPrice));
    }
}
