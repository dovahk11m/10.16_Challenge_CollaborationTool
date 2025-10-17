package choong.domain.member;

import choong.domain.member.MemberDTO.SignupRequest;
import choong.domain.member.MemberDTO.SignupResponse;
import choong.domain.member.memberEvent.SignupEvent;
import choong.domain.member.memberHelper.NicknameGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    //회원가입
    public SignupResponse signUp(
            SignupRequest request
    ) {
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
        // 인증 토큰 생성
        String verificationToken = UUID.randomUUID().toString();
        // 엔티티 생성
        Member member = Member.builder()
                .nickname(uniqueNickname)
                .email(request.getEmail())
                .password(encodedPassword)
                .verificationToken(verificationToken)
                .build();
        // DB 저장
        Member savedMember = memberRepository.save(member);
        //완료되면 변경사실 전파
        eventPublisher.publishEvent(new SignupEvent(member));
        // DTO 반환
        return SignupResponse.from(savedMember);
    }

    //가입인증
    public void verifyEmail(String token) {
        Member member = memberRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰"));
        member.activate();
        log.info("[이메일 가입인증 성공 memberId: {}, email: {}]", member.getId(), member.getEmail());
    }

}
