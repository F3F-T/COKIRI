package f3f.dev1.scrap;

import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.dto.MemberDTO;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.scrap.application.ScrapService;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class ScrapServiceTest {
    @Autowired
    ScrapRepository scrapRepository;

    @Autowired
    ScrapService scrapService;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;


    public Address createAddress() {
        return Address.builder()
                .addressName("address")
                .postalAddress("13556")
                .latitude("37.49455")
                .longitude("127.12170")
                .build();
    }

    // 회원가입 DTO 생성 메소드
    public MemberDTO.SignUpRequest createSignUpRequest() {
        return MemberDTO.SignUpRequest.builder()
                .userName("username")
                .nickname("nickname")
                .phoneNumber("01012345678")
                .email("userEmail@email.com")
                .birthDate("990128")
                .address(createAddress())
                .password("password")
                .build();
    }

    // 로그인 DTO 생성 메소드
    public MemberDTO.LoginRequest createLoginRequest() {
        return MemberDTO.LoginRequest.builder()
                .email("userEmail@email.com")
                .password("password").build();
    }

}
