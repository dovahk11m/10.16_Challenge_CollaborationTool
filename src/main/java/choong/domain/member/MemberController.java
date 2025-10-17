package choong.domain.member;

import choong.common.util.CommonResponse;
import choong.domain.member.MemberDTO.SignupRequest;
import choong.domain.member.MemberDTO.SignupResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<SignupResponse>> signUp(
            @Valid @RequestBody SignupRequest request
    ) {
        SignupResponse responseData = memberService.signUp(request);
        //새로 생성된 리소스에 접근할 수 있는 URI를 생성
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
        return ResponseEntity.ok(CommonResponse.success(null, "이메일 인증 성공"));

    }

}