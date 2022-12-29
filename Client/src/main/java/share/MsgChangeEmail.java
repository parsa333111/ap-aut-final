package share;

import server.Discord;

public class MsgChangeEmail extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgChangeEmail(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        Discord.dataBase.getClient(getOwner()).setEmail(getText());
        return null;
    }
}
