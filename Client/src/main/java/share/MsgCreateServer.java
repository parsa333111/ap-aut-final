package share;

import server.Discord;

public class MsgCreateServer extends Msg{
    String serverId;
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgCreateServer(String id, String owner, String text, String serverId) {
        super(id, owner, text);
        this.serverId = serverId;
    }

    @Override
    public String run() {
        Server server = new Server(getText(), getOwner(), serverId);
        Discord.dataBase.addServer(server);
        Discord.dataBase.addChannelToServer(server.getId(), "General");
        Discord.dataBase.addClientToServer(getOwner(), server.getId());
        System.out.println("server create : " + server.getId() + " " + getOwner());
        return null;
    }
}
