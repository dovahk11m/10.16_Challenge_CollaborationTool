package choong.domain.notification;

public interface NotificationSender {

    void send(NotificationDTO request);
    boolean supports(String type);
}
