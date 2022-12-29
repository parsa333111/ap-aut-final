package share;

import server.Discord;

public class MsgRemoveBlock extends Msg{

    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgRemoveBlock(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        Discord.dataBase.getClient(getOwner()).getBlocks().remove(getText());
        return null;
    }
}
