package com.ludavault.LudaVault;

/**
 * Steven Pride
 * CEN 3024 - Software Development I
 * 10/22/2025
 * Message
 * Defines the Message object that stores the type of message and the message itself.
 */
public class Message {
    /**
     * Class attributes:
     *     type: String - the type of message.
     *     message: String - the message itself.
     */
    private String type;
    private String message;

    /**
     * constructor: Message
     * parameters: String type - the type of message.
     *             String message - the message itself.
     * return: Message
     * purpose: Initializes a new Message object with the provided attributes.
     */
    public Message(String type, String message) {
        this.type = type;
        this.message = message;
    }

    //Getters
    public String getType() {
        return type;
    }
    public String getMessage() {
        return message;
    }

    //Setters
    public void setType(String type) {
        this.type = type;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
