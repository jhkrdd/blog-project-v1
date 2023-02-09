package shop.mtcoding.blogv1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blogv1.dto.board.BoardReq.BoardSaveDto;
import shop.mtcoding.blogv1.dto.board.BoardReq.BoardUpdateDto;
import shop.mtcoding.blogv1.handler.ex.CustomApiException;
import shop.mtcoding.blogv1.model.Board;
import shop.mtcoding.blogv1.model.BoardRepository;
import shop.mtcoding.blogv1.util.HtmlParser;

@Transactional(readOnly = true)
@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    @Transactional
    public void insert(BoardSaveDto boardSaveDto, int userId) {

        String thumbnail = HtmlParser.getThumbnail(boardSaveDto.getContent());

        int result = boardRepository.insert(
                boardSaveDto.getTitle(),
                boardSaveDto.getContent(),
                thumbnail,
                userId);
        if (result != 1) {
            throw new CustomApiException("글쓰기 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void deletebyId(int id, int userId) {
        // 게시글 유무 확인
        Board boardPs = boardRepository.findById(id);
        if (boardPs == null) {
            throw new CustomApiException("없는 게시물을 수정할 수 없습니다");
        }
        // 권한 체크
        if (userId != boardPs.getUserId()) {
            throw new CustomApiException("해당 게시글을 삭제할 권한이 없습니다", HttpStatus.FORBIDDEN);
        }

        int result = boardRepository.deleteById(id);
        if (result != 1) {
            throw new CustomApiException("일시적인 문제가 생겼습니다, 다시 시도해주세요",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void updatebyId(int id, BoardUpdateDto boardUpdateDto, int userId) {
        // 게시글 유무 확인
        Board boardPs = boardRepository.findById(id);
        if (boardPs == null) {
            throw new CustomApiException("없는 게시물을 삭제할 수 없습니다");
        }
        // 권한 체크
        if (userId != boardPs.getUserId()) {
            throw new CustomApiException("해당 게시글을 삭제할 권한이 없습니다", HttpStatus.FORBIDDEN);
        }
        String thumbnail = HtmlParser.getThumbnail(boardUpdateDto.getContent());
        int result = boardRepository.updateById(id,
                boardUpdateDto.getTitle(),
                boardUpdateDto.getContent(), thumbnail);
        if (result != 1) {
            throw new CustomApiException("일시적인 문제가 생겼습니다, 다시 시도해주세요",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
