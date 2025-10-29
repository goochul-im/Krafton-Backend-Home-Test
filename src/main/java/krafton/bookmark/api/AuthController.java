package krafton.bookmark.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import krafton.bookmark.api.dto.SingUpRequest;
import krafton.bookmark.common.security.details.CustomUserDetails;
import krafton.bookmark.domain.member.Member;
import krafton.bookmark.application.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;

    @GetMapping("/test")
    public ResponseEntity<?> test(@AuthenticationPrincipal CustomUserDetails userDetails){

        return userDetails == null ? ResponseEntity.ok("Unauthorized") : ResponseEntity.ok(userDetails.getUsername());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){

        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return new ResponseEntity<>("logout success" ,HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SingUpRequest req){

        Member member = memberService.signUp(req);

        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }

    /**
     * swagger 명세서를 만들기 위한 가짜 api입니다.
     * /auth/login은 스프링 시큐리티의 CustomAuthenticationRequestFilter가 가로채어 처리합니다.
     */
    @PostMapping("/login")
    public void fakeLoginApi(){}

}
