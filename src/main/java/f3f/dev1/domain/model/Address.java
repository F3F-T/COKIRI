package f3f.dev1.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    String addressName;

    String postalAddress;

    String latitude;

    String longitude;

}
