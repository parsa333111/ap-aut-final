package share;

import server.Discord;

public class MsgChatMassage extends Msg{
    private String chatId;
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     * @param chatId of msg
     */
    public MsgChatMassage(String id, String owner, String text, String chatId) {
        super(id, owner, text);
        this.chatId = chatId;
    }

    @Override
    public String run() {
        Discord.dataBase.newMassage(chatId, getOwner(), getText());
        Discord.sendToAllInChat(getId(), this);
        return null;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
