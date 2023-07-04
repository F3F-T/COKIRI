//package f3f.dev1.message;
//
//import f3f.dev1.domain.address.model.Address;
//import f3f.dev1.domain.category.application.CategoryService;
//import f3f.dev1.domain.category.dao.CategoryRepository;
//import f3f.dev1.domain.category.dto.CategoryDTO;
//import f3f.dev1.domain.category.model.Category;
//import f3f.dev1.domain.member.application.AuthService;
//import f3f.dev1.domain.member.application.EmailCertificationService;
//import f3f.dev1.domain.member.application.MemberService;
//import f3f.dev1.domain.member.dao.MemberRepository;
//import f3f.dev1.domain.member.dto.MemberDTO;
//import f3f.dev1.domain.member.model.Member;
//import f3f.dev1.domain.message.application.MessageRoomService;
//import f3f.dev1.domain.message.application.MessageService;
//import f3f.dev1.domain.message.dao.MessageRepository;
//import f3f.dev1.domain.message.dao.MessageRoomRepository;
//import f3f.dev1.domain.message.dto.MessageDTO;
//import f3f.dev1.domain.message.dto.MessageRoomDTO;
//import f3f.dev1.domain.message.exception.CanNotMakeMessegeRoom;
//import f3f.dev1.domain.message.exception.CanNotSendMessageByTradeStatus;
//import f3f.dev1.domain.message.model.MessageRoom;
//import f3f.dev1.domain.model.TradeStatus;
//import f3f.dev1.domain.post.application.PostService;
//import f3f.dev1.domain.post.dao.PostRepository;
//import f3f.dev1.domain.post.dto.PostDTO;
//import f3f.dev1.domain.post.model.Post;
//import f3f.dev1.domain.scrap.dao.ScrapRepository;
//import f3f.dev1.domain.tag.dao.TagRepository;
//import f3f.dev1.domain.trade.application.TradeService;
//import f3f.dev1.domain.trade.dao.TradeRepository;
//import f3f.dev1.domain.trade.dto.TradeDTO;
//import f3f.dev1.domain.trade.model.Trade;
//import io.netty.util.internal.UnstableApi;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.Assert;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static f3f.dev1.domain.message.dto.MessageDTO.*;
//import static f3f.dev1.domain.message.dto.MessageRoomDTO.*;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@Transactional
//@SpringBootTest
//public class MessageRoomServiceTest {
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    PostRepository postRepository;
//    @Autowired
//    MessageRepository messageRepository;
//    @Autowired
//    MessageService messageService;
//    @Autowired
//    MessageRoomRepository messageRoomRepository;
//    @Autowired
//    MessageRoomService messageRoomService;
//    @Autowired
//    CategoryRepository categoryRepository;
//    @Autowired
//    CategoryService categoryService;
//    @Autowired
//    TradeRepository tradeRepository;
//    @Autowired
//    TradeService tradeService;
//    @Autowired
//    ScrapRepository scrapRepository;
//    @Autowired
//    TagRepository tagRepository;
//    @Autowired
//    MemberService memberService;
//    @Autowired
//    PostService postService;
//
//    @Autowired
//    AuthService authService;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//
//    @Autowired
//    EmailCertificationService emailCertificationService;
//
//
//
//    @BeforeEach
//    public void deleteAll() {
//        messageRepository.deleteAll();
//        messageRoomRepository.deleteAll();
//        tradeRepository.deleteAll();
//        scrapRepository.deleteAll();
//        postRepository.deleteAll();
//        categoryRepository.deleteAll();
//        memberRepository.deleteAll();
//    }
//
//    // 주소 오브젝트 생성
//    public Address createAddress() {
//        return Address.builder()
//                .addressName("address")
//                .postalAddress("13556")
//                .latitude("37.49455")
//                .longitude("127.12170")
//                .build();
//    }
//
//
//    // 회원가입 DTO 생성 메소드
//    public MemberDTO.SignUpRequest createSignUpRequest1() {
//        return MemberDTO.SignUpRequest.builder()
//                .userName("user1")
//                .nickname("admin")
//                .phoneNumber("01012345678")
//                .email("userEmail@email.com")
//                .birthDate("990128")
//                .password("password")
//                .build();
//    }
//    public MemberDTO.SignUpRequest createSignUpRequest2() {
//        return MemberDTO.SignUpRequest.builder()
//                .userName("user2")
//                .nickname("nickname")
//                .phoneNumber("01011112222")
//                .email("user2Email@email.com")
//                .birthDate("990127")
//                .password("password")
//                .build();
//    }
//    public MemberDTO.SignUpRequest createSignUpRequest3() {
//        return MemberDTO.SignUpRequest.builder()
//                .userName("user3")
//                .nickname("user3ya")
//                .phoneNumber("01011113333")
//                .email("user3Email@email.com")
//                .birthDate("990126")
//                .password("password")
//                .build();
//    }
//
//    // 로그인 DTO 생성 메소드
//    public MemberDTO.LoginRequest createLoginRequest1() {
//        return MemberDTO.LoginRequest.builder()
//                .email("userEmail@email.com")
//                .password("password").build();
//    }
//    public MemberDTO.LoginRequest createLoginRequest2() {
//        return MemberDTO.LoginRequest.builder()
//                .email("user2Email@email.com")
//                .password("password").build();
//    }
//    public MemberDTO.LoginRequest createLoginRequest3() {
//        return MemberDTO.LoginRequest.builder()
//                .email("user3Email@email.com")
//                .password("password").build();
//    }
//
//    public PostDTO.PostSaveRequest createPostSaveRequest(Member author, boolean tradeEachOther, String productName, String wishName) {
//        return PostDTO.PostSaveRequest.builder()
//                .content("테스트용 게시글 본문")
//                .title("3년 신은 양말 거래 희망합니다")
//                .tradeEachOther(tradeEachOther)
//                .authorId(author.getId())
//                .productCategory(productName)
//                .tagNames(new ArrayList<>())
//                .wishCategory(wishName)
//                .build();
//    }
//    public PostDTO.PostSaveRequest createPostSaveRequest2(Member author, boolean tradeEachOther, String productName, String wishName) {
//        return PostDTO.PostSaveRequest.builder()
//                .content("읽다가 밤샘 심심하신분 읽으세요. 재미 감동 다 있음.")
//                .title("이 책 존잼 거래하실분")
//                .tradeEachOther(tradeEachOther)
//                .authorId(author.getId())
//                .productCategory(productName)
//                .tagNames(new ArrayList<>())
//                .wishCategory(wishName)
//                .build();
//    }
//    public TradeDTO.CreateTradeDto createTradeDto(Long sellerId, Long postId) {
//        return TradeDTO.CreateTradeDto.builder()
//                .sellerId(sellerId)
//                .postId(postId).build();
//    }
//    private CategoryDTO.CategorySaveRequest createCategoryDto(String name, Long memberId, Long depth, Long parentId) {
//        CategoryDTO.CategorySaveRequest saveRequest = new CategoryDTO.CategorySaveRequest(name, memberId, depth, parentId);
//        return saveRequest;
//    }
//
//    private MessageRoomSaveRequest messageRoomSaveRequest (Long postId, Long buyerId){
//        MessageRoomSaveRequest saveRequest = new MessageRoomSaveRequest(postId, buyerId);
//        return saveRequest;
//    }
//    private MessageSaveRequest messageSaveRequest(String content, Long senderId, Long receiverId, Long postId, Long messageRoomId){
//        MessageSaveRequest saveRequest = new MessageSaveRequest(content, senderId, receiverId, postId, messageRoomId);
//        return saveRequest;
//    }
//    private DeleteMessageRequest deleteMessageRequest(Long id, Long memberId, Long messageRoomId){
//        DeleteMessageRequest deleteRequest = new DeleteMessageRequest(id,memberId, messageRoomId);
//        return deleteRequest;
//    }
//
//    private DeleteMessageRoomRequest deleteMessageRoomRequest(Long Id, Long memberId, Long postId){
//        DeleteMessageRoomRequest deleteMessageRoomRequest = new DeleteMessageRoomRequest(Id, memberId, postId);
//        return deleteMessageRoomRequest;
//    }
////---------------------------------------------messageRoom 생성 -----------------------------------------------------------------
//    @Test
//    @DisplayName("메시지룸 생성 테스트")
//    public void createMessageRoomTest() throws Exception{
//        //given
//        MemberDTO.SignUpRequest signUpRequest1 = createSignUpRequest1();
//        authService.signUp(signUpRequest1);
//        Member admin = memberRepository.findByEmail(signUpRequest1.getEmail()).get();
//
//        MemberDTO.SignUpRequest signUpRequest2 = createSignUpRequest2();
//        authService.signUp(signUpRequest2);
//        Member user = memberRepository.findByEmail(signUpRequest2.getEmail()).get();
//
//        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
//        Long cid1 = categoryService.createCategory(categoryDTO1);
//        Category category1 = categoryRepository.findById(cid1).get();
//        Category root = categoryRepository.findCategoryByName("root").get();
//        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
//        Long cid2 = categoryService.createCategory(categoryDTO2);
//        Category category2 = categoryRepository.findById(cid2).get();
//        PostDTO.PostSaveRequest postSaveRequest = createPostSaveRequest(admin, true, "도서", "도서");
//        Long postId = postService.savePost(postSaveRequest, admin.getId());
//        Post post = postRepository.findById(postId).get();
//
//        MessageRoomSaveRequest messageRoomDTO1 = messageRoomSaveRequest(post.getId(), user.getId());
//        //when
//        MessageRoomInfoDto msgRoomDto = messageRoomService.createMessageRoom(messageRoomDTO1, user.getId());
//
//        //when
//        assertThat(messageRoomRepository.existsById(msgRoomDto.getId())).isEqualTo(true);
//
//
//    }
//
//    @Test
//    @DisplayName("(실패)메시지룸 생성 테스트: 본인에게 메시지")
//    public void createWrongMessageRoomTest() throws Exception {
//        //given
//        MemberDTO.SignUpRequest signUpRequest1 = createSignUpRequest1();
//        authService.signUp(signUpRequest1);
//        Member admin = memberRepository.findByEmail(signUpRequest1.getEmail()).get();
//
//        MemberDTO.SignUpRequest signUpRequest2 = createSignUpRequest2();
//        authService.signUp(signUpRequest2);
//        Member user = memberRepository.findByEmail(signUpRequest2.getEmail()).get();
//
//        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
//        Long cid1 = categoryService.createCategory(categoryDTO1);
//        Category category1 = categoryRepository.findById(cid1).get();
//        Category root = categoryRepository.findCategoryByName("root").get();
//        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
//        Long cid2 = categoryService.createCategory(categoryDTO2);
//        Category category2 = categoryRepository.findById(cid2).get();
//        PostDTO.PostSaveRequest postSaveRequest = createPostSaveRequest(admin, true, "도서", "도서");
//        Long postId = postService.savePost(postSaveRequest, admin.getId());
//        Post post = postRepository.findById(postId).get();
//
//        MessageRoomSaveRequest messageRoomDTO1 = messageRoomSaveRequest(post.getId(), admin.getId());
//        //when
//        assertThrows(CanNotMakeMessegeRoom.class, () -> {
//            Long msgRoomId = messageRoomService.createMessageRoom(messageRoomDTO1, admin.getId()).getId();
//        });
//    }
//
//    @Test
//    @DisplayName("(실패)메시지룸 생성 테스트: 거래 완료")
//    public void createTradedPostMessageRoomTest() throws Exception {
//        //given
//        MemberDTO.SignUpRequest signUpRequest1 = createSignUpRequest1();
//        authService.signUp(signUpRequest1);
//        Member admin = memberRepository.findByEmail(signUpRequest1.getEmail()).get();
//
//        MemberDTO.SignUpRequest signUpRequest2 = createSignUpRequest2();
//        authService.signUp(signUpRequest2);
//        Member user = memberRepository.findByEmail(signUpRequest2.getEmail()).get();
//
//        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
//        Long cid1 = categoryService.createCategory(categoryDTO1);
//        Category category1 = categoryRepository.findById(cid1).get();
//        Category root = categoryRepository.findCategoryByName("root").get();
//        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
//        Long cid2 = categoryService.createCategory(categoryDTO2);
//        Category category2 = categoryRepository.findById(cid2).get();
//        PostDTO.PostSaveRequest postSaveRequest = createPostSaveRequest(admin, true, "도서", "주방");
//        Long postId = postService.savePost(postSaveRequest, admin.getId());
//        Post post = postRepository.findById(postId).get();
//
//        MessageRoomSaveRequest messageRoomDTO1 = messageRoomSaveRequest(post.getId(), user.getId());
////        TradeDTO.CreateTradeDto tradeDto = TradeDTO.CreateTradeDto.builder().sellerId(user.getId()).postId(postId).build();
////        Long tradeId = tradeService.createTrade(tradeDto, user.getId());
//        Trade trade = tradeRepository.findByPostId(postId).get();
//        TradeDTO.UpdateTradeDto updateTradeDto = TradeDTO.UpdateTradeDto.builder().postId(postId).userId(admin.getId()).tradeStatus(TradeStatus.TRADED).build();
//        tradeService.updateTradeStatus(updateTradeDto, admin.getId());
//        //when
//        assertThrows(CanNotSendMessageByTradeStatus.class, () -> {
//            Long msgRoomId = messageRoomService.createMessageRoom(messageRoomDTO1, user.getId()).getId();
//        });
//    }
//
//   // -------------------------------------------메시지룸 조회 테스트------------------------------
//    @Test
//    @DisplayName("메시지룸 메시지 조회")
//    public void readMessageRoomTest() throws Exception {
//        //given
//        MemberDTO.SignUpRequest signUpRequest1 = createSignUpRequest1();
//        authService.signUp(signUpRequest1);
//        Member admin = memberRepository.findByEmail(signUpRequest1.getEmail()).get();
//
//        MemberDTO.SignUpRequest signUpRequest2 = createSignUpRequest2();
//        authService.signUp(signUpRequest2);
//        Member user = memberRepository.findByEmail(signUpRequest2.getEmail()).get();
//        MemberDTO.SignUpRequest signUpRequest3 = createSignUpRequest3();
//        authService.signUp(signUpRequest3);
//        Member user2 = memberRepository.findByEmail(signUpRequest3.getEmail()).get();
//
//
//        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
//        Long cid1 = categoryService.createCategory(categoryDTO1);
//        Category category1 = categoryRepository.findById(cid1).get();
//        Category root = categoryRepository.findCategoryByName("root").get();
//        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
//        Long cid2 = categoryService.createCategory(categoryDTO2);
//        Category category2 = categoryRepository.findById(cid2).get();
//        PostDTO.PostSaveRequest postSaveRequest = createPostSaveRequest(admin, true, "도서", "도서");
//        Long postId = postService.savePost(postSaveRequest, admin.getId());
//        Post post = postRepository.findById(postId).get();
//
//        //given
//        MessageRoomSaveRequest messageRoomDTO1 = messageRoomSaveRequest(post.getId(), user2.getId());
//        Long msgRoomId = messageRoomService.createMessageRoom(messageRoomDTO1, user2.getId()).getId();
//
//        MessageSaveRequest messageDTO1 = messageSaveRequest("저기요 물건 교환 하고 싶어요", user2.getId(), post.getAuthor().getId(), post.getId(), msgRoomId);
//        Long messageId1 = messageService.createMessage(messageDTO1,user2.getId()).getId();
//        MessageSaveRequest messageDTO2 = messageSaveRequest("어떠세요?", user2.getId(), post.getAuthor().getId(), post.getId(), msgRoomId);
//        Long messageId2 = messageService.createMessage(messageDTO2, user2.getId()).getId();
//        MessageSaveRequest messageDTO3 = messageSaveRequest("잠시만요", admin.getId(), user2.getId(), post.getId(), msgRoomId);
//        Long messageId3 = messageService.createMessage(messageDTO3, admin.getId()).getId();
//
//        assertThat(messageRoomRepository.findById(msgRoomId).get().getMessages().size()).isEqualTo(3);
//    }
//
//    @Test
//    @DisplayName("유저 아이디로 메시지룸 조회, selling, buying messageRoom 포함")
//    public void readMessageRoomByUserIdTest() throws Exception {
//        //given
//        MemberDTO.SignUpRequest signUpRequest1 = createSignUpRequest1();
//        authService.signUp(signUpRequest1);
//        Member admin = memberRepository.findByEmail(signUpRequest1.getEmail()).get();
//        MemberDTO.SignUpRequest signUpRequest2 = createSignUpRequest2();
//        authService.signUp(signUpRequest2);
//        Member user = memberRepository.findByEmail(signUpRequest2.getEmail()).get();
//        MemberDTO.SignUpRequest signUpRequest3 = createSignUpRequest3();
//        authService.signUp(signUpRequest3);
//        Member user2 = memberRepository.findByEmail(signUpRequest3.getEmail()).get();
//
//        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
//        Long cid1 = categoryService.createCategory(categoryDTO1);
//        Category category1 = categoryRepository.findById(cid1).get();
//        Category root = categoryRepository.findCategoryByName("root").get();
//        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
//        Long cid2 = categoryService.createCategory(categoryDTO2);
//        Category category2 = categoryRepository.findById(cid2).get();
//
//        PostDTO.PostSaveRequest postSaveRequest = createPostSaveRequest(admin, true, "도서", "도서");
//        Long postId = postService.savePost(postSaveRequest, admin.getId());
//        Post post = postRepository.findById(postId).get();
//        PostDTO.PostSaveRequest postSaveRequest2 = createPostSaveRequest2(user, true, "도서", "도서");
//        Long postId2 = postService.savePost(postSaveRequest2, user.getId());
//        Post post2 = postRepository.findById(postId2).get();
//
//        //given
//        MessageRoomSaveRequest messageRoomDTO1 = messageRoomSaveRequest(post.getId(), user2.getId());
//        Long msgRoomId = messageRoomService.createMessageRoom(messageRoomDTO1, user2.getId()).getId();
//        MessageRoomSaveRequest messageRoomDTO2 = messageRoomSaveRequest(post.getId(), user.getId());
//        Long msgRoomId2 = messageRoomService.createMessageRoom(messageRoomDTO2, user.getId()).getId();
//        MessageRoomSaveRequest messageRoomDTO3 = messageRoomSaveRequest(post2.getId(), admin.getId());
//        Long msgRoomId3 = messageRoomService.createMessageRoom(messageRoomDTO3, admin.getId()).getId();
//
//        MessageSaveRequest messageDTO1 = messageSaveRequest("저기요 물건 교환 하고 싶어요", user2.getId(), post.getAuthor().getId(), post.getId(), msgRoomId);
//        Long messageId1 = messageService.createMessage(messageDTO1, user2.getId()).getId();
//        MessageSaveRequest messageDTO2 = messageSaveRequest("어떠세요?", user2.getId(), post.getAuthor().getId(), post.getId(), msgRoomId);
//        Long messageId2 = messageService.createMessage(messageDTO2, user2.getId()).getId();
//        MessageSaveRequest messageDTO3 = messageSaveRequest("잠시만요", admin.getId(), user2.getId(), post.getId(), msgRoomId);
//        Long messageId3 = messageService.createMessage(messageDTO3, admin.getId()).getId();
//
//        MessageSaveRequest messageDTO4 = messageSaveRequest("그 양말 냄새 안나나요?", user.getId(), post.getAuthor().getId(), post.getId(), msgRoomId2);
//        Long messageId4 = messageService.createMessage(messageDTO4, user.getId()).getId();
//        MessageSaveRequest messageDTO5 = messageSaveRequest("빨면 안나는데 솔직히 이제 다섯번만 더 빨면 없어질것같아요.", admin.getId(), user.getId(), post.getId(), msgRoomId2);
//        Long messageId5 = messageService.createMessage(messageDTO5, admin.getId()).getId();
//
//        MessageSaveRequest messageDTO6 = messageSaveRequest("그 책 그림 많고 안뚜거운가요?", admin.getId(), post2.getAuthor().getId(), post2.getId(), msgRoomId3);
//        Long messageId6 = messageService.createMessage(messageDTO6, admin.getId()).getId();
//        MessageSaveRequest messageDTO7 = messageSaveRequest("그림은 별로 없고 두께는 보통인데 묘사가 걍 미쳤어요. 그림 필요없음.ㅎ", user.getId(), admin.getId(), post2.getId(), msgRoomId3);
//        Long messageId7 = messageService.createMessage(messageDTO7, user.getId()).getId();
//
//        //given
//        List<MessageRoomInfoDto> totalMsgRoom = messageRoomService.ReadMessageRoomsByUserId(admin.getId(), admin.getId());
//        List<BuyingRoomInfoDto> adminBuyingRoom = messageRoomService.ReadBuyingMessageRoomsByUserId(admin.getId(), admin.getId());
//        List<SellingRoomInfoDto> adminSellingRoom = messageRoomService.ReadSellingMessageRoomsByUserId(admin.getId(), admin.getId());
//        List<SellingRoomInfoDto> userSellingRoom = messageRoomService.ReadSellingMessageRoomsByUserId(user.getId(), user.getId());
//        //then
//        assertThat(totalMsgRoom.size()).isEqualTo(3);
//        assertThat(admin.getBuyingRooms().size()).isEqualTo(1);
//        assertThat(user.getSellingRooms().size()).isEqualTo(1);
//        assertThat(admin.getSellingRooms().size()).isEqualTo(2);
//        assertThat(adminBuyingRoom.size()).isEqualTo(1);
//        assertThat(userSellingRoom.size()).isEqualTo(1);
//        assertThat(adminSellingRoom.size()).isEqualTo(2);
//
//    }
//    @Test
//    @DisplayName("포스트 메시지룸 갯수 조회")
//    public void readMessagesInMessageRoomTest() throws Exception {
//        //given
//        MemberDTO.SignUpRequest signUpRequest1 = createSignUpRequest1();
//        authService.signUp(signUpRequest1);
//        Member admin = memberRepository.findByEmail(signUpRequest1.getEmail()).get();
//
//        MemberDTO.SignUpRequest signUpRequest2 = createSignUpRequest2();
//        authService.signUp(signUpRequest2);
//        Member user = memberRepository.findByEmail(signUpRequest2.getEmail()).get();
//        MemberDTO.SignUpRequest signUpRequest3 = createSignUpRequest3();
//        authService.signUp(signUpRequest3);
//        Member user2 = memberRepository.findByEmail(signUpRequest3.getEmail()).get();
//
//
//        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
//        Long cid1 = categoryService.createCategory(categoryDTO1);
//        Category category1 = categoryRepository.findById(cid1).get();
//        Category root = categoryRepository.findCategoryByName("root").get();
//        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
//        Long cid2 = categoryService.createCategory(categoryDTO2);
//        Category category2 = categoryRepository.findById(cid2).get();
//        PostDTO.PostSaveRequest postSaveRequest = createPostSaveRequest(admin, true, "도서", "도서");
//        Long postId = postService.savePost(postSaveRequest, admin.getId());
//        Post post = postRepository.findById(postId).get();
//
//        //given
//        MessageRoomSaveRequest messageRoomDTO1 = messageRoomSaveRequest(post.getId(), user2.getId());
//        Long msgRoomId = messageRoomService.createMessageRoom(messageRoomDTO1, user2.getId()).getId();
//        MessageRoomSaveRequest messageRoomDTO2 = messageRoomSaveRequest(post.getId(), user.getId());
//        Long msgRoomId2 = messageRoomService.createMessageRoom(messageRoomDTO2,user.getId()).getId();
//        MessageRoomSaveRequest messageRoomDTO3 = messageRoomSaveRequest(post.getId(), user.getId());
//        Long msgRoomId3 = messageRoomService.createMessageRoom(messageRoomDTO3, user.getId()).getId();
//
//        //then
//        assertThat(messageRoomService.ReadMessageRoomsByPostId(postId)).isEqualTo(3);
//    }
//    //----------------------------------메시지룸 삭제---------------------------------------------
////    @Test
////    @DisplayName("메시지룸 삭제")
////    public void deleteMessageRoomTest() throws Exception {
////        //given
////        MemberDTO.SignUpRequest signUpRequest1 = createSignUpRequest1();
////        authService.signUp(signUpRequest1);
////        Member admin = memberRepository.findByEmail(signUpRequest1.getEmail()).get();
////
////        MemberDTO.SignUpRequest signUpRequest2 = createSignUpRequest2();
////        authService.signUp(signUpRequest2);
////        Member user = memberRepository.findByEmail(signUpRequest2.getEmail()).get();
////        MemberDTO.SignUpRequest signUpRequest3 = createSignUpRequest3();
////        authService.signUp(signUpRequest3);
////        Member user2 = memberRepository.findByEmail(signUpRequest3.getEmail()).get();
////
////
////        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
////        Long cid1 = categoryService.createCategory(categoryDTO1);
////        Category category1 = categoryRepository.findById(cid1).get();
////        Category root = categoryRepository.findCategoryByName("root").get();
////        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
////        Long cid2 = categoryService.createCategory(categoryDTO2);
////        Category category2 = categoryRepository.findById(cid2).get();
////        PostDTO.PostSaveRequest postSaveRequest = createPostSaveRequest(admin, true, "도서", "도서");
////        Long postId = postService.savePost(postSaveRequest, admin.getId());
////        Post post = postRepository.findById(postId).get();
////
////        MessageRoomSaveRequest messageRoomDTO1 = messageRoomSaveRequest(post.getId(), user2.getId());
////        Long msgRoomId = messageRoomService.createMessageRoom(messageRoomDTO1, user2.getId()).getId();
////        MessageRoomSaveRequest messageRoomDTO2 = messageRoomSaveRequest(post.getId(), user.getId());
////        Long msgRoomId2 = messageRoomService.createMessageRoom(messageRoomDTO2,user.getId()).getId();
////        MessageRoomSaveRequest messageRoomDTO3 = messageRoomSaveRequest(post.getId(), user.getId());
////        Long msgRoomId3 = messageRoomService.createMessageRoom(messageRoomDTO3, user.getId()).getId();
////
////        //given
////        DeleteMessageRoomRequest delMsgRoomDto = deleteMessageRoomRequest(msgRoomId, user2.getId(), post.getId());
////        String delMsg = messageRoomService.deleteMessageRoom(delMsgRoomDto, user2.getId());
////
////        //then
////        assertThat(user2.getBuyingRooms().size()).isEqualTo(0);
////    }
//
//}
