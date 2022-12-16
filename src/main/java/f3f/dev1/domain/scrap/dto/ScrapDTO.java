package f3f.dev1.domain.scrap.dto;

import f3f.dev1.domain.scrap.model.Scrap;
import f3f.dev1.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ScrapDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CreateScrapDTO {

        private User user;

        public Scrap toEntity() {
            return Scrap.builder()
                    .user(user).build();
        }

    }

}
