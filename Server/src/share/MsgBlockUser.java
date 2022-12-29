package share;

import server.Discord;

public class MsgBlockUser extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgBlockUser(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        if (!Discord.dataBase.checkClient(getText())) {
            Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(),  "UsernameNotFound"));
        } else if (Discord.dataBase.checkBlock(Discord.dataBase.getClient(getOwner()), getText())) {
            Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(),  "AddBefore"));
        } else {
            if(Discord.dataBase.checkFriend(Discord.dataBase.getClient(getOwner()), getText())) {
                Discord.dataBase.getClient(getOwner()).getFriends().remove(getText());
                Discord.dataBase.getClient(getText()).getFriends().remove(getOwner());
            }
            Discord.dataBase.getClient(getOwner()).getBlocks().add(getText());
            Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(),  "SuccessfullyAddBlockList"));
        }
        return null;
    }
}
