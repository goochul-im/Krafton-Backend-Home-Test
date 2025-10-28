package krafton.bookmark.domain.member;

import krafton.bookmark.api.dto.SingUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member signUp(SingUpRequest request) {

        if (memberRepository.countByUsername(request.username()) > 0)
            throw new IllegalArgumentException("Username already exists");

        return memberRepository.save(new Member(
                request.username(),
                request.password()
        ));
    }

}
