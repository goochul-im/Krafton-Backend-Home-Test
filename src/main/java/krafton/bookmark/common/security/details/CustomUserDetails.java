package krafton.bookmark.common.security.details;

import krafton.bookmark.common.security.dto.LoginRequest;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final LoginRequest request;

    public CustomUserDetails(LoginRequest loginRequest) {
        this.request = loginRequest;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return request.password();
    }

    @Override
    public String getUsername() {
        return request.username();
    }
}
