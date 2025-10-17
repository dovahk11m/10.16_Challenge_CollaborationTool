package choong.domain.member.memberEvent;

import choong.domain.member.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PasswordResetEvent {
    private final Member member;
}