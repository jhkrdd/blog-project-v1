package shop.mtcoding.blogv1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blogv1.dto.board.BoardReq.BoardSaveDto;
import shop.mtcoding.blogv1.handler.ex.CustomApiException;
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

}
