package share;

import server.DataBase;
import server.Discord;

public class MsgAddFriend extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgAddFriend(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        DataBase dataBase = Discord.dataBase;
        System.out.println("New Add Friend" + getOwner() + " " + getText());
        dataBase.getClient(getOwner()).getFriends().add(getText());
        dataBase.getClient(getText()).getFriends().add(getOwner());
        dataBase.getClient(getOwner()).getFriendRequests().remove(getText());
        return null;
    }
}
