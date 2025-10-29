package krafton.bookmark.common.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import krafton.bookmark.common.security.util.SecurityResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        log.warn(" 인증 실패(401) : {} , url = {}", authException.getMessage(), request.getRequestURI());

        HashMap<String, Object> map = new HashMap<>();
        map.put("message", "인증되지 않은 사용자는 접근할 수 없습니다.");

        SecurityResponse.sendResponse(map , HttpStatus.UNAUTHORIZED, response);
    }
}
