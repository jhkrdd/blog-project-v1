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
            throw new CustomException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        Board boardPS = boardRepository.findById(id);
        if (boardPS == null) {
            throw new CustomException("없는 게시글을 수정할 수 없습니다");
        }
        if (boardPS.getUserId() != principal.getId()) {
            throw new CustomException("게시글을 수정할 권한이 없습니다", HttpStatus.FORBIDDEN);
        }
        model.addAttribute("board", boardPS);
        return "board/updateForm";
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

    @DeleteMapping("/board/{id}")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable int id) {
        // 인증 확인
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("로그인이 필요합니다", HttpStatus.UNAUTHORIZED);
        }
        boardService.deletebyId(id, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "삭제 성공", null), HttpStatus.OK);
    }

    @PutMapping("/board/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody BoardUpdateDto boardUpdateDto) {
        // 인증 확인
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("로그인이 필요합니다", HttpStatus.UNAUTHORIZED);
        }
        boardService.updatebyId(id, boardUpdateDto, principal.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "수정 성공", null), HttpStatus.OK);
    }

}
