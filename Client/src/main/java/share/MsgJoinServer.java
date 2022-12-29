package share;

import server.Discord;

public class MsgJoinServer extends Msg{

    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgJoinServer(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        if(Discord.dataBase.checkServer(getText())) {
            if(Discord.dataBase.addClientToServer(getOwner(), getText())) {
                Server server = Discord.dataBase.getServer(getText());
                ChatChannel chatChannel = server.getChatChannels().get(0);
                chatChannel.getMassages().add(new Massage(getOwner(), "Hello To server" + getOwner(), chatChannel.getId()));
                System.out.println(chatChannel.getName() + " ");
                for(Massage massage : chatChannel.getMassages())
                    System.out.println(massage.getSender() + " " + massage.getText());
                //Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(),  "SuccessfullyAddToServer"));
            }
            else {
                //Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(),  "ClientBeforeInServer"));
            }
        }
        else {
            //Discord.msgSendToClient(getId(), new Msg(getId(), getOwner(),  "ServerNotFound"));
        }
        return null;
    }
}
