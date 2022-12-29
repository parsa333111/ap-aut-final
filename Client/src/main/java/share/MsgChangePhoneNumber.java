package share;

import server.Discord;

public class MsgChangePhoneNumber extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgChangePhoneNumber(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        Discord.dataBase.getClient(getOwner()).setPhoneNumber(getText());
        return null;
    }
}
