package shop.mtcoding.blogv1.dto.board;

import lombok.Getter;
import lombok.Setter;

public class BoardReq {
    @Getter
    @Setter
    public static class BoardSaveDto {
        private String title;
        private String content;
    }

    @Getter
    @Setter
    public static class BoardUpdateDto {
        private String title;
        private String content;
    }
}
