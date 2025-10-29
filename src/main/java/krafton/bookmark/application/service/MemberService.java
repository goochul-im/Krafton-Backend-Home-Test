package krafton.bookmark.application.service;

import krafton.bookmark.api.dto.SingUpRequest;
import krafton.bookmark.application.exception.AlreadyExistException;
import krafton.bookmark.domain.member.Member;
import krafton.bookmark.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member signUp(SingUpRequest request) {

        if (memberRepository.countByUsername(request.username()) > 0) {
            log.error("이미 가입된 회원입니다. username : {}", request.username());
            throw new AlreadyExistException("Username already exists");
        }

        return memberRepository.save(new Member(
                request.username(),
                passwordEncoder.encode(request.password())
        ));
    }

}
