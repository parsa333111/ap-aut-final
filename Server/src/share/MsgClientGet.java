package share;

public class MsgClientGet extends Msg {
    private ClientData clientData;
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgClientGet(String id, String owner, String text, ClientData clientData) {
        super(id, owner, text);
        this.clientData = clientData;
    }

    public ClientData getClientData() {
        return clientData;
    }

    public void setClientData(ClientData clientData) {
        this.clientData = clientData;
    }
}
