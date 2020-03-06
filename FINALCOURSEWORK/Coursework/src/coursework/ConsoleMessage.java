package coursework;
import java.io.Serializable;

// M.B. - to put comment here 
public class ConsoleMessage implements Serializable {
    protected static final long serialVersionUID = 7777L;
    static final int online = 0, stringMessage = 1, logout = 2;
    private final int type;
    private final String message;

    ConsoleMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }
    int getType() {
        return type;
    }
    String getMessage() {
        return message;
    }
}
