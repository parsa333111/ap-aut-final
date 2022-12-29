package share;

import server.Discord;

public class MsgRemoveMassage extends Msg{
    private int index;
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgRemoveMassage(String id, String owner, String text, int index) {
        super(id, owner, text);
        this.index = index;
    }

    @Override
    public String run() {
        Discord.dataBase.removeMassageFromChat(getText(), index);
        return null;
    }
}
