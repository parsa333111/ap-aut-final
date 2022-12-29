package share;

import server.Discord;

public class MsgRemoveUserFromServer extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgRemoveUserFromServer(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        Discord.dataBase.removeUserFromServer(getOwner(), getText());
        return null;
    }
}
