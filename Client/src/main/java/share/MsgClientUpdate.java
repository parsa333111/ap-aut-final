package share;

import server.DataBase;
import server.Discord;

public class MsgClientUpdate extends Msg {
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgClientUpdate(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        DataBase dataBase = Discord.dataBase;
        String username = getOwner();
        String password = getText();
        if (!dataBase.checkClient(username)) {
           Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(), "InvalidUsername"));
        } else if (dataBase.checkPassword(username, password)) {
            Discord.msgSendToClient(getId(), new MsgClientGet(getId(), getOwner(), "SuccessfullyUpdate", new ClientData(dataBase.getClient(username))));
        } else {
            Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(), "InvalidPassword"));
        }
        return null;
    }
}
