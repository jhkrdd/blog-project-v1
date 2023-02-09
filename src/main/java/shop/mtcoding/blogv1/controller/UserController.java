package shop.mtcoding.blogv1.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blogv1.dto.user.UserReq.JoinDto;
import shop.mtcoding.blogv1.dto.user.UserReq.LoginDto;
import shop.mtcoding.blogv1.handler.ex.CustomException;
import shop.mtcoding.blogv1.model.User;
import shop.mtcoding.blogv1.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @PostMapping("/join")
    public String join(JoinDto joinDto) {
        // 입력 데이터 유효성 검사
        if (joinDto.getUsername() == null || joinDto.getUsername().isEmpty()) {
            throw new CustomException("username이 잘못 입력되었습니다");
        }
        if (joinDto.getPassword() == null || joinDto.getPassword().isEmpty()) {
            throw new CustomException("password가 잘못 입력되었습니다");
        }
        if (joinDto.getEmail() == null || joinDto.getEmail().isEmpty()) {
            throw new CustomException("email이 잘못 입력되었습니다");
        }
        // DI : service
        userService.join(joinDto);

        return "redirect:/loginForm";
    }

    @PostMapping("/login")
    public String login(LoginDto loginDto) {
        // 입력 데이터 유효성 검사
        if (loginDto.getUsername() == null || loginDto.getUsername().isEmpty()) {
            throw new CustomException("username이 잘못 입력되었습니다");
        }
        if (loginDto.getPassword() == null || loginDto.getPassword().isEmpty()) {
            throw new CustomException("password가 잘못 입력되었습니다");
        }
        // DI : service
        User principal = userService.login(loginDto);
        // 세션 정보 저장
        session.setAttribute("principal", principal);

        return "redirect:/";
    }
}
