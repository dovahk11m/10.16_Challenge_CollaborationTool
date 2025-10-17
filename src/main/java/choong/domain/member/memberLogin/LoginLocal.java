package choong.domain.member.memberLogin;

import choong.common.token.JwtProvider;
import choong.domain.member.Member;
import choong.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LoginLocal implements LoginStrategy {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalIdentifierException("이메일 또는 비밀번호 불일치"));
        if (!passwordEncoder.matches(
                request.getPassword(),
                member.getPassword()
        )) {
            throw new IllegalArgumentException("이메일 또는 비밀번호 불일치");
        }
        String token = jwtProvider.createToken(
                member.getEmail(),
                member.getRole()
        );
        return LoginResponse.of(
                member,
                token
        );
    }

    @Override
    public boolean supports(String type) {
        return "LOCAL".equalsIgnoreCase(type);
    }
}
