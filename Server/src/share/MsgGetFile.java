package share;

public class MsgGetFile extends Msg {
    byte[] bytes;
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgGetFile(String id, String owner, String text) {
        super(id, owner, text);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
