package share;

import server.Discord;

public class MsgGetRole extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgGetRole(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        String role = Discord.dataBase.getRoleNameData(getOwner(), getText());
        Msg send = new Msg(getId(), getOwner(), role);
        Discord.msgSendToClient(getId(), send);
        return null;
    }
}
