package share;

import server.Discord;

public class MsgByeFromChat extends MsgGetChannel{
    /**
     * constructor
     *
     * @param id          of msg
     * @param owner       of msg
     * @param text        of msg
     * @param chatChannel
     */
    public MsgByeFromChat(String id, String owner, String text, ChatChannel chatChannel) {
        super(id, owner, text, chatChannel);
    }

    @Override
    public String run() {
        Discord.dataBase.getClient(getOwner()).setCurrentChat("");
        Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(), "SuccessfullyOutFromChat"));
        return null;
    }
}
