package share;

import server.Discord;

public class MsgChangeServerName extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgChangeServerName(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        Discord.dataBase.changeServerName(getOwner(), getText());
        return null;
    }
}
