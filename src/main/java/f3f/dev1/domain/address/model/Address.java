package f3f.dev1.domain.address.model;

import f3f.dev1.domain.address.dto.AddressDTO;
import f3f.dev1.domain.address.dto.AddressDTO.AddressInfoDTO;
import f3f.dev1.domain.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static f3f.dev1.domain.address.dto.AddressDTO.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue
    @Column(name = "address_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String addressName;

    private String postalAddress;

    private String latitude;

    private String longitude;

    @Override
    public boolean equals(Object o) {
        return o instanceof Address &&
                ((Address) o).addressName.equalsIgnoreCase(addressName) &&
                ((Address) o).postalAddress.equalsIgnoreCase(postalAddress) &&
                ((Address) o).latitude.equalsIgnoreCase(latitude) &&
                ((Address) o).longitude.equalsIgnoreCase(longitude);
    }

    public AddressInfoDTO toInfoDto() {
        return AddressInfoDTO.builder()
                .memberId(member.getId())
                .addressName(addressName)
                .postalAddress(postalAddress)
                .latitude(latitude)
                .longitude(longitude).build();
    }

    public void updateAddress(UpdateAddressDTO updateAddressDTO) {
        addressName = updateAddressDTO.getAddressName();
        latitude = updateAddressDTO.getLatitude();
        longitude = updateAddressDTO.getLongitude();
        postalAddress = updateAddressDTO.getPostalAddress();
    }
}
