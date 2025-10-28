package krafton.bookmark.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import krafton.bookmark.common.security.util.SecurityResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        HashMap<String, Object> map = new HashMap<>();
        map.put("message", "Login failed");

        if (exception instanceof BadCredentialsException) {
            map.put("error", "Invalid password");
        } else if (exception instanceof UsernameNotFoundException) {
            map.put("error", "Username not found");
        } else if (exception instanceof AuthenticationServiceException) {
            map.put("error", "Username and password must be provided");
        }

        SecurityResponse.sendResponse(map, HttpStatus.BAD_REQUEST, response);
    }
}
