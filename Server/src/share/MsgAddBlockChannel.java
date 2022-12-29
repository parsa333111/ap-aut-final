package share;

import server.Discord;

public class MsgAddBlockChannel extends Msg {
    private String chatId;
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgAddBlockChannel(String id, String owner, String text, String chatId) {
        super(id, owner, text);
        this.chatId = chatId;
    }

    @Override
    public String run() {
        Discord.dataBase.addBlockChannelFromUser(getOwner(), getText(), chatId);
        return null;
    }
}
