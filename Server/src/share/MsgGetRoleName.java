package share;

import server.Discord;

public class MsgGetRoleName extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgGetRoleName(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        String role = Discord.dataBase.getRoles(getText(), getOwner());
        Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(), role));
        return null;
    }
}
