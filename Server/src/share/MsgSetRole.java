package share;

import server.Discord;

public class MsgSetRole extends Msg{
    private String chatId;
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgSetRole(String id, String owner, String text, String chatId) {
        super(id, owner, text);
        this.chatId = chatId;
    }

    @Override
    public String run() {
        Discord.dataBase.setRole(getOwner(), getText(), chatId);
        return null;
    }
}
