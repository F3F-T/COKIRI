package f3f.dev1.domain.address.dao;

import f3f.dev1.domain.address.dto.AddressDTO;
import f3f.dev1.domain.address.dto.AddressDTO.AddressInfoDTO;
import f3f.dev1.domain.address.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findById(Long id);


    void deleteAddressById(Long addressId);


}
