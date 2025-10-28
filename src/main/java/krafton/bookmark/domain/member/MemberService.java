package krafton.bookmark.domain.member;

import krafton.bookmark.api.dto.SingUpRequest;
import krafton.bookmark.domain.member.exception.AlreadyExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member signUp(SingUpRequest request) {

        if (memberRepository.countByUsername(request.username()) > 0) {
            log.error("이미 가입된 회원입니다. username : {}", request.username());
            throw new AlreadyExistException("Username already exists");
        }

        return memberRepository.save(new Member(
                request.username(),
                request.password()
        ));
    }

}
