package com.ludavault.LudaVault;

public class Message {
    private String type;
    private String message;

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
