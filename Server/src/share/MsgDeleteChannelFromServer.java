package share;

import server.Discord;

public class MsgDeleteChannelFromServer extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgDeleteChannelFromServer(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        Discord.dataBase.deleteChannelFromServer(getOwner(), getText());
        return null;
    }
}
