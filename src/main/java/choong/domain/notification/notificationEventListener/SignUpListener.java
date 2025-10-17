package choong.domain.notification.notificationEventListener;

import choong.domain.member.memberEvent.SignupEvent;
import choong.domain.notification.NotificationSender;
import choong.domain.notification.NotificationSenderFactory;
import choong.domain.notification.NotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
@Slf4j
public class SignUpListener {
    private final NotificationSenderFactory notificationSenderFactory;
    @Value("${notification.policy.on-status-done}")
    private String onStatusDoneType;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleSignUpEvent(SignupEvent event) {
        log.info(
                "[회원가입 이벤트 수신: {}]",
                event.getMember().getEmail()
        );
        NotificationSender sender = notificationSenderFactory.findSender(onStatusDoneType);
        String subject = "[Challenge] 회원가입을 축하합니다 이메일 인증을 완료해주세요";
        String verificationUrl = "http://localhost:8080/api/members/verify?token=" + event.getMember().getVerificationToken();
        String content = String.format(
                "'%s'님의 회원가입이 거의 완료되었습니다.\n아래 링크를 클릭하여 계정을 활성화해주세요.\n\n%s",
                event.getMember().getNickname(),
                verificationUrl
        );
        NotificationDTO request = new NotificationDTO(
                event.getMember().getEmail(),
                subject,
                content
        );
        sender.send(request);
    }
}
