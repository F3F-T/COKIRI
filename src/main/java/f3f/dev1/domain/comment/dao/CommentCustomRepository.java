package f3f.dev1.domain.comment.dao;

import f3f.dev1.domain.comment.dto.CommentDTO;

import java.util.List;

import static f3f.dev1.domain.comment.dto.CommentDTO.*;

public interface CommentCustomRepository {
    List<CommentInfoDto> findCommentDtoByPostId(Long postId);
}
