package share;

import server.Discord;

public class MsgCurrentChat extends MsgGetChannel{
    /**
     * constructor
     *
     * @param id          of msg
     * @param owner       of msg
     * @param text        of msg
     * @param chatChannel
     */
    public MsgCurrentChat(String id, String owner, String text, ChatChannel chatChannel) {
        super(id, owner, text, chatChannel);
    }

    @Override
    public String run() {
        ChatChannel chatChannel = Discord.dataBase.getChannel(getText());
        Discord.dataBase.getClient(getOwner()).setCurrentChat(chatChannel.getId());
        return null;
    }
}
