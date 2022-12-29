package share;

import server.Discord;

public class MsgGivePhoto extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgGivePhoto(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        String photo = Discord.dataBase.showPhoto(getOwner());
        Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(), photo));
        return null;
    }
}
