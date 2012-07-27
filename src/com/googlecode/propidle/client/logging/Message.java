package com.googlecode.propidle.client.logging;



public class Message {
    private final String message;

    private Message(String message){
        this.message = message;
    }

    public static Message message(String message) {
        return new Message(message);
    }

    @Override
    public String toString() {
        return message;
    }
}
