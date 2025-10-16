package choong.domain.member.memberDTO;

import choong.domain.member.Member;
import choong.domain.member.memberEnum.MemberRole;
import choong.domain.member.memberEnum.MemberStatus;
import choong.domain.member.memberEnum.MemberType;
import lombok.Getter;

@Getter
public class SignupResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final MemberRole role;
    private final MemberStatus status;
    private final MemberType type;

    private SignupResponse(
            Long id,
            String email,
            String nickname,
            MemberRole role,
            MemberStatus status,
            MemberType type
    ) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.status = status;
        this.type = type;
    }

    public static SignupResponse from(Member member){
        return new SignupResponse(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getRole(),
                member.getStatus(),
                member.getType()
        );
    }
}
