package share;

import server.Discord;

public class MsgAddRole extends Msg{
    private String chatId;
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgAddRole(String id, String owner, String text, String chatId) {
        super(id, owner, text);
        this.chatId = chatId;
    }

    @Override
    public String run() {
        Discord.dataBase.setRoleName(getOwner(), getText(), chatId);
        return null;
    }
}
