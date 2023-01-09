package f3f.dev1.domain.address.api;

import f3f.dev1.domain.address.application.AddressService;
import f3f.dev1.domain.address.dto.AddressDTO;
import f3f.dev1.domain.address.dto.AddressDTO.CreateAddressDto;
import f3f.dev1.domain.address.dto.AddressDTO.AddressInfoDTO;
import f3f.dev1.domain.member.exception.NotAuthorizedException;
import f3f.dev1.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static f3f.dev1.domain.address.dto.AddressDTO.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;


    // 아이디로 주소 조회
    @GetMapping(value = "/address/{address_id}")
    public ResponseEntity<AddressInfoDTO> getAddress(@PathVariable Long address_id) {
        return ResponseEntity.ok(addressService.getAddress(address_id));
    }

    // 주소 추가
    @PostMapping(value = "/address")
    public ResponseEntity<AddressInfoDTO> addAddress(@RequestBody CreateAddressDto createAddressDto) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        if (!memberId.equals(createAddressDto.getUserId())) {
            throw new NotAuthorizedException();
        }
        return ResponseEntity.ok(addressService.addAddress(memberId, createAddressDto));
    }

    // 주소 업데이트
    @PatchMapping(value = "/address")
    public ResponseEntity<AddressInfoDTO> updateAddress(@RequestBody UpdateAddressDTO updateAddressDTO) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        if (!memberId.equals(updateAddressDTO.getUserId())) {
            throw new NotAuthorizedException();
        }
        return ResponseEntity.ok(addressService.updateAddress(memberId, updateAddressDTO));
    }

    // 주소 삭제
    @DeleteMapping(value = "/address")
    public ResponseEntity<String> deleteAddress(@RequestBody DeleteAddressDTO deleteAddressDTO) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        if (!memberId.equals(deleteAddressDTO.getUserId())) {
            throw new NotAuthorizedException();
        }
        return ResponseEntity.ok(addressService.deleteAddress(memberId, deleteAddressDTO));
    }
}
