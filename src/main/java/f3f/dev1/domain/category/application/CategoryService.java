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

import java.util.List;

import static f3f.dev1.domain.category.dto.CategoryDTO.*;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    //멤버 중 어드민만 카테고리를 CRUD할 수 있다.
    //카테고리
    //TODO 위시, 프로덕트 카테고리  구분해서 만들기(뎁스 3)
    @Transactional
    public Long createCategory(CategorySaveRequest categorySaveRequest){
        Member admin = memberRepository.findById(categorySaveRequest.getMemberId()).orElseThrow(NotFoundByIdException::new);
        //유저 어드민 확인해야되는데 어드민 만들어지면 해야할듯

        //질문)category.getParent()-- null을 categoryRepository.existById(categor.getParentId())이렇게 해주는게 나으려나 뭐가 달라?

        //카테고리 이름에 대한 처리
        if(categoryRepository.existsByName(categorySaveRequest.getName())){
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
        // 루트 카테고리가 존재하는데 부모가 없다면 예외 던지기
        //설정 처음에 루트 카테고리 만들기-> 카테고리가 있는지 확인하기
        if(categoryRepository.findAll().isEmpty()) {//이걸 써도 되나?
            if (!categoryRepository.existsById(category.getParent().getId())) {
                if (category.getDepth() != 0) {
                    throw new CategoryException("부모카테고리 설정 오류 : root 카테고리여야합니다.");
                }
            }
        }
        //root 카테고리 하위
        else{
            //질문)depth를 NonNUll로 했는데 그럼 parent에서 deth+1을 안해도 되지 않나? 동준오빠는 무한 depth여서?set을 왜 한거지?
            //질문) save 위치 나는 save한다음에 add가 들어가는게 맞는 것 같음. 근데 그렇게 안하면 코드 1줄로 줄일 수 있음.
           // Category parentCategory = categoryRepository.findById(category.getParent().getId()).orElseThrow(NotFoundByIdException::new);
            if(category.getDepth().equals(category.getParent().getDepth())){ //왜 !=로 비교 안돼?
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


    //TODO)각각 조회말고 전체 조회 해야함.
    //이건 카테고리를 클릭하면 해당 카테고리 리스트만 나오는거.
    //카테고리 누르면 자식 카테고리 나오는 식. (쿠팡)
    @Transactional(readOnly = true)
    public List<Category> readCategoryByCategory(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(NotFoundByIdException::new);
//        if(category.getDepth() == 1){
//
//        }
        return category.getChild();
    }

    //루트에서 바로 그냥 카테고리 불러오기->원래 들어가면 바로 카테고리 보이게?->필터형식?
//    @Transactional(readOnly = true)
//    public List<Category> readTotalCategory(){
//
//
//    }



    @Transactional(readOnly = true)
    public List<Post> readPostByCategory(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(NotFoundByIdException::new);

        return category.getProducts();
    }

    //카테고리 업데이트
    @Transactional
    public String updateCategoryName(Long id, String newName){
        Category category = categoryRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        //업데이트하려는 카테고리 이름이 이미 존재할 때
        if(categoryRepository.existsByName(newName)){
            throw new CategoryException("이미 존재하는 카테고리 이름입니다.");
        }
        else{
            category.updateCategoryName(newName);
        }
        return "UPDATE";
    }

    //카테고리 깊이 수정 -> 그냥 삭제하고 다시 만들어?-> 12/31 회의 결과 루트->물물교환, 끼리끼리-> 도서, 화장품 등으로 나뉘어서 굳이 필요 없을 듯.
//    @Transactional
//    public String updateCategoryDepth(Long id, String newDepth){
//        Category category = categoryRepository.findById(id).orElseThrow(NotFoundByIdException::new);
////        category.getDepth().toString();
//        return "UPDATE";
//    }


    //카테고리 삭제
    //카테고리를 삭제하면 관련 포스트도 다 삭제된다.
    @Transactional
    public String deleteCategoryByID(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        //자식 카테고리가 있다면, 자식 카테고리를 지운다.(계속 반복)
        while(!category.getChild().isEmpty()){
            for(Category childCategory : category.getChild()){
                //자식 카테고리에 포스트가 있다면, 포스트를 다 지움.
                if(!childCategory.getProducts().isEmpty()){
                    for(Post post : childCategory.getProducts()) {
                        postRepository.delete(post); //deleteAll, deleteAllInBatch()다시 찾아보기
                    }
                }
                categoryRepository.delete(childCategory);
            }
        }
        return "delete";
    }









}
