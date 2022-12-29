package share;

import server.Discord;

public class MsgChangePhoto extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgChangePhoto(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        Discord.dataBase.changePhoto(getOwner(), getText());
        return null;
    }
}
