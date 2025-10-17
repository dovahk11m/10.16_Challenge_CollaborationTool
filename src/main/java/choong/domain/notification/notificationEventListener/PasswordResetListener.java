package choong.domain.notification.notificationEventListener;

import choong.domain.member.memberEvent.PasswordResetEvent;
import choong.domain.notification.NotificationDTO;
import choong.domain.notification.NotificationSender;
import choong.domain.notification.NotificationSenderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
@Slf4j
public class PasswordResetListener {
    private final NotificationSenderFactory notificationSenderFactory;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handlePasswordResetEvent(PasswordResetEvent event) {
        log.info(
                "[비밀번호 재설정 이벤트 수신: {}]",
                event.getMember().getEmail()
        );
        NotificationSender sender = notificationSenderFactory.findSender("EMAIL");

        String subject = "[Challenge] 비밀번호 재설정 안내";
        String resetUrl = "http://localhost:8080/members/reset-password?token=" + event.getMember().getPasswordResetToken();
        String content = String.format(
                "비밀번호를 재설정하려면 아래 링크를 클릭하세요.\n\n%s",
                resetUrl
        );

        NotificationDTO request = new NotificationDTO(
                event.getMember().getEmail(),
                subject,
                content
        );
        sender.send(request);
    }
}