package choong.domain.member;

import choong.common.util.CommonResponse;
import choong.domain.member.memberDTO.SignupRequest;
import choong.domain.member.memberDTO.SignupResponse;
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

    @PostMapping("/signup/{type}")
    public ResponseEntity<CommonResponse<SignupResponse>> signUp(
            @PathVariable String type,
            @Valid @RequestBody SignupRequest request
    ) {
        SignupResponse responseData = memberService.signUp(type, request);
        //새로 생성된 리소스에 접근할 수 있는 URI를 생성
        URI location = URI.create("/api/members/" + responseData.getId());
        //201 Created 상태 코드와 함께 Location 헤더, 응답 본문을 반환
        return ResponseEntity.created(location).body(
                CommonResponse.success(
                        responseData,
                        "회원가입 성공"
                ));
    }
}