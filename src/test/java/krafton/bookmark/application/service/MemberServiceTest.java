package krafton.bookmark.application.service;

import krafton.bookmark.api.dto.SingUpRequest;
import krafton.bookmark.application.exception.AlreadyExistException;
import krafton.bookmark.domain.member.Member;
import krafton.bookmark.domain.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    String username = "user33";
    String password = "1234";
    SingUpRequest testRequest = new SingUpRequest(
            username,
            password
    );
    Member testMember = new Member(username, password);

    @Test
    void 멤버는_정확히_가입되어야_한다() {

        //GIVEN
        given(memberRepository.save(any())).willReturn(testMember);
        given(memberRepository.countByUsername(username)).willReturn(0);
        given(passwordEncoder.encode(password)).willReturn(password);

        //WHEN
        Member save = memberService.signUp(testRequest);

        //THEN
        assertEquals(username, save.getUsername());
    }

    @Test
    void 이미_존재하는_username으로_가입하면_예외를_던져야_한다() {

        //GIVEN
        given(memberRepository.countByUsername(username)).willReturn(1);

        //WHEN & THEN
        assertThrows(AlreadyExistException.class, () -> memberService.signUp(testRequest));
    }
}
