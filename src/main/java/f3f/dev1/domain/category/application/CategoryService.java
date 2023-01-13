package f3f.dev1.domain.category.application;

import f3f.dev1.domain.category.dao.CategoryRepository;
import f3f.dev1.domain.category.dto.CategoryDTO;
import f3f.dev1.domain.category.exception.CanNotDeleteCategoryException;
import f3f.dev1.domain.category.exception.CategoryException;
import f3f.dev1.domain.category.exception.NotFoundCategoryByNameException;
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
    //TODO 위시, 프로덕트 카테고리 구분해서 만들기(뎁스 3)
    @Transactional
    public Long createCategory(CategorySaveRequest categorySaveRequest) {
        Member admin = memberRepository.findById(categorySaveRequest.getMemberId()).orElseThrow(NotFoundByIdException::new);
        //유저 어드민 확인해야되는데 어드민 만들어지면 해야할듯

        //카테고리 이름에 대한 처리
        if (categoryRepository.existsByName(categorySaveRequest.getName())) {
            throw new CategoryException("이미 존재하는 카테고리입니다.");
        }
        //depth가 root 포함 0,1,2임
        if (categorySaveRequest.getDepth() > 1) {
            throw new CategoryException("max depth(1)를 초과하셨습니다.");
        }

        //이거 만들긴 했는데 이정도 이면 그냥 데이터에 밖는게 나을듯 시바

        //부모 카테고리 확인
        //부모 카테고리가 존재하지 않음. -> 무조건 root 카테고리
        // 루트 카테고리가 존재하는데 부모가 없다면 예외 던지기
        //설정 처음에 루트 카테고리 만들기-> 카테고리가 있는지 확인하기
//        if (!categoryRepository.existsById(category.getParent().getId())) {

        //처음에 아무것도 없이 카테고리생성이 들어왔을 때, 루트를 만들고 카테고리 생성해주기.
        if(categorySaveRequest.getParentId()==null){
            if (categoryRepository.findAll().isEmpty()) {
                Category rootCategory = Category.builder()
                        .name("root")
                        .depth(0L)
                        .parent(null)
                        .build();
                categoryRepository.save(rootCategory);
                Category category = categorySaveRequest.toEntity(rootCategory);
                categoryRepository.save(category);
                category.getParent().getChild().add(category);
                return category.getId();
                }
            else{//부모가 널인데 카테고리가 비어있지 않다 -> 루트 있으니까 무조건 부모 있어야함.
                throw new CategoryException("부모 카테고리 오류(depth가 1이면 root가 부모)");
            }
        }
        else{//널이 아니면 생성
            Category parentCat = categoryRepository.findById(categorySaveRequest.getParentId()).orElseThrow(NotFoundByIdException::new);
            //카테고리 엔티티 생성
            Category category = categorySaveRequest.toEntity(parentCat);
            //root 카테고리 하위
            //부모 카테고리가 있다면

            if (!category.getDepth().equals(category.getParent().getDepth() + 1)) {
                throw new CategoryException("카테고리 depth 오류 : depth는 1이어야함.");
            }
            categoryRepository.save(category);
            category.getParent().getChild().add(category);
            return category.getId();
        }
    }


    //TODO)각각 조회말고 전체 조회 해야함.
    //이건 카테고리를 클릭하면 해당 카테고리 리스트만 나오는거.
    //카테고리 누르면 자식 카테고리 나오는 식. (쿠팡)
    @Transactional(readOnly = true)
    public List<Category> readCategoryByCategory(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        return category.getChild();
    }


   // 루트에서 바로 그냥 카테고리 불러오기->원래 들어가면 바로 카테고리 보이게?->필터형식?
    @Transactional(readOnly = true)
    public List<Category> readTotalCategory(){
        return categoryRepository.findAll();
    }

    //카테고리 아이디로 포스트 조회
    @Transactional(readOnly = true)
    public List<Post> readProductByCategory(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(NotFoundByIdException::new);

        return category.getProducts();
    }

    @Transactional(readOnly = true)
    public List<Post> readWishProductByCategory(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(NotFoundByIdException::new);

        return category.getWishProducts();
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

    //카테고리 깊이 수정 -> 원래는 depth를 받아서 했는데 부모카테고리 문제가 있어서 이름만!
    //자식 카테고리도 따라가고 부모 카테고리 잘 있는지, 원래꺼 사라졌는지 확인
    @Transactional
    public String updateCategoryDepth(Long id, String name){//바꾸고자 하는 카테고리 아이디, 바꿀 카테고리 이름
        Category category = categoryRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        Category parentCat = categoryRepository.findCategoryByName(name).orElseThrow(NotFoundCategoryByNameException::new);

        category.updateCategoryDepth(parentCat.getDepth()+1);
        parentCat.getChild().add(category);

        return "UPDATE";
    }


    //카테고리 삭제
    //카테고리를 삭제하면 관련 포스트도 다 삭제된다.
    //TODO 포스트 상위에 속하게 하기, 상위 삭제시 하위 카테고리 삭제 여부 결정
    @Transactional
    public String deleteCategoryByID(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        Category parentCat = categoryRepository.findById(category.getParent().getId()).orElseThrow(NotFoundByIdException::new);

        //자식 카테고리가 있다면, 자식 카테고리를 지운다.(계속 반복) -> 하위 카테고리가 없을 경우 삭제 가능.
        if(category.getChild().isEmpty()){
//            for(Category childCategory : category.getChild()){
//                categoryRepository.delete(childCategory);
//            }
            categoryRepository.delete(category);
        }
        else{
            throw new CanNotDeleteCategoryException();
        }
        return "DELETE";
    }

    @Transactional
    public String deleteCategoryByName(String name){
        if(!categoryRepository.existsByName(name)){
            throw new NotFoundCategoryByNameException();
        }
        else{
            Category category = categoryRepository.findCategoryByName(name).get();
            //자식 카테고리가 있다면, 자식 카테고리를 지운다.(계속 반복) -> 하위 카테고리가 없을 경우 삭제 가능.
            if(category.getChild().isEmpty()){
//            for(Category childCategory : category.getChild()){
//                categoryRepository.delete(childCategory);
//            }
                categoryRepository.delete(category);
            }
            else{
                throw new CanNotDeleteCategoryException();
            }
            return "DELETE";
        }

    }
}
