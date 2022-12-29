package share;

import server.Discord;

public class MsgGetPrivateChat extends Msg{
    /**
     * constructor
     *
     * @param id          of msg
     * @param owner       of msg
     * @param text        of msg
     */
    public MsgGetPrivateChat(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        ChatChannel chatChannel = Discord.dataBase.getPrivateChatChannel(getOwner(), getText());
        //Discord.dataBase.getClient(getOwner()).setCurrentChat(chatChannel.getId());
        Discord.msgSendToClient(getId(), new MsgGetChannel(getId(), getOwner(), "GetChannel", new ChatChannel(chatChannel)));
        return null;
    }
}
