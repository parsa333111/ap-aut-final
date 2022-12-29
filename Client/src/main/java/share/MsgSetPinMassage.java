package share;

import server.Discord;

public class MsgSetPinMassage extends MsgChatMassage{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     * @param chatId of msg
     */
    public MsgSetPinMassage(String id, String owner, String text, String chatId) {
        super(id, owner, text, chatId);
    }

    @Override
    public String run() {
        Discord.dataBase.newPinMassage(getChatId(), getOwner(), getText());
        //this.setText("New Pin Massage : " + getText());
        //Discord.sendToAllInChat(getChatId(), this);
        return null;
    }
}
