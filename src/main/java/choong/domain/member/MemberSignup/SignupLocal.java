package choong.domain.member.MemberSignup;

import choong.domain.member.Member;
import choong.domain.member.MemberRepository;
import choong.domain.member.memberDTO.SignupRequest;
import choong.domain.member.memberDTO.SignupResponse;
import choong.domain.member.memberUtil.NicknameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SignupLocal implements SignupStrategy{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SignupResponse signup(SignupRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 이메일");
        }
        // 닉네임 생성
        String uniqueNickname;
        do {
            uniqueNickname = NicknameGenerator.generate();
        } while (memberRepository.findByNickname(uniqueNickname).isPresent());
        // 패스워드 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        // 엔티티 생성
        Member member = Member.builder()
                .nickname(uniqueNickname)
                .email(request.getEmail())
                .password(encodedPassword)
                .build();
        // DB 저장
        Member savedMember = memberRepository.save(member);
        // DTO 반환
        return SignupResponse.from(savedMember);
    }

    @Override
    public boolean supports(String type) {
        return "LOCAL".equalsIgnoreCase(type);
    }
}
