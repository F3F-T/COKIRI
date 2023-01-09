package f3f.dev1.domain.address.dto;

import f3f.dev1.domain.address.model.Address;
import f3f.dev1.domain.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AddressDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CreateAddressDto {
        private Long userId;

        private String addressName;

        private String postalAddress;

        private String latitude;

        private String longitude;

        public Address toEntity(Member member) {
            return Address.builder()
                    .addressName(addressName)
                    .postalAddress(postalAddress)
                    .latitude(latitude)
                    .longitude(longitude)
                    .member(member)
                    .build();

        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class AddressInfoDTO {

        private Long id;

        private Long memberId;
        private String addressName;

        private String postalAddress;

        private String latitude;

        private String longitude;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdateAddressDTO {

        private Long userId;

        private Long addressId;
        private String addressName;

        private String postalAddress;

        private String latitude;

        private String longitude;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class DeleteAddressDTO {
        private Long userId;

        private Long addressId;
    }



}
