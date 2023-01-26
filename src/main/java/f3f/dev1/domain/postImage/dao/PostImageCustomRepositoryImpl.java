package f3f.dev1.domain.postImage.dao;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static f3f.dev1.domain.postImage.dto.PostImageDTO.*;
import static f3f.dev1.domain.postImage.model.QPostImage.*;

@RequiredArgsConstructor
public class PostImageCustomRepositoryImpl implements PostImageCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostImageInfoDto> findByPostIdWithQueryDSL(Long postId) {
        List<PostImageInfoDto> result = jpaQueryFactory
                .select(Projections.constructor(PostImageInfoDto.class,
                        postImage.id,
                        postImage.imgPath))
                .from(postImage)
                .where(postImage.post.id.eq(postId))
                .fetch();

        return result;
    }
}
