package share;

import server.OpenThread;

public class MsgDeleteCallThread extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgDeleteCallThread(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        OpenThread.delete();
        return null;
    }
}
