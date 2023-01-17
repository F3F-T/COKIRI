package f3f.dev1.domain.member.dao;

import f3f.dev1.domain.address.dto.AddressDTO;
import f3f.dev1.domain.member.dto.MemberDTO;
import f3f.dev1.domain.member.dto.MemberDTO.UserInfo;

import java.util.List;

public interface MemberCustomRepository {

    MemberDTO.UserInfoWithAddress getUserInfo(Long userId);

    List<AddressDTO.AddressInfoDTO> getUserAddress(Long userId);

    MemberDTO.UserDetail getUserDetail(Long userId);
}
