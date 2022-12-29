package share;

import server.DataBase;
import server.Discord;

public class MsgSingInUser extends Msg {
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgSingInUser(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        DataBase dataBase = Discord.dataBase;
        String username = getOwner();
        String password = getText();
        if (!dataBase.checkClient(username)) {
            Discord.msgSendToClient(getId(), new MsgClientGet(getId(), getOwner(), "InvalidUsername", null));
        } else if (dataBase.checkPassword(username, password)) {
            Discord.msgSendToClient(getId(), new MsgClientGet(getId(), getOwner(), "SuccessfullyUpdate", new ClientData(dataBase.getClient(username))));
            return "True";
        } else {
            Discord.msgSendToClient(getId(), new MsgClientGet(getId(), getOwner(), "InvalidPassword", null));
        }
        return "False";
    }
}
