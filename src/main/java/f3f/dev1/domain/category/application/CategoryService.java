package f3f.dev1.domain.category.application;

import f3f.dev1.domain.category.dao.CategoryRepository;
import f3f.dev1.domain.category.dto.CategoryDTO;
import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

import static f3f.dev1.domain.category.dto.CategoryDTO.*;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final MemberRepository userRepository;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

//    public Long CreateCategory(CategorySaveRequest categorySaveRequest){
//        //포스트 초기화는 해줘도 처음에 확인해야되는거 아님? 딱히 필요 없나?
//        //Post post = postRepository.existsById(categorySaveRequest.)
//        //카테고리가 뭘 해줘야하지? 처음부터 만들어져있는애들 아닌가?
//
//
//
//        return
//    }


}
