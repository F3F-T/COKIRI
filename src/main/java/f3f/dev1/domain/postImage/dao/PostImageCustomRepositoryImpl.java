package f3f.dev1.domain.postImage.dao;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import f3f.dev1.domain.postImage.dto.PostImageDTO;
import f3f.dev1.domain.postImage.model.QPostImage;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static f3f.dev1.domain.postImage.dto.PostImageDTO.*;
import static f3f.dev1.domain.postImage.model.QPostImage.*;

@RequiredArgsConstructor
public class PostImageCustomRepositoryImpl implements PostImageCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<postImageInfoDto> findByPostId(Long postId) {
        List<postImageInfoDto> result = jpaQueryFactory
                .select(Projections.constructor(postImageInfoDto.class,
                        postImage.id,
                        postImage.imgPath))
                .from(postImage)
                .where(postImage.post.id.eq(postId))
                .fetch();

        return result;
    }
}
