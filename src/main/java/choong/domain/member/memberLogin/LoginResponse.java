package choong.domain.member.memberLogin;

import choong.domain.member.Member;
import choong.domain.member.memberEnum.MemberRole;
import choong.domain.member.memberEnum.MemberStatus;
import choong.domain.member.memberEnum.MemberType;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final MemberRole role;
    private final MemberStatus status;
    private final MemberType type;
    //토큰부여
    private final String token;

    private LoginResponse(
            Long id,
            String email,
            String nickname,
            MemberRole role,
            MemberStatus status,
            MemberType type,
            String token
    ) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.status = status;
        this.type = type;
        this.token = token;
    }

    public static LoginResponse of(Member member, String token){
        return new LoginResponse(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getRole(),
                member.getStatus(),
                member.getType(),
                token
        );
    }
}
