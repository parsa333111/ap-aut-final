package share;

import server.Discord;

public class MsgRemoveRequest extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgRemoveRequest(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        Discord.dataBase.getClient(getOwner()).getFriendRequests().remove(getText());
        return null;
    }
}
