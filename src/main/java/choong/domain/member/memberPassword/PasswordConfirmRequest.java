package choong.domain.member.memberPassword;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordConfirmRequest {
    @NotEmpty
    private String token;

    @NotEmpty
    @Size(min = 4, max = 20)
    private String newPassword;
}