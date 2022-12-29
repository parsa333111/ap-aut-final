package share;

import server.Discord;

public class MsgIsOnline extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgIsOnline(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        if(Discord.IsOnline(getOwner()))
            Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(), "Online"));
        else
            Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(), "Offline"));
        return null;
    }
}
