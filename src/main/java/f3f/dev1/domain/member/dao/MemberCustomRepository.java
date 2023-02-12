package f3f.dev1.domain.member.dao;

import f3f.dev1.domain.address.dto.AddressDTO;
import f3f.dev1.domain.member.dto.MemberDTO;
import f3f.dev1.domain.member.dto.MemberDTO.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberCustomRepository {

    MemberDTO.UserInfoWithAddress getUserInfo(Long userId);

    List<AddressDTO.AddressInfoDTO> getUserAddress(Long userId);

    MemberDTO.UserDetail getUserDetail(Long userId);

    MemberDTO.GetOtherUserInfoDto getOtherUserInfo(Long userId);

    MemberDTO.SimpleUserInfo getSimplerUserInfo(Long userId);

//    Page<MemberDTO.GetUserPost> getUserPost(Long userId, Pageable pageable);
}
