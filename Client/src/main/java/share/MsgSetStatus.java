package share;

import server.Discord;

public class MsgSetStatus extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgSetStatus(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        Discord.dataBase.changeStatus(getOwner(), getText());
        return null;
    }
}
