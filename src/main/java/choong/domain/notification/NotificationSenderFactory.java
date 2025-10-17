package choong.domain.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class NotificationSenderFactory {
    private final List<NotificationSender> senderList;

    //들어온 type에 맞는 Sender를 찾아 반환
    public NotificationSender findSender(String type) {
        for (NotificationSender sender : senderList) {
            if (sender.supports(type)) {
                return sender;
            }
        }
        throw new IllegalArgumentException("미지원 알림 방식" + type);
    }
}
