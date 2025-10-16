package choong.domain.member.MemberSignup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class SignupStrategyFactory {

    private final List<SignupStrategy> strategies;

    public SignupStrategy findStrategy(String type) {
        for (SignupStrategy strategy : strategies) {
            if (strategy.supports(type)) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("미지원 가입방식");
    }
}
