package shop.mtcoding.blogv1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BoardController {
    @GetMapping({ "/", "/home" })
    public String main() {
        return "board/main";
    }

}
