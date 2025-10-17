package choong.domain.member.MemberDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Size(min = 4, max = 20)
    private String password;

}
