package share;

import server.Discord;

public class MsgEditMassage extends Msg{
    private int index;
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgEditMassage(String id, String owner, String text, int index) {
        super(id, owner, text);
        this.index = index;
    }

    @Override
    public String run() {
        Discord.dataBase.editMassageFromChat(getText(), index, getOwner());
        return null;
    }
}
