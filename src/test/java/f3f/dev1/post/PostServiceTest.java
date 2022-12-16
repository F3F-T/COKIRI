package f3f.dev1.post;

import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.comment.dao.CommentRepository;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.dto.PostDTO;
import f3f.dev1.domain.trade.model.Trade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static f3f.dev1.domain.post.dto.PostDTO.*;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    MemberRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;


//    public PostSaveRequest CreatePostSaveRequestDTO(String title, String content, Boolean tradeEachOther, Member author, Category productCategory, Category wishCategory, Trade trade) {
//        PostSaveRequest.builder()
//                .
//                .build()
//    }

    @BeforeEach
    public void deleteAll() {
        userRepository.deleteAll();
        postRepository.deleteAll();
        commentRepository.deleteAll();
    }


    // TODO 카테고리 완료되면 Create test 마저 진행하겠다
    @Test
    public void savePostSuccess() throws Exception {
        //given

        //when
        
        //then
    }

    @Test
    public void findPostByAuthorSuccess() throws Exception {
        //given

        //when

        //then
    }
}
