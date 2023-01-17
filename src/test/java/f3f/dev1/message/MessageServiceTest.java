package f3f.dev1.message;

import f3f.dev1.domain.category.application.CategoryService;
import f3f.dev1.domain.category.dao.CategoryRepository;
import f3f.dev1.domain.category.dto.CategoryDTO;
import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.application.EmailCertificationService;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.dto.MemberDTO;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.message.application.MessageRoomService;
import f3f.dev1.domain.message.application.MessageService;
import f3f.dev1.domain.message.dao.MessageRepository;
import f3f.dev1.domain.message.dao.MessageRoomRepository;
import f3f.dev1.domain.message.dto.MessageDTO;
import f3f.dev1.domain.message.dto.MessageRoomDTO;
import f3f.dev1.domain.message.exception.CanNotDeleteMessage;
import f3f.dev1.domain.message.exception.CanNotSendMessageByTradeStatus;
import f3f.dev1.domain.message.exception.MessageException;
import f3f.dev1.domain.message.model.Message;
import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.address.model.Address;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.dto.PostDTO;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import f3f.dev1.domain.tag.dao.TagRepository;
import f3f.dev1.domain.trade.application.TradeService;
import f3f.dev1.domain.trade.dao.TradeRepository;
import f3f.dev1.domain.trade.dto.TradeDTO;
import f3f.dev1.domain.trade.model.Trade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class MessageServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostService postService;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    MessageService messageService;
    @Autowired
    MessageRoomRepository messageRoomRepository;
    @Autowired
    MessageRoomService messageRoomService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryService categoryService;
    @Autowired
    TradeRepository tradeRepository;
    @Autowired
    TradeService tradeService;
    @Autowired
    ScrapRepository scrapRepository;
    @Autowired
    TagRepository tagRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    AuthService authService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    EmailCertificationService emailCertificationService;


    @BeforeEach
    public void deleteAll() {
        memberRepository.deleteAll();
        postRepository.deleteAll();
        messageRepository.deleteAll();
        categoryRepository.deleteAll();
        tradeRepository.deleteAll();
        scrapRepository.deleteAll();
        tradeRepository.deleteAll();
    }


    // 주소 오브젝트 생성
    public Address createAddress() {
        return Address.builder()
                .addressName("address")
                .postalAddress("13556")
                .latitude("37.49455")
                .longitude("127.12170")
                .build();
    }

    // 회원가입 DTO 생성 메소드
    public MemberDTO.SignUpRequest createSignUpRequest1() {
        return MemberDTO.SignUpRequest.builder()
                .userName("user1")
                .nickname("admin")
                .phoneNumber("01012345678")
                .email("userEmail@email.com")
                .birthDate("990128")
                .password("password")
                .build();
    }
    public MemberDTO.SignUpRequest createSignUpRequest2() {
        return MemberDTO.SignUpRequest.builder()
                .userName("user2")
                .nickname("nickname")
                .phoneNumber("01011112222")
                .email("user2Email@email.com")
                .birthDate("990127")
                .password("password")
                .build();
    }
    public MemberDTO.SignUpRequest createSignUpRequest3() {
        return MemberDTO.SignUpRequest.builder()
                .userName("user3")
                .nickname("user3ya")
                .phoneNumber("01011113333")
                .email("user3Email@email.com")
                .birthDate("990126")
                .password("password")
                .build();
    }

    // 로그인 DTO 생성 메소드
    public MemberDTO.LoginRequest createLoginRequest1() {
        return MemberDTO.LoginRequest.builder()
                .email("userEmail@email.com")
                .password("password").build();
    }
    public MemberDTO.LoginRequest createLoginRequest2() {
        return MemberDTO.LoginRequest.builder()
                .email("user2Email@email.com")
                .password("password").build();
    }
    public MemberDTO.LoginRequest createLoginRequest3() {
        return MemberDTO.LoginRequest.builder()
                .email("user3Email@email.com")
                .password("password").build();
    }

    // 업데이트 DTO 생성 메소드
    public MemberDTO.UpdateUserInfo createUpdateRequest() {
        return MemberDTO.UpdateUserInfo.builder()
                .address(createAddress())
                .nickname("newNickname")
                .phoneNumber("01088888888")
                .build();
    }
    public PostDTO.PostSaveRequest createPostSaveRequest(Member author, boolean tradeEachOther, String productName, String wishName) {
        return PostDTO.PostSaveRequest.builder()
                .content("냄새가 조금 나긴 하는데 뭐 그럭저럭 괜찮아요")
                .title("3년 신은 양말 거래 희망합니다")
                .tradeEachOther(tradeEachOther)
                .authorId(author.getId())
                .productCategory(productName)
                .tagNames(new ArrayList<>())
                .wishCategory(wishName)
                .build();
    }

    public TradeDTO.CreateTradeDto createTradeDto(Long sellerId, Long postId) {
        return TradeDTO.CreateTradeDto.builder()
                .sellerId(sellerId)
                .postId(postId).build();
    }

    private CategoryDTO.CategorySaveRequest createCategoryDto(String name, Long memberId, Long depth, Long parentId) {
        CategoryDTO.CategorySaveRequest saveRequest = new CategoryDTO.CategorySaveRequest(name, memberId, depth, parentId);
        return saveRequest;
    }

    private MessageRoomDTO.MessageRoomSaveRequest messageRoomSaveRequest (Long postId, Long buyerId){
        MessageRoomDTO.MessageRoomSaveRequest saveRequest = new MessageRoomDTO.MessageRoomSaveRequest(postId, buyerId);
        return saveRequest;
    }
    private MessageDTO.MessageSaveRequest messageSaveRequest(String content, Long senderId, Long receiverId, Long postId, Long messageRoomId){
        MessageDTO.MessageSaveRequest saveRequest = new MessageDTO.MessageSaveRequest(content, senderId, receiverId, postId, messageRoomId);
        return saveRequest;
    }
    private MessageDTO.DeleteMessageRequest deleteMessageRequest(Long id,Long memberId, Long messageRoomId){
        MessageDTO.DeleteMessageRequest deleteRequest = new MessageDTO.DeleteMessageRequest(id,memberId, messageRoomId);
        return deleteRequest;
    }
    @Test
    @DisplayName("메시지 생성 테스트")
    public void createMessageTest() throws Exception{
        MemberDTO.SignUpRequest signUpRequest1 = createSignUpRequest1();
        authService.signUp(signUpRequest1);
        Member admin = memberRepository.findByEmail(signUpRequest1.getEmail()).get();
        MemberDTO.SignUpRequest signUpRequest2 = createSignUpRequest2();
        authService.signUp(signUpRequest2);
        Member user = memberRepository.findByEmail(signUpRequest2.getEmail()).get();
        MemberDTO.SignUpRequest signUpRequest3 = createSignUpRequest3();
        authService.signUp(signUpRequest3);
        Member user2 = memberRepository.findByEmail(signUpRequest3.getEmail()).get();

        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
        Long cid1 = categoryService.createCategory(categoryDTO1);
        Category category1 = categoryRepository.findById(cid1).get();
        Category root = categoryRepository.findCategoryByName("root").get();
        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
        Long cid2 = categoryService.createCategory(categoryDTO2);
        Category category2 = categoryRepository.findById(cid2).get();
        PostDTO.PostSaveRequest postSaveRequest = createPostSaveRequest(admin, true, "도서", "도서");
        Long postId = postService.savePost(postSaveRequest, admin.getId());
        Post post = postRepository.findById(postId).get();
        MessageRoomDTO.MessageRoomSaveRequest msgRoomDTO1 = messageRoomSaveRequest(post.getId(), user.getId());
        Long msgRoomId1 = messageRoomService.createMessageRoom(msgRoomDTO1, user.getId()).getId();
        MessageRoom msgRoom1 = messageRoomRepository.findById(msgRoomId1).get();
        //when
        MessageDTO.MessageSaveRequest messageDTO1 = messageSaveRequest("저기요 물건 교환 하고 싶어요", user.getId(),msgRoom1.getSeller().getId(), post.getId(), msgRoom1.getId());
//        assertThat()
        Long messageId1 = messageService.createMessage(messageDTO1, user.getId()).getId();
        Message message1 = messageRepository.findById(messageId1).get();
        //then
        assertThat(messageRepository.existsById(messageId1));

    }
    @Test
    @DisplayName("(실패)메시지 생성 실패 테스트 : 거래 완료")
    public void createTradedPostMessageTest() throws Exception{
        MemberDTO.SignUpRequest signUpRequest1 = createSignUpRequest1();
        authService.signUp(signUpRequest1);
        Member admin = memberRepository.findByEmail(signUpRequest1.getEmail()).get();
        MemberDTO.SignUpRequest signUpRequest2 = createSignUpRequest2();
        authService.signUp(signUpRequest2);
        Member user = memberRepository.findByEmail(signUpRequest2.getEmail()).get();
        MemberDTO.SignUpRequest signUpRequest3 = createSignUpRequest3();
        authService.signUp(signUpRequest3);
        Member user2 = memberRepository.findByEmail(signUpRequest3.getEmail()).get();

        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
        Long cid1 = categoryService.createCategory(categoryDTO1);
        Category category1 = categoryRepository.findById(cid1).get();
        Category root = categoryRepository.findCategoryByName("root").get();
        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
        Long cid2 = categoryService.createCategory(categoryDTO2);
        Category category2 = categoryRepository.findById(cid2).get();
        PostDTO.PostSaveRequest postSaveRequest = createPostSaveRequest(admin, true, "도서", "도서");
        Long postId = postService.savePost(postSaveRequest, admin.getId());
        Post post = postRepository.findById(postId).get();
        MessageRoomDTO.MessageRoomSaveRequest msgRoomDTO1 = messageRoomSaveRequest(post.getId(), user.getId());
        Long msgRoomId1 = messageRoomService.createMessageRoom(msgRoomDTO1, user.getId()).getId();
        MessageRoom msgRoom1 = messageRoomRepository.findById(msgRoomId1).get();
        //when
        MessageDTO.MessageSaveRequest messageDTO1 = messageSaveRequest("저기요 물건 교환 하고 싶어요", user.getId(), post.getAuthor().getId(), post.getId(), msgRoom1.getId());
        Trade trade = tradeRepository.findByPostId(postId).get();
        TradeDTO.UpdateTradeDto updateTradeDto = TradeDTO.UpdateTradeDto.builder().postId(postId).userId(admin.getId()).tradeStatus(TradeStatus.TRADED).build();
        tradeService.updateTradeStatus(updateTradeDto, admin.getId());
        //then
        assertThat(trade.getTradeStatus()).isEqualTo(TradeStatus.TRADED);
        Assertions.assertThrows(CanNotSendMessageByTradeStatus.class,()->{
            Long messageId1 = messageService.createMessage(messageDTO1, user.getId()).getId();
        });
    }
    @Test
    @DisplayName("(실패)메시지 생성 실패 테스트 : 본인에게 메시지")
    public void createOneselfMessageTest() throws Exception{
        MemberDTO.SignUpRequest signUpRequest1 = createSignUpRequest1();
        authService.signUp(signUpRequest1);
        Member admin = memberRepository.findByEmail(signUpRequest1.getEmail()).get();
        MemberDTO.SignUpRequest signUpRequest2 = createSignUpRequest2();
        authService.signUp(signUpRequest2);
        Member user = memberRepository.findByEmail(signUpRequest2.getEmail()).get();
        MemberDTO.SignUpRequest signUpRequest3 = createSignUpRequest3();
        authService.signUp(signUpRequest3);
        Member user2 = memberRepository.findByEmail(signUpRequest3.getEmail()).get();

        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
        Long cid1 = categoryService.createCategory(categoryDTO1);
        Category category1 = categoryRepository.findById(cid1).get();
        Category root = categoryRepository.findCategoryByName("root").get();
        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
        Long cid2 = categoryService.createCategory(categoryDTO2);
        Category category2 = categoryRepository.findById(cid2).get();
        PostDTO.PostSaveRequest postSaveRequest = createPostSaveRequest(admin, true, "도서", "도서");
        Long postId = postService.savePost(postSaveRequest, admin.getId());
        Post post = postRepository.findById(postId).get();
        MessageRoomDTO.MessageRoomSaveRequest msgRoomDTO1 = messageRoomSaveRequest(post.getId(), user.getId());
        Long msgRoomId1 = messageRoomService.createMessageRoom(msgRoomDTO1, user.getId()).getId();
        MessageRoom msgRoom1 = messageRoomRepository.findById(msgRoomId1).get();
        //when

        MessageDTO.MessageSaveRequest messageDTO1 = messageSaveRequest("저기요 물건 교환 하고 싶어요", user.getId(), user.getId(), post.getId(), msgRoom1.getId());
        //then
        Assertions.assertThrows(MessageException.class,()->{
            Long messageId1 = messageService.createMessage(messageDTO1, user.getId()).getId();
        });
    }
    //--------------------------------------------메시지 삭제 -------------------------------------------------------------
    @Test
    @DisplayName("메시지 삭제 테스트")
    public void deleteMessageTest() throws Exception {
        MemberDTO.SignUpRequest signUpRequest1 = createSignUpRequest1();
        authService.signUp(signUpRequest1);
        Member admin = memberRepository.findByEmail(signUpRequest1.getEmail()).get();
        MemberDTO.SignUpRequest signUpRequest2 = createSignUpRequest2();
        authService.signUp(signUpRequest2);
        Member user = memberRepository.findByEmail(signUpRequest2.getEmail()).get();
        MemberDTO.SignUpRequest signUpRequest3 = createSignUpRequest3();
        authService.signUp(signUpRequest3);
        Member user2 = memberRepository.findByEmail(signUpRequest3.getEmail()).get();

        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
        Long cid1 = categoryService.createCategory(categoryDTO1);
        Category category1 = categoryRepository.findById(cid1).get();
        Category root = categoryRepository.findCategoryByName("root").get();
        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
        Long cid2 = categoryService.createCategory(categoryDTO2);
        Category category2 = categoryRepository.findById(cid2).get();
        PostDTO.PostSaveRequest postSaveRequest = createPostSaveRequest(admin, true, "도서", "도서");
        Long postId = postService.savePost(postSaveRequest, admin.getId());
        Post post = postRepository.findById(postId).get();
        MessageRoomDTO.MessageRoomSaveRequest msgRoomDTO1 = messageRoomSaveRequest(post.getId(), user.getId());
        Long msgRoomId1 = messageRoomService.createMessageRoom(msgRoomDTO1, user.getId()).getId();
        MessageRoom msgRoom1 = messageRoomRepository.findById(msgRoomId1).get();
        MessageDTO.MessageSaveRequest messageDTO1 = messageSaveRequest("저기요 물건 교환 하고 싶어요", user.getId(), post.getAuthor().getId(), post.getId(), msgRoom1.getId());
        Long messageId1 = messageService.createMessage(messageDTO1, user.getId()).getId();
        MessageDTO.MessageSaveRequest messageDTO2 = messageSaveRequest("어떠세요?", user.getId(), post.getAuthor().getId(), post.getId(), msgRoom1.getId());
        Long messageId2 = messageService.createMessage(messageDTO2, user.getId()).getId();
        MessageDTO.MessageSaveRequest messageDTO3 = messageSaveRequest("잠시만요", admin.getId(), user.getId(), post.getId(), msgRoom1.getId());
        Long messageId3 = messageService.createMessage(messageDTO3, admin.getId()).getId();
        //when
        assertThat(msgRoom1.getMessages().size()).isEqualTo(3);
        MessageDTO.DeleteMessageRequest delMessageDTO1 = deleteMessageRequest(messageId2, user.getId(),msgRoomId1);
        messageService.deleteMessage(delMessageDTO1, user.getId());
        //then
        assertThat(messageRepository.existsById(messageId2)).isEqualTo(false);
    }

    @Test
    @DisplayName("(실패)메시지 삭제 테스트 : 본인 아님")
    public void deleteMessageByWrongUserTest() throws Exception {
        MemberDTO.SignUpRequest signUpRequest1 = createSignUpRequest1();
        authService.signUp(signUpRequest1);
        Member admin = memberRepository.findByEmail(signUpRequest1.getEmail()).get();
        MemberDTO.SignUpRequest signUpRequest2 = createSignUpRequest2();
        authService.signUp(signUpRequest2);
        Member user = memberRepository.findByEmail(signUpRequest2.getEmail()).get();
        MemberDTO.SignUpRequest signUpRequest3 = createSignUpRequest3();
        authService.signUp(signUpRequest3);
        Member user2 = memberRepository.findByEmail(signUpRequest3.getEmail()).get();

        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
        Long cid1 = categoryService.createCategory(categoryDTO1);
        Category category1 = categoryRepository.findById(cid1).get();
        Category root = categoryRepository.findCategoryByName("root").get();
        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
        Long cid2 = categoryService.createCategory(categoryDTO2);
        Category category2 = categoryRepository.findById(cid2).get();
        PostDTO.PostSaveRequest postSaveRequest = createPostSaveRequest(admin, true, "도서", "도서");
        Long postId = postService.savePost(postSaveRequest, admin.getId());
        Post post = postRepository.findById(postId).get();
        MessageRoomDTO.MessageRoomSaveRequest msgRoomDTO1 = messageRoomSaveRequest(post.getId(), user.getId());
        Long msgRoomId1 = messageRoomService.createMessageRoom(msgRoomDTO1, user.getId()).getId();
        MessageRoom msgRoom1 = messageRoomRepository.findById(msgRoomId1).get();
        MessageDTO.MessageSaveRequest messageDTO1 = messageSaveRequest("저기요 물건 교환 하고 싶어요", user.getId(), post.getAuthor().getId(), post.getId(), msgRoom1.getId());
        Long messageId1 = messageService.createMessage(messageDTO1,user.getId()).getId();
        MessageDTO.MessageSaveRequest messageDTO2 = messageSaveRequest("어떠세요?", user.getId(), post.getAuthor().getId(), post.getId(), msgRoom1.getId());
        Long messageId2 = messageService.createMessage(messageDTO2, user.getId()).getId();
        MessageDTO.MessageSaveRequest messageDTO3 = messageSaveRequest("잠시만요", admin.getId(), user.getId(), post.getId(), msgRoom1.getId());
        Long messageId3 = messageService.createMessage(messageDTO3, admin.getId()).getId();
        //when
        assertThat(msgRoom1.getMessages().size()).isEqualTo(3);
        MessageDTO.DeleteMessageRequest delMessageDTO1 = deleteMessageRequest(messageId2, admin.getId(),msgRoomId1);
        //then
        Assertions.assertThrows(CanNotDeleteMessage.class, ()->{
            messageService.deleteMessage(delMessageDTO1, admin.getId());
        });
    }
    @Test
    @DisplayName("(실패)메시지 삭제 테스트 : 거래 완료")
    public void deleteTradedPostMessageTest() throws Exception {
        MemberDTO.SignUpRequest signUpRequest1 = createSignUpRequest1();
        authService.signUp(signUpRequest1);
        Member admin = memberRepository.findByEmail(signUpRequest1.getEmail()).get();
        MemberDTO.SignUpRequest signUpRequest2 = createSignUpRequest2();
        authService.signUp(signUpRequest2);
        Member user = memberRepository.findByEmail(signUpRequest2.getEmail()).get();
        MemberDTO.SignUpRequest signUpRequest3 = createSignUpRequest3();
        authService.signUp(signUpRequest3);
        Member user2 = memberRepository.findByEmail(signUpRequest3.getEmail()).get();

        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
        Long cid1 = categoryService.createCategory(categoryDTO1);
        Category category1 = categoryRepository.findById(cid1).get();
        Category root = categoryRepository.findCategoryByName("root").get();
        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
        Long cid2 = categoryService.createCategory(categoryDTO2);
        Category category2 = categoryRepository.findById(cid2).get();
        PostDTO.PostSaveRequest postSaveRequest = createPostSaveRequest(admin, true, "도서", "도서");
        Long postId = postService.savePost(postSaveRequest, admin.getId());
        Post post = postRepository.findById(postId).get();
        MessageRoomDTO.MessageRoomSaveRequest msgRoomDTO1 = messageRoomSaveRequest(post.getId(), user.getId());
        Long msgRoomId1 = messageRoomService.createMessageRoom(msgRoomDTO1, user.getId()).getId();
        MessageRoom msgRoom1 = messageRoomRepository.findById(msgRoomId1).get();
        MessageDTO.MessageSaveRequest messageDTO1 = messageSaveRequest("저기요 물건 교환 하고 싶어요", user.getId(), post.getAuthor().getId(), post.getId(), msgRoom1.getId());
        Long messageId1 = messageService.createMessage(messageDTO1, user.getId()).getId();
        MessageDTO.MessageSaveRequest messageDTO2 = messageSaveRequest("어떠세요?", user.getId(), post.getAuthor().getId(), post.getId(), msgRoom1.getId());
        Long messageId2 = messageService.createMessage(messageDTO2, user.getId()).getId();
        MessageDTO.MessageSaveRequest messageDTO3 = messageSaveRequest("잠시만요", admin.getId(), user.getId(), post.getId(), msgRoom1.getId());
        Long messageId3 = messageService.createMessage(messageDTO3, admin.getId()).getId();
        //when
        assertThat(msgRoom1.getMessages().size()).isEqualTo(3);
        MessageDTO.DeleteMessageRequest delMessageDTO1 = deleteMessageRequest(messageId2, admin.getId(),msgRoomId1);
        Trade trade = tradeRepository.findByPostId(postId).get();
        TradeDTO.UpdateTradeDto updateTradeDto = TradeDTO.UpdateTradeDto.builder().postId(postId).userId(admin.getId()).tradeStatus(TradeStatus.TRADED).build();
        tradeService.updateTradeStatus(updateTradeDto, admin.getId());
        //then
        Assertions.assertThrows(CanNotDeleteMessage.class, ()->{
            messageService.deleteMessage(delMessageDTO1, admin.getId());
        });
    }

    }
