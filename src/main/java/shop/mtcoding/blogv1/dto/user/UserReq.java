package shop.mtcoding.blogv1.dto.user;

import lombok.Getter;
import lombok.Setter;

public class UserReq {

    @Setter
    @Getter
    public static class JoinDto {
        private String username;
        private String password;
        private String email;
    }

    @Setter
    @Getter
    public static class LoginDto {
        private String username;
        private String password;
    }

}
