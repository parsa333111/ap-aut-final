package share;

import server.DataBase;
import server.Discord;

public class MsgRequestFriend extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgRequestFriend(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        DataBase dataBase = Discord.dataBase;
        if (!dataBase.checkClient(getText())) {
            Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(), "UsernameNotFound"));
        } else if(dataBase.checkRequest(dataBase.getClient(getText()), getOwner())) {
            Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(), "AddedRequest"));
        } else if (dataBase.checkFriend(dataBase.getClient(getOwner()), getText())) {
            Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(), "AddBefore"));
        } else if (dataBase.checkBlock(dataBase.getClient(getText()), getOwner())) {
            Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(), "BlockUser"));
        } else {
            dataBase.getClient(getText()).getFriendRequests().add(getOwner());
            Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(), "RequestSend"));
        }
        return null;
    }
}
