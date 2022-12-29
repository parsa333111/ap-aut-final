package share;

import server.Discord;

public class MsgSendRequest extends Msg {
    String friend;
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgSendRequest(String id, String owner, String text, String friend) {
        super(id, owner, text);
        this.friend = friend;
    }

    @Override
    public String run() {
        ChatChannel channel = Discord.dataBase.getPrivateChatChannel(getOwner(), friend);
        System.out.println("new Massage Add server + " + " " + getOwner() + " " + getText());
        channel.getMassages().add(new Massage(getOwner(), getText(), "AddServer"));
        return null;
    }
}
