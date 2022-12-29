package share;

import java.io.Serializable;
/**
 * this class transfer between client and server for communication
 */
public class Msg implements Serializable {
    private String id;
    private String text;
    private String owner;

    /**
     * constructor
     * @param id of msg
     * @param owner of msg
     * @param text of msg
     */
    public Msg(String id, String owner, String text) {
        this.id = id;
        this.owner = owner;
        this.text = text;
    }

    public String run() {
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}