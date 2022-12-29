package share;

import server.Discord;

public class MsgAddUser extends MsgClientGet {
    /**
     * constructor
     *
     * @param id         of msg
     * @param owner      of msg
     * @param text       of msg
     * @param clientData
     */
    public MsgAddUser(String id, String owner, String text, ClientData clientData) {
        super(id, owner, text, clientData);
    }

    @Override
    public String run() {
        System.out.println("gav");
        Discord.dataBase.getClientDatas().add(getClientData());
        return null;
    }
}
