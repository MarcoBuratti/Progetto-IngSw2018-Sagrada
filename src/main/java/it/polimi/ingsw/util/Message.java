package it.polimi.ingsw.util;

import java.io.Serializable;

public class Message implements Serializable {

    private String content;

    /**
     * Creates a Message Object, a serializable class having just a String as attribute.
     * @param content a String containing the message the user wants to send
     */
    public Message(String content) {
        this.content = content;
    }

    /**
     * Returns the content attribute.
     * @return a String containing information
     */
    public String getContent() {
        return this.content;
    }

}