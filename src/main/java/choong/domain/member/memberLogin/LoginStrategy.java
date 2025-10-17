package choong.domain.member.memberLogin;

public interface LoginStrategy {

    LoginResponse login(LoginRequest request);

    boolean supports(String type);
}
