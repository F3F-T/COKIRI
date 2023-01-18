package f3f.dev1.domain.address.application;

import f3f.dev1.domain.address.dao.AddressRepository;
import f3f.dev1.domain.address.dto.AddressDTO.AddressInfoDTO;
import f3f.dev1.domain.address.exception.InvalidDeleteRequest;
import f3f.dev1.domain.address.exception.WrongUserException;
import f3f.dev1.domain.address.model.Address;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.exception.NotAuthorizedException;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static f3f.dev1.domain.address.dto.AddressDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public AddressInfoDTO getAddress(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(NotFoundByIdException::new);
        return address.toInfoDto();
    }


    // 주소 생성
    @Transactional
    public AddressInfoDTO addAddress(Long memberId, CreateAddressDto createAddressDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        if (!memberId.equals(createAddressDto.getUserId())) {
            throw new NotAuthorizedException();
        }
        Address address = createAddressDto.toEntity(member);
        addressRepository.save(address);
        return address.toInfoDto();
    }

    // 주소 업데이트
    @Transactional
    public AddressInfoDTO updateAddress(Long memberId, UpdateAddressDTO updateAddressDTO) {
        Address address = addressRepository.findById(updateAddressDTO.getAddressId()).orElseThrow(NotFoundByIdException::new);
        if (!address.getMember().getId().equals(memberId)) {
            throw new WrongUserException();
        }
        address.updateAddress(updateAddressDTO);
        return address.toInfoDto();
    }

    // 주소 삭제
    @Transactional
    public String deleteAddress(Long memberId, DeleteAddressDTO deleteAddressDTO) {
        if (!memberId.equals(deleteAddressDTO.getUserId())) {
            throw new NotAuthorizedException();
        }
        Address address = addressRepository.findById(deleteAddressDTO.getAddressId()).orElseThrow(NotFoundByIdException::new);
        addressRepository.delete(address);

        return "DELETE";
    }



}
