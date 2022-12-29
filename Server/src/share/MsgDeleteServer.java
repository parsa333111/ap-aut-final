package share;

import server.Discord;

public class MsgDeleteServer extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgDeleteServer(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        Discord.dataBase.removeServer(getOwner());
        return null;
    }
}
