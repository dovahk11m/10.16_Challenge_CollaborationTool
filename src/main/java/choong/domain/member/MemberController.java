package choong.domain.member;

import choong.common.CommonResponse;
import choong.common.token.JwtProvider;
import choong.domain.member.MemberSignup.SignupRequest;
import choong.domain.member.MemberSignup.SignupResponse;
import choong.domain.member.memberLogin.LoginRequest;
import choong.domain.member.memberLogin.LoginResponse;
import choong.domain.member.memberPassword.PasswordConfirmRequest;
import choong.domain.member.memberPassword.PasswordResetRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    //로그인
    @PostMapping("/login/{type}")
    public ResponseEntity<CommonResponse<LoginResponse>> login(
            @PathVariable String type,
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse responseData = memberService.login(
                type,
                request
        );
        return ResponseEntity.ok(CommonResponse.success(
                responseData,
                "로그인 완료"
        ));
    }

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<SignupResponse>> signUp(
            @Valid @RequestBody SignupRequest request
    ) {
        SignupResponse responseData = memberService.signup(request);
        URI location = URI.create("/api/members/" + responseData.getId());
        //201 Created 상태 코드와 함께 Location 헤더, 응답 본문을 반환
        return ResponseEntity.created(location).body(
                CommonResponse.success(
                        responseData,
                        "회원가입 성공"
                ));
    }

    //인증링크
    @GetMapping("/verify")
    public ResponseEntity<CommonResponse<String>> verifyEmail(
            @RequestParam String token
    ) {
        memberService.verifyEmail(token);
        return ResponseEntity.ok(CommonResponse.success(
                null,
                "이메일 인증 성공"
        ));

    }

    //비밀번호 재설정
    @PostMapping("/password-reset/request")
    public ResponseEntity<CommonResponse<Void>> requestPasswordReset(
            @Valid @RequestBody PasswordResetRequest request
    ) {
        memberService.issuePasswordReset(request.getEmail());
        return ResponseEntity.ok(CommonResponse.success(
                null,
                "이메일로 비밀번호 재설정 링크 발송"
        ));
    }
    //비밀번호 재설정 확인
    @PostMapping("/password-reset/confirm")
    public ResponseEntity<CommonResponse<Void>> confirmPasswordReset(
            @Valid @RequestBody PasswordConfirmRequest request
    ) {
        memberService.resetPassword(request);
        return ResponseEntity.ok(CommonResponse.success(
                null,
                "비밀번호 재설정 성공"
        ));
    }
}