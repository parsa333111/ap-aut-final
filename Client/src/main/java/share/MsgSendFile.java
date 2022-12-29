package share;

public class MsgSendFile extends Msg{
    String idFile;

    byte[] bytes;
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgSendFile(String id, String owner, String text, String idFile, byte[] bytes) {
        super(id, owner, text);
        this.idFile = idFile;
        this.bytes = bytes;
    }

    public String getIdFile() {
        return idFile;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
