package share;

import server.Discord;

public class MsgNewReaction extends Msg {
    String massageId;
    String type;
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgNewReaction(String id, String owner, String text, String massageId, String type) {
        super(id, owner, text);
        this.massageId = massageId;
        this.type = type;
    }

    @Override
    public String run() {
        Discord.dataBase.newReact(getOwner(), getText(), massageId, type);
        return null;
    }
}
