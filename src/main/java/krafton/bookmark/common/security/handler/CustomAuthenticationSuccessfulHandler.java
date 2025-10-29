package krafton.bookmark.common.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import krafton.bookmark.common.security.details.CustomUserDetails;
import krafton.bookmark.common.security.util.SecurityResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
public class CustomAuthenticationSuccessfulHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        HashMap<String, Object> map = new HashMap<>();
        map.put("message", "Login success");
        map.put("username", userDetails.getUsername());

        SecurityResponse.sendResponse(map, HttpStatus.OK, response);

        clearAuthenticationAttribute(request);
    }

    /**
     * 이전에 로그인 실패 시 세션에 불필요한 데이터가 쌓여있을 수 있습니다.
     * 이 실패 흔적을 명시적으로 삭제하여 메모리를 확보합니다.
     */
    private void clearAuthenticationAttribute(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
