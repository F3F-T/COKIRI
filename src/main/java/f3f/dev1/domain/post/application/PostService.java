package f3f.dev1.domain.post.application;

import f3f.dev1.domain.category.dao.CategoryRepository;
import f3f.dev1.domain.category.exception.NotFoundProductCategoryNameException;
import f3f.dev1.domain.category.exception.NotFoundWishCategoryNameException;
import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.member.exception.NotAuthorizedException;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.exception.NotFoundPostListByAuthorException;
import f3f.dev1.domain.post.exception.NotMatchingAuthorException;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.tag.dao.PostTagRepository;
import f3f.dev1.domain.tag.dao.TagRepository;
import f3f.dev1.domain.tag.exception.NotFoundByPostAndTagException;
import f3f.dev1.domain.tag.model.PostTag;
import f3f.dev1.domain.tag.model.Tag;
import f3f.dev1.domain.trade.dao.TradeRepository;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.post.dto.PostDTO.*;

@Service
@Validated
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final TradeRepository tradeRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final PostTagRepository postTagRepository;

    @Transactional
    public Long savePost(PostSaveRequest postSaveRequest, Long currentMemberId) {

        Member member = memberRepository.findById(postSaveRequest.getAuthorId()).orElseThrow(NotFoundByIdException::new);
        List<PostTag> resultsList = new ArrayList<>();
        Category productCategory = categoryRepository.findCategoryByName(postSaveRequest.getProductCategory()).orElseThrow(NotFoundProductCategoryNameException::new);
        Category wishCategory = categoryRepository.findCategoryByName(postSaveRequest.getWishCategory()).orElseThrow(NotFoundWishCategoryNameException::new);
        memberRepository.findById(currentMemberId).orElseThrow(NotFoundByIdException::new);
        if(!member.getId().equals(currentMemberId)) {
            throw new NotAuthorizedException("요청자가 현재 로그인한 유저가 아닙니다");
        }
        // resultList가 postService의 save 에서는 항상 비어있는 리스트로 들어간다.
        // 컨트롤러에서 postService.save 이후에 tagService를 호출해 addTagToPost로 태그를 추가해주는데,
        // 그때 포스트가 호출되어 리스트에 PostTag가 추가되게 된다.
        Post post = postSaveRequest.toEntity(member, productCategory, wishCategory, resultsList);
        member.getPosts().add(post);
        postRepository.save(post);
        return post.getId();
    }

    /* TODO
        R : read
        카테고리, 태그 필터링은 Post에서 할 수 없다. 각각의 서비스에서 구현하겠다.
        title 별로 조회도 필요할까? 검색엔진이 필요한가? - 피드백 결과 일단은 제외하는 걸로.
     */

    // 게시글 전체 조회
    // 컨트롤러에서는 쓰이지 않음. 컨트롤러에서는 findPostsWithConditions를 사용하고 아무 조건이 건네지지 않으면 전체조회를 수행함.
    public List<PostInfoDtoWithTag> findAllPosts() {
        List<Post> allPosts = postRepository.findAll();
        List<PostInfoDtoWithTag> response = new ArrayList<>();
        for (Post post : allPosts) {
            List<PostTag> postTags = postTagRepository.findByPost(post);
            List<String> tagNames = new ArrayList<>();
            for (PostTag postTag : postTags) {
                tagNames.add(postTag.getTag().getName());
            }
            PostInfoDtoWithTag responseEach = post.toInfoDtoWithTag(tagNames);
            response.add(responseEach);
        }
        return response;
    }


    // findByIdPostListDTO는 검색된 포스트 리스트를 가지고 있는 DTO이다.
    @Transactional(readOnly = true)
    public List<PostInfoDtoWithTag> findPostByAuthor(Long authorId) {
        // TODO 없애도 될 예외같음. 게시글이 없으면 빈 리스트를 반환해주면 된다.
        if(!postRepository.existsByAuthorId(authorId)) {
            throw new NotFoundPostListByAuthorException("해당 작성자의 게시글이 없습니다.");
        }
        List<PostInfoDtoWithTag> response = new ArrayList<>();
        List<Post> byAuthor = postRepository.findByAuthorId(authorId);
        for (Post post : byAuthor) {
            List<PostTag> postTags = postTagRepository.findByPost(post);
            List<String> tagNames = new ArrayList<>();
            for (PostTag postTag : postTags) {
                tagNames.add(postTag.getTag().getName());
            }
            PostInfoDtoWithTag responseEach = post.toInfoDtoWithTag(tagNames);
            response.add(responseEach);
        }
        return response;
    }

    // TODO 추가된 가격대 검색 기능 반드시 테스트 해봐야 한다.
    @Transactional(readOnly = true)
    public List<PostInfoDtoWithTag> findPostsWithConditions(SearchPostRequest searchPostRequest) {
        List<Post> resultPostList = new ArrayList<>();
        List<PostInfoDtoWithTag> response = new ArrayList<>();
        List<String> tagNames = searchPostRequest.getTagNames();
        String productCategoryName = searchPostRequest.getProductCategory();
        String wishCategoryName = searchPostRequest.getWishCategory();
        String minPrice = searchPostRequest.getMinPrice();
        String maxPrice = searchPostRequest.getMaxPrice();
        boolean flag = false;

        if(minPrice != null && maxPrice != null) {
            if(minPrice.chars().allMatch(Character::isDigit)) {
                flag = true;
                if(!maxPrice.chars().allMatch(Character::isDigit)) {
                    List<Post> postsFromMinPrice = postRepository.findByPriceGreaterThanEqual(Long.parseLong(minPrice));
                    resultPostList.addAll(postsFromMinPrice);
                } else {
                    List<Post> postsFromBoth = postRepository.findByPriceBetween(Long.parseLong(minPrice), Long.parseLong(maxPrice));
                    resultPostList.addAll(postsFromBoth);
                }
            } else if(maxPrice.chars().allMatch(Character::isDigit)){
                flag = true;
                List<Post> postsFromMaxPrice = postRepository.findByPriceLessThanEqual(Long.parseLong(maxPrice));
                resultPostList.addAll(postsFromMaxPrice);
            }
        }

        if(!tagNames.isEmpty()) {
            // 카테고리 정보는 없고 태그로만 검색하는 경우
            if(productCategoryName.equals("") && wishCategoryName.equals("")) {
                for(int i=0; i<tagNames.size(); i++) {
                    List<PostTag> postTags = postTagRepository.findByTagName(tagNames.get(i));
                    List<Post> posts = postRepository.findByPostTagsIn(postTags);
                    if(i == 0) {
                        if(flag) {
                            resultPostList.retainAll(posts);
                        } else {
                            resultPostList.addAll(posts);
                        }
                    } else {
                        resultPostList.retainAll(posts);
                    }
                }
                // 올린 상품 카테고리 정보와 태그만 있고, 희망 상품 카테고리 정보는 없이 검색한 경우
            } else if(!productCategoryName.equals("") && wishCategoryName.equals("")) {
                for(int i=0; i<tagNames.size(); i++) {
                    List<PostTag> postTags = postTagRepository.findByTagName(tagNames.get(i));
                    List<Post> posts = postRepository.findByProductCategoryNameAndPostTagsIn(productCategoryName, postTags);
                    if(i == 0) {
                        if(flag) {
                            resultPostList.retainAll(posts);
                        } else {
                            resultPostList.addAll(posts);
                        }
                    } else {
                        resultPostList.retainAll(posts);
                    }
                }
                // 올린 상품 카테고리 정보는 없고, 희망 상품 카테고리 정보와 태그로만 검색한 경우
            } else if(productCategoryName.equals("") && !wishCategoryName.equals("")) {
                for(int i=0; i<tagNames.size(); i++) {
                    List<PostTag> postTags = postTagRepository.findByTagName(tagNames.get(i));
                    List<Post> posts = postRepository.findByWishCategoryNameAndPostTagsIn(wishCategoryName, postTags);
                    if(i == 0) {
                        if(flag) {
                            resultPostList.retainAll(posts);
                        } else {
                            resultPostList.addAll(posts);
                        }
                    } else {
                        resultPostList.retainAll(posts);
                    }
                }
                // 올린 상품 카테고리와 희망 상품 카테고리, 태그 모두 사용해서 검색한 경우
            } else if(!productCategoryName.equals("") && !wishCategoryName.equals("")) {
                for(int i=0; i<tagNames.size(); i++) {
                    List<PostTag> postTags = postTagRepository.findByTagName(tagNames.get(i));
                    List<Post> posts = postRepository.findByProductCategoryNameAndWishCategoryNameAndPostTagsIn(productCategoryName, wishCategoryName, postTags);
                    if(i == 0) {
                        if(flag) {
                            resultPostList.retainAll(posts);
                        } else {
                            resultPostList.addAll(posts);
                        }
                    } else {
                        resultPostList.retainAll(posts);
                    }
                }
            }

        } else if(tagNames.isEmpty()) {
            // 올린 상품 카테고리와 희망 상품 카테고리, 태그정보 모두 없이 검색한 경우 - 전체 조회 결과로 반환
            if(productCategoryName.equals("") && wishCategoryName.equals("")) {
                List<Post> all = postRepository.findAll();
                if(flag) {
                    resultPostList.retainAll(all);
                } else {
                    resultPostList.addAll(all);
                }
            // 올린 상품 카테고리만 있고 희망 상품 카테고리와 태그는 없이 검색하는 경우
            } else if(!productCategoryName.equals("") && wishCategoryName.equals("")) {
                List<Post> postsFromProductCategoryName = postRepository.findByProductCategoryName(productCategoryName);
                if(flag) {
                    resultPostList.retainAll(postsFromProductCategoryName);
                } else {
                    resultPostList.addAll(postsFromProductCategoryName);
                }
                // 올린 상품 카테고리와 태그는 없고 희망 상품 카테고리만 사용하여 검색하는 경우
            } else if(productCategoryName.equals("") && !wishCategoryName.equals("")) {
                List<Post> postsFromWishProductCategoryName = postRepository.findByWishCategoryName(wishCategoryName);
                if(flag) {
                    resultPostList.retainAll(postsFromWishProductCategoryName);
                } else {
                    resultPostList.addAll(postsFromWishProductCategoryName);
                }
            } else if(!productCategoryName.equals("") && !wishCategoryName.equals("")) {
                List<Post> posts = postRepository.findByProductCategoryNameAndWishCategoryName(productCategoryName, wishCategoryName);
                if(flag) {
                    resultPostList.retainAll(posts);
                } else {
                    resultPostList.addAll(posts);
                }
            }
        }
            // 지금까지 resultPostList를 위에서 필터링하여 만들었다.
            // 여기서부터는 필터링된 resultPostList를 postInfoDto로 바꿔서 리스트에 추가하는 파트
            for (Post post : resultPostList) {
                List<PostTag> postTags = postTagRepository.findByPost(post);
                List<String> tagNamesOfPost = new ArrayList<>();
                for (PostTag postTag : postTags) {
                    tagNamesOfPost.add(postTag.getTag().getName());
                }
                PostInfoDtoWithTag responseEach = post.toInfoDtoWithTag(tagNamesOfPost);
                response.add(responseEach);
            }
        return response;
    }

    // TODO 거래 가능한 게시글만 검색하기

    @Transactional(readOnly = true)
    public PostInfoDtoWithTag findPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(NotFoundByIdException::new);
//        // TODO 거래 가능 상태인지 확인하기
        List<String> tagNames = new ArrayList<>();
        List<PostTag> postTags = postTagRepository.findByPost(post);
        for (PostTag postTag : postTags) {
            tagNames.add(postTag.getTag().getName());
        }
//        Trade trade = tradeRepository.findByPostId(post.getId()).orElseThrow(NotFoundByIdException::new);
        PostInfoDtoWithTag response = post.toInfoDtoWithTag(tagNames);
        return response;
    }

    /* TODO
        U : 게시글 업데이트
     */

    @Transactional
    public PostInfoDtoWithTag updatePost(UpdatePostRequest updatePostRequest, Long currentMemberId) {


        Post post = postRepository.findById(updatePostRequest.getId()).orElseThrow(NotFoundByIdException::new);
        Category productCategory = categoryRepository.findCategoryByName(updatePostRequest.getProductCategory()).orElseThrow(NotFoundProductCategoryNameException::new);
        Category wishCategory = categoryRepository.findCategoryByName(updatePostRequest.getWishCategory()).orElseThrow(NotFoundWishCategoryNameException::new);
        List<Tag> tags = tagRepository.findByNameIn(updatePostRequest.getTagNames());
        List<PostTag> postTags = new ArrayList<>();
        if(!updatePostRequest.getAuthorId().equals(currentMemberId)) {
            throw new NotAuthorizedException("요청자가 현재 로그인한 유저가 아닙니다");
        }
        if(!post.getAuthor().getId().equals(updatePostRequest.getAuthorId())) {
            throw new NotMatchingAuthorException("게시글 작성자가 아닙니다.");
        }
        // postTag에서 post는 아래의 코드로 지울 수 있지만, post 단에서 postTag는 여기서 지울 수 없다.
        // 따라서 컨트롤러에서 tag 서비스에 먼저 들러 관련 postTag를 다 지우고 현재의 메소드를 호출하도록 하겠다.
        // postTagRepository.deletePostTagByPost(post);
        if(tags.isEmpty()) {
            post.updatePostInfos(updatePostRequest, productCategory, wishCategory, new ArrayList<>());
        } else {
            // PostTag가 없으면 여기서 새로 만들어서 추가까지 해줘야 한다.
            for (Tag tag : tags) {
                if(!postTagRepository.existsByPostAndTag(post,tag)) {
                    // 업데이트 요청으로 넘어온 태그가 기존에 포스트에 존재하지 않는 태그라면 새로 추가해주기
                    PostTag postTag = PostTag.builder()
                            .post(post)
                            .tag(tag)
                            .build();
                    postTagRepository.save(postTag);
                    tag.getPostTags().add(postTag);
                    postTags.add(postTag);
                } else {
                    // 기존에 존재하는 태그라면 다시 클리어된 리스트에 추가해주기
                    PostTag postTag = postTagRepository.findByPostAndTag(post, tag).orElseThrow(NotFoundByPostAndTagException::new);
                    tag.getPostTags().add(postTag);
                    postTags.add(postTag);
                }
            }
            post.updatePostInfos(updatePostRequest, productCategory, wishCategory, postTags);
        }
        List<PostTag> postTagsOfPost = postTagRepository.findByPost(post);
        List<String> tagNames = new ArrayList<>();
        for (PostTag postTag : postTagsOfPost) {
            tagNames.add(postTag.getTag().getName());
        }
        PostInfoDtoWithTag response = post.toInfoDtoWithTag(tagNames);
        return response;
    }

    @Transactional
    public String deletePost(DeletePostRequest deletePostRequest, Long currentMemberId) {
        // 먼저 해당 게시글이 존재하는지 검증
        Post post = postRepository.findById(deletePostRequest.getId()).orElseThrow(NotFoundByIdException::new);
        // 그 후 작성자가 요청자와 동일인물인지 검증
        Member author = post.getAuthor();
        if(!author.getId().equals(currentMemberId)) {
            throw new NotAuthorizedException("요청자가 현재 로그인한 유저가 아닙니다");
        }
        if(!author.getId().equals(deletePostRequest.getAuthorId())) {
            throw new NotMatchingAuthorException("게시글 작성자가 아닙니다.");
        }

        postRepository.deleteById(deletePostRequest.getId());
        return "DELETE";
    }

}
