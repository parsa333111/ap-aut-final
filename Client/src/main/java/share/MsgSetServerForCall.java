package share;

import server.OpenThread;
import server.StackServer;

public class MsgSetServerForCall extends Msg{
    /**
     * constructor
     *
     * @param id    of msg
     * @param owner of msg
     * @param text  of msg
     */
    public MsgSetServerForCall(String id, String owner, String text) {
        super(id, owner, text);
    }

    @Override
    public String run() {
        StackServer stackServer = new StackServer();
        Thread thread = new Thread(new StackServer());
        thread.start();
        OpenThread.addThread(thread);
        return null;
    }
}
