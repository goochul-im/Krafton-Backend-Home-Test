package krafton.bookmark.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import krafton.bookmark.api.dto.SingUpRequest;
import krafton.bookmark.common.security.details.CustomUserDetails;
import krafton.bookmark.common.security.dto.LoginRequest;
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

@Tag(name = "Auth", description = "인증 및 회원가입 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;

    @Operation(summary = "인증 테스트", description = "현재 인증된 사용자 정보를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "인증된 사용자 정보 반환 또는 Unauthorized",
            content = @Content(schema = @Schema(implementation = String.class)))
    @GetMapping("/test")
    public ResponseEntity<?> test(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails){

        return userDetails == null ? ResponseEntity.ok("Unauthorized") : ResponseEntity.ok(userDetails.getUsername());
    }

    @Operation(summary = "로그아웃", description = "현재 세션을 무효화하여 로그아웃합니다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){

        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return new ResponseEntity<>("logout success" ,HttpStatus.OK);
    }

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "회원가입 성공",
            content = @Content(schema = @Schema(implementation = Member.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SingUpRequest req){

        Member member = memberService.signUp(req);

        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }

    /**
     * swagger 명세서를 만들기 위한 가짜 api입니다.
     * /auth/login은 스프링 시큐리티의 CustomAuthenticationRequestFilter가 가로채어 처리합니다.
     */
    @Operation(summary = "로그인", description = "애플리케이션 로그인합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @ApiResponse(responseCode = "400", description = "로그인 실패")
    @PostMapping("/login")
    public void fakeLoginApi(
            @RequestBody LoginRequest req
    ){}

}
