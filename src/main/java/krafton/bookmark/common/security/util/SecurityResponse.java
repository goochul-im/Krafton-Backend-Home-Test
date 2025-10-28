package krafton.bookmark.common.security.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Map;

public class SecurityResponse {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 스프링 시큐리티 내부에서 응답을 클라이언트에게 json으로 전달합니다.
     * @param messages
     * 응답 메세지, Map을 통해 복수로 전달 가능합니다
     * @param status
     * 응답 상태 코드
     */
    public static void sendResponse(Map<String, Object> messages, HttpStatus status, HttpServletResponse response) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(
                objectMapper.writeValueAsString(messages)
        );
    }

}
