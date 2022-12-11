package f3f.dev1.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    String addressName;

    String postalAddress;

    String latitude;

    String longitude;

    @Override
    public boolean equals(Object o) {
        return o instanceof Address &&
                ((Address) o).addressName.equalsIgnoreCase(addressName) &&
                ((Address) o).postalAddress.equalsIgnoreCase(postalAddress) &&
                ((Address) o).latitude.equalsIgnoreCase(latitude) &&
                ((Address) o).longitude.equalsIgnoreCase(longitude);
    }
}
