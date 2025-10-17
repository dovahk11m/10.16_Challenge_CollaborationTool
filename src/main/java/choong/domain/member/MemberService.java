package choong.domain.member;

import choong.domain.member.MemberSignup.NicknameGenerator;
import choong.domain.member.MemberSignup.SignupRequest;
import choong.domain.member.MemberSignup.SignupResponse;
import choong.domain.member.memberEvent.PasswordResetEvent;
import choong.domain.member.memberEvent.SignupEvent;
import choong.domain.member.memberLogin.LoginRequest;
import choong.domain.member.memberLogin.LoginResponse;
import choong.domain.member.memberLogin.LoginStrategy;
import choong.domain.member.memberLogin.LoginStrategyFactory;
import choong.domain.member.memberPassword.PasswordConfirmRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginStrategyFactory loginStrategyFactory;
    private final ApplicationEventPublisher eventPublisher;

    //로그인
    public LoginResponse login(String type, LoginRequest request){
        LoginStrategy strategy = loginStrategyFactory.findStrategy(type);
        return strategy.login(request);
    }

    //회원가입
    public SignupResponse signup(
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

    // 비밀번호 재설정 요청
    public void issuePasswordReset(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        String resetToken = UUID.randomUUID().toString();
        member.generatePasswordResetToken(resetToken);

        // 비밀번호 재설정 요청 이벤트 발행
        eventPublisher.publishEvent(new PasswordResetEvent(member));
        log.info("[비밀번호 재설정 요청] memberId: {}, email: {}", member.getId(), member.getEmail());
    }

    // 비밀번호 재설정
    public void resetPassword(PasswordConfirmRequest request) {
        Member member = memberRepository.findByPasswordResetToken(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 재설정 토큰입니다."));

        // 토큰 만료 시간 검증
        if (member.getPasswordResetTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("만료된 재설정 토큰입니다.");
        }

        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
        member.updatePassword(encodedNewPassword);
        log.info("[비밀번호 재설정 성공] memberId: {}", member.getId());
    }
}
