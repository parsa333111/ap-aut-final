package share;

import server.Discord;

public class MsgChangePassword extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgChangePassword(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        Discord.dataBase.getClient(getOwner()).setPassword(getText());
        return null;
    }
}
