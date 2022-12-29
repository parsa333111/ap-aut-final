package share;

import server.Discord;

public class MsgGetServer extends Msg {
    private Server server;
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgGetServer(String id, String owner, String text, Server server) {
        super(id, owner, text);
        this.server = server;
    }

    @Override
    public String run() {
        if(Discord.dataBase.checkServer(getText())) {
            MsgGetServer send = new MsgGetServer(getId(), getOwner(), "ReceiveServer", new Server(Discord.dataBase.getServer(getText())));
            Discord.msgSendToClient(getId(), send);
        }
        else {
            Discord.msgSendToClient(getId(), new MsgGetServer(getId(), getOwner(), "ServerNotFound", null));
        }
        return null;
    }

    public Server getServer() {
        return server;
    }
}
