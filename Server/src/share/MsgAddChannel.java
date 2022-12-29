package share;

import server.Discord;

public class MsgAddChannel extends Msg {
    private String chatId;
    /**
     * constructor
     *
     * @param id     of msg
     * @param owner  of msg
     * @param text   of msg
     * @param chatId of msg
     */
    public MsgAddChannel(String id, String owner, String text, String chatId) {
        super(id, owner, text);
        this.chatId = chatId;
    }

    @Override
    public String run() {
        Discord.dataBase.addChannelToServer(getText(), chatId);
        return null;
    }
}
