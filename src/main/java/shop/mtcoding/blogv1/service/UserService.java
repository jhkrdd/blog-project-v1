package shop.mtcoding.blogv1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blogv1.dto.user.UserReq.JoinDto;
import shop.mtcoding.blogv1.dto.user.UserReq.LoginDto;
import shop.mtcoding.blogv1.handler.ex.CustomException;
import shop.mtcoding.blogv1.model.User;
import shop.mtcoding.blogv1.model.UserRepository;

@Transactional(readOnly = true)
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    public void join(JoinDto joinDto) {
        // DI - Repository
        // 회원가입 유효성 검사
        User sameUserCheck = userRepository.findByUsername(joinDto.getUsername());
        if (sameUserCheck != null) {
            throw new CustomException("동일한 username이 존재합니다");
        }
        int result = userRepository.insert(joinDto.getUsername(), joinDto.getPassword(),
                joinDto.getEmail());

        if (result != 1) {
            throw new CustomException("회원가입실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public User login(LoginDto loginDto) {
        User principal = userRepository.findByUsernameAndPassword(
                loginDto.getUsername(), loginDto.getPassword());
        if (principal == null) {
            throw new CustomException("유저네임 혹은 패스워드가 잘못 입력되었습니다");
        }
        return principal;
    }
}
