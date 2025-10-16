package choong.domain.member.MemberSignup;

import choong.domain.member.memberDTO.SignupRequest;
import choong.domain.member.memberDTO.SignupResponse;

public interface SignupStrategy {

    SignupResponse signup(SignupRequest request);

    boolean supports(String type);
}
