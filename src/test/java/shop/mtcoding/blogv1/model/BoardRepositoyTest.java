package shop.mtcoding.blogv1.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
//r - mb - db
import org.springframework.beans.factory.annotation.Autowired;

import shop.mtcoding.blogv1.dto.board.BoardResp.BoardMainRespDto;

@MybatisTest
public class BoardRepositoyTest {

    @Autowired
    private BoardRepository boardRepository;

    // Test_save1. query 테스트
    @Test
    public void findAllWithUser_test() throws Exception {
        // given
        // when
        List<BoardMainRespDto> boardMainRespDto = boardRepository.findAllWithUser();
        // then
        System.out.println("boardMainRespDto size test : " + boardMainRespDto.size());
        assertThat(boardMainRespDto.get(5).getUsername()).isEqualTo("love");
    }
}
