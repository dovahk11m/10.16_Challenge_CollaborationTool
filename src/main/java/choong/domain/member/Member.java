package choong.domain.member;

import choong.common.util.DateUtil;
import choong.domain.member.memberEnum.MemberRole;
import choong.domain.member.memberEnum.MemberStatus;
import choong.domain.member.memberEnum.MemberType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_tb")
@Getter
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String nickname;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    //ENUM (role, status, type)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberType type;

    @CreationTimestamp
    private LocalDateTime createdAt;

    //회원 생성
    @Builder
    public Member(
            String nickname,
            String email,
            String password
    ) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        //기본값
        this.role = MemberRole.USER;
        this.status = MemberStatus.INACTIVE;
        this.type = MemberType.LOCAL;
    }

    //비밀번호 수정
    public void updatePassword(String password) {
        this.password = password;
    }

    //닉네임 수정
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    //시간 포맷
    public String getFormattedCreatedAt() {
        return DateUtil.format(createdAt);
    }

}
