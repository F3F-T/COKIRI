package f3f.dev1.domain.postImage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class PostImageDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class postImageInfoDto {
        private Long id;
        private String imgPath;
//        private Boolean isThumbnail;
    }
}
