package coursework;
import java.io.Serializable;

public class ConsoleMessage implements Serializable {
    protected static final long serialVersionUID = 7777L;
    static final int online = 0, stringMessage = 1, logout = 2, commands = 3, coordinator = 4, ping = 5;
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
