package share;

import server.Discord;

public class MsgRemoveFriend extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgRemoveFriend(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        Discord.dataBase.getClient(getOwner()).getFriends().remove(getText());
        Discord.dataBase.getClient(getText()).getFriends().remove(getOwner());
        return null;
    }
}
