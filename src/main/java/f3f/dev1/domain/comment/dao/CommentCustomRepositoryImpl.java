package f3f.dev1.domain.comment.dao;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import f3f.dev1.domain.comment.dto.CommentDTO.CommentInfoDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static f3f.dev1.domain.comment.model.QComment.*;

@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CommentInfoDto> findCommentDtoByPostId(Long postId) {

        List<CommentInfoDto> results = jpaQueryFactory
                .select(Projections.constructor(CommentInfoDto.class,
                        comment.id,
                        comment.post.id,
                        comment.author.id,
                        comment.author.nickname,
                        comment.author.imageUrl,
                        comment.content,
                        comment.depth,
                        comment.parent.id,
                        comment.createDate))
                .from(comment)
                .where(comment.post.id.eq(postId))
                .fetch();
        return results;
    }
}
