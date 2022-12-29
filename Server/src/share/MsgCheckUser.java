package share;

import server.Discord;

public class MsgCheckUser extends Msg {
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgCheckUser(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        if(!Discord.dataBase.checkClient(getText()))
            Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(), "False"));
        else
            Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(), "True"));
        return null;
    }
}
