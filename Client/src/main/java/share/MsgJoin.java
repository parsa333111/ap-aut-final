package share;

public class MsgJoin extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgJoin(String id, String owner, String text) {
        super(id, owner, text);
    }
}
