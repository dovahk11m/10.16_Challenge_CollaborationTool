package choong.domain.member.memberLogin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class LoginStrategyFactory {
    private final List<LoginStrategy> strategies;
    public LoginStrategy findStrategy(String type) {
        for (LoginStrategy strategy : strategies) {
            if (strategy.supports(type)) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 로그인 타입");
    }
}
