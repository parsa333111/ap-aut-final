package share;

import server.Discord;

public class MsgGetTime extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgGetTime(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(), Discord.dataBase.getTimeClientFromServer(getOwner(), getText())));
        return null;
    }
}
