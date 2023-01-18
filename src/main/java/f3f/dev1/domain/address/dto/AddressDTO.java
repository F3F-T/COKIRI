package f3f.dev1.domain.address.dto;

import com.querydsl.core.annotations.QueryProjection;
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
                    .member(member)
                    .postalAddress(postalAddress)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();

        }
    }

    @Builder
    @NoArgsConstructor
    @Getter
    public static class AddressInfoDTO {

        private Long id;

        private String addressName;

        private String postalAddress;

        private String latitude;

        private String longitude;
        @QueryProjection
        public AddressInfoDTO(Long id, String addressName, String postalAddress, String latitude, String longitude) {
            this.id = id;
            this.addressName = addressName;
            this.postalAddress = postalAddress;
            this.latitude = latitude;
            this.longitude = longitude;
        }
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
