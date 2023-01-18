package f3f.dev1.domain.comment.dao;

import f3f.dev1.domain.comment.dto.CommentDTO;
import f3f.dev1.domain.comment.model.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static f3f.dev1.domain.comment.dto.CommentDTO.*;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {

    Optional<Comment> findById(Long id);

    boolean existsById(Long id);

    List<Comment> findByPostId(Long postId);

    List<Comment> findByParentId(Long parentId);



    // post id랑 comment id를 함께 충족하는 댓글을 찾는다
    Optional<Comment> findByPostIdAndId(Long postId, Long commentId);
    // user id랑 comment id를 함께 충족하는 댓글을 찾는다
    Optional<Comment> findByAuthorIdAndId(Long authorId, Long commentId);
    boolean existsByPostId(Long postId);

//    @Query("select new f3f.dev1.domain.comment.dto.CommentDTO.CommentInfoDto(c.id, c.post.id, c.author.id, c.content, c.depth, c.parent.id) " +
//            "from Comment c where c.post.id = :postId")
//    List<CommentInfoDto> findCommentInfoDtoByPostId(@Param("postId") Long postId);
}
