package krafton.bookmark.common;

import krafton.bookmark.domain.member.Member;
import krafton.bookmark.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (memberRepository.count() == 0) {
            memberRepository.save(new Member("user", passwordEncoder.encode("1111")));
        }
    }
}
