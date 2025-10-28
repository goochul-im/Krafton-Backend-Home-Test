package krafton.bookmark.common.security.service;

import krafton.bookmark.common.security.details.CustomUserDetails;
import krafton.bookmark.common.security.dto.LoginRequest;
import krafton.bookmark.domain.member.Member;
import krafton.bookmark.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username);

        if (member == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        LoginRequest loginRequest = new LoginRequest(
                member.getUsername(),
                member.getPassword()
        );

        return new CustomUserDetails(loginRequest);
    }
}
