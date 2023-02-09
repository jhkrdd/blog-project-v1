package shop.mtcoding.blogv1.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blogv1.dto.ResponseDto;
import shop.mtcoding.blogv1.dto.board.BoardReq.BoardSaveDto;
import shop.mtcoding.blogv1.handler.ex.CustomApiException;
import shop.mtcoding.blogv1.model.BoardRepository;
import shop.mtcoding.blogv1.model.User;
import shop.mtcoding.blogv1.service.BoardService;

@Controller
public class BoardController {
    @Autowired
    HttpSession session;

    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @GetMapping({ "/", "/board" })
    public String main(Model model) {
        model.addAttribute("brwithuser", boardRepository.findAllWithUser());
        return "board/main";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, Model model) {
        model.addAttribute("brbyid", boardRepository.findByIdWithUser(id));
        return "board/detail";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @PostMapping("/board")
    public @ResponseBody ResponseEntity<?> saveBoard(@RequestBody BoardSaveDto boardSaveDto) {
        // 인증 체크
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("로그인이 필요합니다", HttpStatus.UNAUTHORIZED);
        }
        // 입력값 유효성 검사
        if (boardSaveDto.getTitle() == null || boardSaveDto.getTitle().isEmpty()) {
            throw new CustomApiException("title를 입력해주세요");
        }
        if (boardSaveDto.getContent() == null || boardSaveDto.getContent().isEmpty()) {
            throw new CustomApiException("content를 입력해주세요");
        }
        if (boardSaveDto.getTitle().length() > 100) {
            throw new CustomApiException("title을 100자 이하로 작성해주세요");
        }
        boardService.insert(boardSaveDto, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "게시 성공", null), HttpStatus.CREATED);
    }

}
