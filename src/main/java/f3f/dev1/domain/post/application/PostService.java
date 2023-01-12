package f3f.dev1.domain.post.application;

import f3f.dev1.domain.category.dao.CategoryRepository;
import f3f.dev1.domain.category.exception.NotFoundProductCategoryNameException;
import f3f.dev1.domain.category.exception.NotFoundWishCategoryNameException;
import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.comment.dao.CommentRepository;
import f3f.dev1.domain.comment.dto.CommentDTO;
import f3f.dev1.domain.comment.model.Comment;
import f3f.dev1.domain.member.dto.MemberDTO;
import f3f.dev1.domain.member.exception.NotAuthorizedException;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.message.dao.MessageRoomRepository;
import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.post.dao.PostCustomRepositoryImpl;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.dao.ScrapPostRepository;
import f3f.dev1.domain.post.exception.NotFoundPostListByAuthorException;
import f3f.dev1.domain.post.exception.NotMatchingAuthorException;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.post.model.ScrapPost;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import f3f.dev1.domain.scrap.exception.UserScrapNotFoundException;
import f3f.dev1.domain.scrap.model.Scrap;
import f3f.dev1.domain.tag.dao.PostTagRepository;
import f3f.dev1.domain.tag.dao.TagRepository;
import f3f.dev1.domain.tag.exception.NotFoundByPostAndTagException;
import f3f.dev1.domain.tag.model.PostTag;
import f3f.dev1.domain.tag.model.Tag;
import f3f.dev1.domain.trade.dao.TradeRepository;
import f3f.dev1.domain.trade.dto.TradeDTO;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.comment.dto.CommentDTO.*;
import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.post.dto.PostDTO.*;
import static f3f.dev1.domain.trade.dto.TradeDTO.*;

@Service
@Validated
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final TradeRepository tradeRepository;
    private final CommentRepository commentRepository;
    private final ScrapRepository scrapRepository;
    private final ScrapPostRepository scrapPostRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final PostTagRepository postTagRepository;
    // Custom repository
    private final PostCustomRepositoryImpl postCustomRepository;


    // TODO 게시글 사진 개수 제한 걸기 - 게시글에 사진 필드 추가해야겠다.
    @Transactional
    public Long savePost(PostSaveRequest postSaveRequest, Long currentMemberId) {

        Member member = memberRepository.findById(postSaveRequest.getAuthorId()).orElseThrow(NotFoundByIdException::new);
        List<PostTag> resultsList = new ArrayList<>();
        Category productCategory = categoryRepository.findCategoryByName(postSaveRequest.getProductCategory()).orElseThrow(NotFoundProductCategoryNameException::new);
        Category wishCategory = categoryRepository.findCategoryByName(postSaveRequest.getWishCategory()).orElseThrow(NotFoundWishCategoryNameException::new);
        memberRepository.findById(currentMemberId).orElseThrow(NotFoundByIdException::new);

        // 401 예외 빼기
//        if(!member.getId().equals(currentMemberId)) {
//            throw new NotAuthorizedException("요청자가 현재 로그인한 유저가 아닙니다");
//        }
        // resultList가 postService의 save 에서는 항상 비어있는 리스트로 들어간다.
        // 컨트롤러에서 postService.save 이후에 tagService를 호출해 addTagToPost로 태그를 추가해주는데,
        // 그때 포스트가 호출되어 리스트에 PostTag가 추가되게 된다.
        Post post = postSaveRequest.toEntity(member, productCategory, wishCategory, resultsList);
        member.getPosts().add(post);
        postRepository.save(post);
        Trade trade = CreateTradeDto.builder().sellerId(member.getId()).postId(post.getId()).build().toEntity(member, post);
        tradeRepository.save(trade);

        return post.getId();
    }

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
            List<ScrapPost> scrapPosts = scrapPostRepository.findByPostId(post.getId());
            List<MessageRoom> messageRooms = messageRoomRepository.findByPostId(post.getId());
            PostInfoDtoWithTag responseEach = post.toInfoDtoWithTag(tagNames, (long) scrapPosts.size(), (long) messageRooms.size());
            response.add(responseEach);
        }
        return response;
    }


    // TODO 페이징 적용하기
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
            List<ScrapPost> scrapPosts = scrapPostRepository.findByPostId(post.getId());
            List<MessageRoom> messageRooms = messageRoomRepository.findByPostId(post.getId());
            PostInfoDtoWithTag responseEach = post.toInfoDtoWithTag(tagNames, (long) scrapPosts.size(), (long) messageRooms.size());
            response.add(responseEach);
        }
        return response;
    }


    // TODO 페이징 적용하기
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

//         넘어오는 가격대 문자열이 숫자로 변환할 수 없는 구조라면 현재는 그냥 무시해버린다.
//         예외를 터뜨리게 바꿔야 할까??
//         이전 로직. 일단 유지하겠음
//            if(minPrice.chars().allMatch(Character::isDigit)) {
//                flag = true;
//                if(!maxPrice.chars().allMatch(Character::isDigit)) {
//                    List<Post> postsFromMinPrice = postRepository.findByPriceGreaterThanEqual(Long.parseLong(minPrice));
//                    resultPostList.addAll(postsFromMinPrice);
//                } else {
//                    List<Post> postsFromBoth = postRepository.findByPriceBetween(Long.parseLong(minPrice), Long.parseLong(maxPrice));
//                    resultPostList.addAll(postsFromBoth);
//                }
//            } else if(maxPrice.chars().allMatch(Character::isDigit)){
//                flag = true;
//                List<Post> postsFromMaxPrice = postRepository.findByPriceLessThanEqual(Long.parseLong(maxPrice));
//                resultPostList.addAll(postsFromMaxPrice);
//            }

        if(minPrice != null && maxPrice != null) {
            if (!minPrice.equals("")) {
                flag = true;
                if (maxPrice.equals("")) {
                    List<Post> postsFromMinPrice = postRepository.findByPriceGreaterThanEqual(Long.parseLong(minPrice));
                    resultPostList.addAll(postsFromMinPrice);
                } else {
                    List<Post> postsFromBoth = postRepository.findByPriceBetween(Long.parseLong(minPrice), Long.parseLong(maxPrice));
                    resultPostList.addAll(postsFromBoth);
                }
            } else if (!maxPrice.equals("")) {
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
                List<ScrapPost> scrapPosts = scrapPostRepository.findByPostId(post.getId());
                List<MessageRoom> messageRooms = messageRoomRepository.findByPostId(post.getId());
                List<String> tagNamesOfPost = new ArrayList<>();
                for (PostTag postTag : postTags) {
                    tagNamesOfPost.add(postTag.getTag().getName());
                }
                PostInfoDtoWithTag responseEach = post.toInfoDtoWithTag(tagNamesOfPost, (long) scrapPosts.size(), (long) messageRooms.size());
                response.add(responseEach);
            }
        return response;
    }

    @Transactional(readOnly = true)
    public Page<PostInfoDtoWithTag> findPostsByCategoryAndPriceRange(SearchPostRequestExcludeTag searchPostRequestExcludeTag, Pageable pageable) {
        // 조회에서는 오류가 발생할 여지가 없다.
        // 조건이 잘못 전달될 경우 queryDSL에서 조건이 무시되어 반영된다.
        Page<Post> postPages = postCustomRepository.findPostsByCondition(searchPostRequestExcludeTag, pageable);
        List<PostInfoDtoWithTag> dtoList = new ArrayList<>();
        for (Post post : postPages) {
            List<PostTag> postTags = postTagRepository.findByPost(post);
            List<ScrapPost> scrapPosts = scrapPostRepository.findByPostId(post.getId());
            List<MessageRoom> messageRooms = messageRoomRepository.findByPostId(post.getId());
            List<String> tagNamesOfPost = new ArrayList<>();
            for (PostTag postTag : postTags) {
                tagNamesOfPost.add(postTag.getTag().getName());
            }
            dtoList.add(post.toInfoDtoWithTag(tagNamesOfPost, (long) scrapPosts.size(), (long) messageRooms.size()));
        }
        return new PageImpl<>(dtoList);
    }

    // TODO 테스트 예정
    // 테스트 완료하면 기존의 조회 로직 삭제 예정
    public Page<PostInfoDtoWithTag> findPostsWithTag(List<String> tagNames, Pageable pageable) {
        List<PostInfoDtoWithTag> dtoList = new ArrayList<>();
        List<Post> beforeConvertToDto = new ArrayList<>();
        if(tagNames.isEmpty()) {
            Page<Post> postPages = postRepository.findAll(pageable);
            for (Post post : postPages) {
                List<PostTag> postTags = postTagRepository.findByPost(post);
                List<ScrapPost> scrapPosts = scrapPostRepository.findByPostId(post.getId());
                List<MessageRoom> messageRooms = messageRoomRepository.findByPostId(post.getId());
                List<String> tagNamesOfPost = new ArrayList<>();
                for (PostTag postTag : postTags) {
                    tagNamesOfPost.add(postTag.getTag().getName());
                }
                PostInfoDtoWithTag responseEach = post.toInfoDtoWithTag(tagNamesOfPost, (long) scrapPosts.size(), (long) messageRooms.size());
                dtoList.add(responseEach);
            }
        } else {
            List<PostTag> totalPostTagList = new ArrayList<>();
            for(int i=0; i<tagNames.size(); i++) {
                List<PostTag> postTags = postTagRepository.findByTagName(tagNames.get(i));
                Page<Post> posts = postRepository.findByPostTagsIn(postTags, pageable);
                List<Post> tempPostList = posts.getContent();
                if (beforeConvertToDto.isEmpty()) {
                    beforeConvertToDto.addAll(tempPostList);
                } else {
                    beforeConvertToDto.retainAll(tempPostList);
                }
            }
            for (Post post : beforeConvertToDto) {
                List<PostTag> postTags = postTagRepository.findByPost(post);
                List<ScrapPost> scrapPosts = scrapPostRepository.findByPostId(post.getId());
                List<MessageRoom> messageRooms = messageRoomRepository.findByPostId(post.getId());
                List<String> tagNamesOfPost = new ArrayList<>();
                for (PostTag postTag : postTags) {
                    tagNamesOfPost.add(postTag.getTag().getName());
                }
                PostInfoDtoWithTag responseEach = post.toInfoDtoWithTag(tagNamesOfPost, (long) scrapPosts.size(), (long) messageRooms.size());
                dtoList.add(responseEach);
            }
        }
        return new PageImpl<>(dtoList);
    }

    public Page<PostInfoDtoForGET> findPostsWithTagNameList(List<String> tagNames, Pageable pageable) {
        Page<PostInfoDtoForGET> dtoList = postCustomRepository.findPostsByTags(tagNames, pageable);
        return dtoList;
    }

    // TODO 거래 가능한 게시글만 검색하기

    @Transactional(readOnly = true)
    public SinglePostInfoDto findPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(NotFoundByIdException::new);
//        // TODO 거래 가능 상태인지 확인하기
        List<String> tagNames = new ArrayList<>();
        List<PostTag> postTags = postTagRepository.findByPost(post);
        for (PostTag postTag : postTags) {
            tagNames.add(postTag.getTag().getName());
        }
        Scrap scrap = scrapRepository.findScrapByMemberId(post.getAuthor().getId()).orElseThrow(UserScrapNotFoundException::new);
        List<Comment> comments = commentRepository.findByPostId(post.getId());
        List<CommentInfoDto> commentInfoDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentInfoDtoList.add(comment.toInfoDto());
        }
        List<MessageRoom> messageRooms = messageRoomRepository.findByPostId(post.getId());
        List<ScrapPost> scrapPosts = scrapPostRepository.findByPostId(post.getId());
        UserInfo userInfo = post.getAuthor().toUserInfo(scrap.getId());
        SinglePostInfoDto response = post.toSinglePostInfoDto(tagNames, (long) scrapPosts.size(), (long) messageRooms.size(), userInfo, commentInfoDtoList);
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
        List<ScrapPost> scrapPosts = scrapPostRepository.findByPostId(post.getId());
        List<MessageRoom> messageRooms = messageRoomRepository.findByPostId(post.getId());
        PostInfoDtoWithTag response = post.toInfoDtoWithTag(tagNames, (long) scrapPosts.size(), (long) messageRooms.size());
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
