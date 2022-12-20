package f3f.dev1.domain.category.application;

import f3f.dev1.domain.category.dao.CategoryRepository;
import f3f.dev1.domain.category.dto.CategoryDTO;
import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static f3f.dev1.domain.category.dto.CategoryDTO.*;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    //멤버 중 어드민만 카테고리를 CRUD할 수 있다.
    //카테고리
    @Transactional
    public Long createCategory(CategorySaveRequest categorySaveRequest){
        Member admin = memberRepository.findById(categorySaveRequest.getMember().getId()).orElseThrow(NotFoundByIdException::new);
        //유저 어드민 확인해야되는데 어드민 만들어지면 해야할듯
        //포스트가 유효한지 확인
        Post post = postRepository.findById(categorySaveRequest.getPost().getId()).orElseThrow(NotFoundByIdException::new);

        Category category = categorySaveRequest.toEntity();

        //부모 카테고리도 root의 자식 카테고리
        //루트부터 먼저 만들기

        //카테고리이름이 들어왔고 부모가 존재한다면,(자식 카테고리)
        if(category.getName() != null && category.getParent()==null){
            //부모를 가져옴.
            Category parentCategory = categoryRepository.findById(categorySaveRequest.getParent().getId()).orElseThrow(NotFoundByIdException::new);

            //부모가 루트이면, parent Category.
            if(parentCategory.getName().equals("root")){

            }
            else{

            }




            categoryRepository.save(category);

        }

        return category.getId();
    }


}
