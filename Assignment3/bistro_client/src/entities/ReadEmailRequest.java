package entities;

public class ReadEmailRequest extends Request {

    private static final long serialVersionUID = 1L;
    private int subscriberId;

    public ReadEmailRequest(int subscriberId) {
        super(RequestType.READ_EMAIL, "SELECT email FROM `user` WHERE subscriber_id = ?");
        this.subscriberId = subscriberId;
    }

    public int getSubscriberId() {
        return subscriberId;
    }
}
