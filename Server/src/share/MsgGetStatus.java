package share;

import server.Discord;

public class MsgGetStatus extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgGetStatus(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(), Discord.dataBase.getStatus(getOwner())));
        return null;
    }
}
