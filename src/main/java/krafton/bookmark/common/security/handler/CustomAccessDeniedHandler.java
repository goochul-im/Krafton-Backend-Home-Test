package krafton.bookmark.common.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import krafton.bookmark.common.security.util.SecurityResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.warn(" 접근이 허용되지 않은 경로입니다. : {} , url = {} ", accessDeniedException.getMessage(), request.getRequestURI());

        HashMap<String, Object> map = new HashMap<>();
        map.put("message", "접근 권한이 없습니다 !!");

        SecurityResponse.sendResponse(map, HttpStatus.FORBIDDEN, response);
    }
}
