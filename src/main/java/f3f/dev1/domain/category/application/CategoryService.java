package f3f.dev1.domain.category.application;

import f3f.dev1.domain.category.dao.CategoryRepository;
import f3f.dev1.domain.category.dto.CategoryDTO;
import f3f.dev1.domain.category.exception.CategoryException;
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

        //질문)category.getParent()-- null을 categoryRepository.existById(categor.getParentId())이렇게 해주는게 나으려나 뭐가 달라?

        //카테고리 이름에 대한 처리
        //getName() Null은 @NonNull하면 따로 안넣어도 될듯
        if(categoryRepository.existByName(categorySaveRequest.getName())){
            throw new CategoryException("이미 존재하는 카테고리입니다.");
        }
        //depth가 root 포함 0,1,2임
        if(categorySaveRequest.getDepth()>2){
            throw new CategoryException("max depth(2)를 초과하셨습니다.");
        }
        //카테고리 엔티티 생성
        Category category = categorySaveRequest.toEntity();

        //부모 카테고리 확인
        //부모 카테고리가 존재하지 않음. -> 무조건 root 카테고리
        if(!categoryRepository.existsById(category.getParent().getId())) {
            if (category.getDepth() != 0) {
                throw new CategoryException("부모카테고리 설정 오류 : root 카테고리여야합니다.");
            }
        }
        //root 카테고리 하위
        else{
            //질문)depth를 NonNUll로 했는데 그럼 parent에서 deth+1을 안해도 되지 않나? 동준오빠는 무한 depth여서?set을 왜 한거지?
            //질문) save 위치 나는 save한다음에 add가 들어가는게 맞는 것 같음. 근데 그렇게 안하면 코드 1줄로 줄일 수 있음.
           // Category parentCategory = categoryRepository.findById(category.getParent().getId()).orElseThrow(NotFoundByIdException::new);
            if(category.getDepth() != category.getParent().getDepth()){
                throw new CategoryException("카테고리 depth 오류 : (1,2)중에서 확인");
            }
//            if(parentCategory.getName().equals("root")){
//                if(category.getDepth() != 1){
//                    throw new CategoryException("카테고리 설정 오류 : depth가 1이어야합니다.");
//                }
                //category.setDepth(parentCategory.getDepth()+1); -> NonNull로 받았으니까 필요 없을 듯.
            category.getParent().getChild().add(category);

        }
        categoryRepository.save(category);
        return category.getId();
    }


}
