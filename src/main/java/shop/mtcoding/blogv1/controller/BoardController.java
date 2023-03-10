package shop.mtcoding.blogv1.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blogv1.dto.ResponseDto;
import shop.mtcoding.blogv1.dto.board.BoardReq.BoardSaveDto;
import shop.mtcoding.blogv1.dto.board.BoardReq.BoardUpdateDto;
import shop.mtcoding.blogv1.handler.ex.CustomApiException;
import shop.mtcoding.blogv1.handler.ex.CustomException;
import shop.mtcoding.blogv1.model.Board;
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

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, Model model) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("????????? ?????? ???????????????", HttpStatus.UNAUTHORIZED);
        }
        Board boardPS = boardRepository.findById(id);
        if (boardPS == null) {
            throw new CustomException("?????? ???????????? ????????? ??? ????????????");
        }
        if (boardPS.getUserId() != principal.getId()) {
            throw new CustomException("???????????? ????????? ????????? ????????????", HttpStatus.FORBIDDEN);
        }
        model.addAttribute("board", boardPS);
        return "board/updateForm";
    }

    @PostMapping("/board")
    public @ResponseBody ResponseEntity<?> saveBoard(@RequestBody BoardSaveDto boardSaveDto) {
        // ?????? ??????
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("???????????? ???????????????", HttpStatus.UNAUTHORIZED);
        }
        // ????????? ????????? ??????
        if (boardSaveDto.getTitle() == null || boardSaveDto.getTitle().isEmpty()) {
            throw new CustomApiException("title??? ??????????????????");
        }
        if (boardSaveDto.getContent() == null || boardSaveDto.getContent().isEmpty()) {
            throw new CustomApiException("content??? ??????????????????");
        }
        if (boardSaveDto.getTitle().length() > 100) {
            throw new CustomApiException("title??? 100??? ????????? ??????????????????");
        }
        boardService.insert(boardSaveDto, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "?????? ??????", null), HttpStatus.CREATED);
    }

    @DeleteMapping("/board/{id}")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable int id) {
        // ?????? ??????
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("???????????? ???????????????", HttpStatus.UNAUTHORIZED);
        }
        boardService.deletebyId(id, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "?????? ??????", null), HttpStatus.OK);
    }

    @PutMapping("/board/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody BoardUpdateDto boardUpdateDto) {
        // ?????? ??????
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("???????????? ???????????????", HttpStatus.UNAUTHORIZED);
        }
        boardService.updatebyId(id, boardUpdateDto, principal.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "?????? ??????", null), HttpStatus.OK);
    }

}
