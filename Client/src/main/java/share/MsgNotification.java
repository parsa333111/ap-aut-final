package share;

public class MsgNotification extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgNotification(String id, String owner, String text) {
        super(id, owner, text);
    }
}
