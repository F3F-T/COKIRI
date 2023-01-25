package f3f.dev1.domain.post.application;

import f3f.dev1.domain.category.dao.CategoryRepository;
import f3f.dev1.domain.category.exception.NotFoundProductCategoryNameException;
import f3f.dev1.domain.category.exception.NotFoundWishCategoryNameException;
import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.comment.dao.CommentRepository;
import f3f.dev1.domain.comment.model.Comment;
import f3f.dev1.domain.member.dao.MemberCustomRepositoryImpl;
import f3f.dev1.domain.member.exception.NotAuthorizedException;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.message.dao.MessageRoomRepository;
import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.post.dao.PostCustomRepositoryImpl;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.exception.NotContainAuthorInfoException;
import f3f.dev1.domain.postImage.dao.PostImageCustomRepositoryImpl;
import f3f.dev1.domain.postImage.model.PostImage;
import f3f.dev1.domain.scrap.dao.ScrapPostRepository;
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
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.comment.dto.CommentDTO.*;
import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.post.dto.PostDTO.*;
import static f3f.dev1.domain.postImage.dto.PostImageDTO.*;
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
    private final MemberCustomRepositoryImpl memberCustomRepository;
    private final PostImageCustomRepositoryImpl postImageCustomRepository;

    // TODO 게시글 사진 개수 제한 걸기
    @Transactional
    public Long savePost(PostSaveRequest postSaveRequest, Long currentMemberId) {

        Member member = memberRepository.findById(postSaveRequest.getAuthorId()).orElseThrow(NotFoundByIdException::new);
        List<PostTag> resultsList = new ArrayList<>();
        Category productCategory = categoryRepository.findCategoryByName(postSaveRequest.getProductCategory()).orElseThrow(NotFoundProductCategoryNameException::new);
        Category wishCategory = categoryRepository.findCategoryByName(postSaveRequest.getWishCategory()).orElseThrow(NotFoundWishCategoryNameException::new);
        memberRepository.findById(currentMemberId).orElseThrow(NotFoundByIdException::new);


        Post post = postSaveRequest.toEntity(member, productCategory, wishCategory, resultsList);
        member.getPosts().add(post);
        postRepository.save(post);
        Trade trade = CreateTradeDto.builder().sellerId(member.getId()).postId(post.getId()).build().toEntity(member, post);
        tradeRepository.save(trade);

        return post.getId();
    }

    @Transactional(readOnly = true)
    public Page<PostSearchResponseDto> findAll(Long currentMemberId, Pageable pageable) {
        Page<Post> all = postRepository.findAll(pageable);
        List<PostSearchResponseDto> resultList = new ArrayList<>();
        if(currentMemberId != null) {
            Member member = memberRepository.findById(currentMemberId).orElseThrow(NotFoundByIdException::new);
            for (Post post : all) {
                boolean isScrap = scrapPostRepository.existsByScrapIdAndPostId(member.getScrap().getId(), post.getId());
                resultList.add(post.toSearchResponseDto((long)post.getMessageRooms().size(), (long)post.getScrapPosts().size(),isScrap));
            }
        } else {
            for (Post post : all) {
                resultList.add(post.toSearchResponseDto((long) post.getMessageRooms().size(), (long) post.getScrapPosts().size(), false));
            }
        }

        return new PageImpl<>(resultList);
    }


    // findByIdPostListDTO는 검색된 포스트 리스트를 가지고 있는 DTO이다.
    // controller에서 사용되지 않는 로직. 현재 테스트에서만 사용되고 있다.
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



    @Transactional(readOnly = true)
    public Page<PostSearchResponseDto> findPostsByCategoryAndPriceRange(SearchPostRequestExcludeTag searchPostRequestExcludeTag, Long currentMemberId, Pageable pageable) {
        List<PostSearchResponseDto> list = new ArrayList<>();
        Page<Post> dtoPages = postCustomRepository.findPostDTOByConditions(searchPostRequestExcludeTag, pageable);
        // 조회하는 사용자가 로그인된 회원인 경우
        if(currentMemberId != null) {
            Member member = memberRepository.findById(currentMemberId).orElseThrow(NotFoundByIdException::new);
            for (Post post : dtoPages) {
                // 캐싱 적용하기 전에는 이게 최선이다..
                boolean isScrap = scrapPostRepository.existsByScrapIdAndPostId(member.getScrap().getId(), post.getId());
                PostSearchResponseDto build = post.toSearchResponseDto((long)post.getMessageRooms().size(), (long)post.getScrapPosts().size(), isScrap);
                list.add(build);
            }
        }
        // 조회하는 사용자가 비회원일 경우
        else {
            for (Post post : dtoPages) {
                PostSearchResponseDto build = post.toSearchResponseDto((long)post.getMessageRooms().size(), (long)post.getScrapPosts().size(), false);
                list.add(build);
            }
        }
        return new PageImpl<>(list);
    }


    @Transactional(readOnly = true)
    public Page<PostSearchResponseDto> findPostsWithTagNameList(List<String> tagNames, Long currentMemberId, Pageable pageable) {
        Page<Post> dtoList = postCustomRepository.findPostsByTags(tagNames, pageable);
        List<PostSearchResponseDto> resultList = new ArrayList<>();
        if(currentMemberId != null) {
            Member member = memberRepository.findById(currentMemberId).orElseThrow(NotFoundByIdException::new);
            for (Post post : dtoList) {
                boolean isScrap = scrapPostRepository.existsByScrapIdAndPostId(member.getScrap().getId(), post.getId());
                PostSearchResponseDto build = post.toSearchResponseDto((long)post.getMessageRooms().size(), (long)post.getScrapPosts().size(), isScrap);
                resultList.add(build);
            }
        } else {
            for (Post post : dtoList) {
                PostSearchResponseDto build = post.toSearchResponseDto((long)post.getMessageRooms().size(), (long)post.getScrapPosts().size(), false);
                resultList.add(build);
            }
        }
        return new PageImpl<>(resultList);
    }

    // TODO 거래 가능한 게시글만 검색하기

    @Transactional(readOnly = true)
    public SinglePostInfoDto findPostById(Long id, Long currentMemberId) {
        Post post = postRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        // TODO 거래 가능 상태인지 확인하기
        List<String> tagNames = new ArrayList<>();
        List<PostTag> postTags = postTagRepository.findByPost(post);
        for (PostTag postTag : postTags) {
            tagNames.add(postTag.getTag().getName());
        }

        List<Comment> comments = commentRepository.findByPostId(post.getId());
        List<CommentInfoDto> commentInfoDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentInfoDtoList.add(comment.toInfoDto());
        }

        UserInfoWithAddress userInfo = memberCustomRepository.getUserInfo(post.getAuthor().getId());
        List<String> postImages = new ArrayList<>();
        for (PostImage postImage : post.getPostImages()) {
            postImages.add(postImage.getImgPath());
        }

        // 어쩔 수 없이 세션에서 받아온 현재 유저의 엔티티를 찾는 쿼리를 한번 날려야겠다.
        Member member = memberRepository.findById(currentMemberId).orElseThrow(NotFoundByIdException::new);
        boolean isScrap = scrapPostRepository.existsByScrapIdAndPostId(member.getScrap().getId(), id);
        SinglePostInfoDto response = post.toSinglePostInfoDto(tagNames, (long) post.getScrapPosts().size(), (long) post.getMessageRooms().size(), userInfo, commentInfoDtoList, postImages, isScrap);
        return response;
    }

    /* TODO
        U : 게시글 업데이트
     */

    @Transactional
    // 현재는 컨트롤러에서 사용하지 않는 로직. 테스트에서만 사용하고 있다.
    public PostInfoDtoWithTag updatePost(UpdatePostRequest updatePostRequest, Long postId,Long currentMemberId) {

        /*
            TODO
             이미지 변경 부분이 없어서 찾아보다가 사고가 났다.
             지금 구현한건 PUT이고 PATCH로 구현해야하는데, 그럼 로직이 달라진다.
         */
        Post post = postRepository.findById(postId).orElseThrow(NotFoundByIdException::new);
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
        PostInfoDtoWithTag response = post.toInfoDtoWithTag(tagNames, (long) post.getScrapPosts().size(), (long) post.getMessageRooms().size());
        return response;
    }

    @Transactional
    public void updatePostWithPatch(UpdatePostRequest updatePostRequest, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundByIdException::new);

        if(updatePostRequest.getAuthorId() == null) {
            throw new NotContainAuthorInfoException();
        }

        if(!post.getAuthor().getId().equals(updatePostRequest.getAuthorId())) {
            throw new NotMatchingAuthorException("게시글 작성자가 아닙니다.");
        }

        /*
            프론트와 의견 상충 과정에서 아래와 같은 코드 구조가 탄생함.
            프론트는 특정 필드의 변경사항을 인지하고 바디에 추가하는게 소요가 크다고 했고,
            나는 불필요한 쿼리의 수를 최대한 줄이고싶었다.
            그래서 결과적으로 PUT도 PATCH도 아닌 업데이트 메서드가 구현됨 :
            수정된 필드 뿐만 아니라 수정되지 않은 필드로 함께 바디로 넘어온다.
            하지만 그 중에서 변동사항이 있는 값만 수정된다.

            이렇게 여러번 쿼리를 별도로 날리는 것 보다 한번에 일괄로 처리하는게 나으려나?
         */

        if(updatePostRequest.getTitle() != null && !post.getTitle().equals(updatePostRequest.getTitle())) {
            post.updateTitle(updatePostRequest.getTitle());
        }

        if(updatePostRequest.getPrice() != null && !post.getPrice().equals(updatePostRequest.getPrice())) {
            post.updatePrice(updatePostRequest.getPrice());
        }

        if(updatePostRequest.getProductCategory() != null && !post.getProductCategory().getName().equals(updatePostRequest.getProductCategory())) {
            Category productCategory = categoryRepository.findCategoryByName(updatePostRequest.getProductCategory()).orElseThrow(NotFoundByIdException::new);
            post.updateProductCategory(productCategory);
        }

        if(updatePostRequest.getWishCategory() != null && !post.getWishCategory().getName().equals(updatePostRequest.getWishCategory())) {
            Category wishCategory = categoryRepository.findCategoryByName(updatePostRequest.getWishCategory()).orElseThrow(NotFoundByIdException::new);
            post.updateWishCategory(wishCategory);
        }

        if(updatePostRequest.getThumbnail() != null && !post.getThumbnailImgPath().equals(updatePostRequest.getThumbnail())) {
            String thumbnail = updatePostRequest.getThumbnail();
            post.updateThumbnail(thumbnail);
        }

        //의성 추가
        if(updatePostRequest.getPrice() != null && !post.getPrice().equals(updatePostRequest.getPrice())) {
            post.updatePrice(updatePostRequest.getPrice());
        }
        //의성 추가
        if(updatePostRequest.getContent() != null && !post.getContent().equals(updatePostRequest.getContent())) {
            post.updateContent(updatePostRequest.getContent());
        }

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

        postRepository.delete(post);
        return "DELETE";
    }

}
