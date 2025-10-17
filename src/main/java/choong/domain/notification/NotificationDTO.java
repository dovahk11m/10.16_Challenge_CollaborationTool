package choong.domain.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationDTO {
    private final String to;
    private final String subject;
    private final String content;
}