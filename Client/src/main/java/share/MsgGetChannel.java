package share;

import server.Discord;

public class MsgGetChannel extends Msg{
    private ChatChannel chatChannel;
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgGetChannel(String id, String owner, String text, ChatChannel chatChannel) {
        super(id, owner, text);
        this.chatChannel = chatChannel;
    }

    public ChatChannel getChatChannel() {
        return chatChannel;
    }

    public void setChatChannel(ChatChannel chatChannel) {
        this.chatChannel = chatChannel;
    }

    @Override
    public String run() {
        System.out.println(getId() + " " + getOwner() + " " + getText());
        MsgGetChannel send = new MsgGetChannel(getId(), getOwner(), getText(), new ChatChannel(Discord.dataBase.getChannel(getText())));
        Discord.msgSendToClient(getId(), send);
        return null;
    }
}
