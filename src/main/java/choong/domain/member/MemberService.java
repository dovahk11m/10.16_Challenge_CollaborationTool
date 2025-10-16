package choong.domain.member;

import choong.domain.member.MemberSignup.SignupStrategy;
import choong.domain.member.MemberSignup.SignupStrategyFactory;
import choong.domain.member.memberDTO.SignupRequest;
import choong.domain.member.memberDTO.SignupResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignupStrategyFactory signupStrategyFactory;

    public SignupResponse signUp(
            String type,
            SignupRequest request
    ) {
        SignupStrategy strategy = signupStrategyFactory.findStrategy(type);
        return strategy.signup(request);
    }
}
