package f3f.dev1.domain.comment.dao;

import f3f.dev1.domain.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(Long id);
    boolean existsById(Long id);
    List<Comment> findByPostId(Long postId);
    // post id랑 comment id를 함께 충족하는 댓글을 찾는다
    Optional<Comment> findByPostIdAndId(Long postId, Long commentId);
    // user id랑 comment id를 함께 충족하는 댓글을 찾는다
    Optional<Comment> findByAuthorIdAndId(Long authorId, Long commentId);
    boolean existsByPostId(Long postId);
}
