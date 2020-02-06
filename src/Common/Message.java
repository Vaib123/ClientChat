package Common;

import java.io.Serializable;

public class Message implements Serializable {  //this class is for constructing a message
    String from;
    String to;
    String text;
    public Message(String from, String to, String text) {
        this.from = from;
        this.to = to;
        this.text = text;
        System.out.println("MyMessage");
        System.out.println(from);
        System.out.println(to);
        System.out.println(text);
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getText() {
        return text;
    }
}
