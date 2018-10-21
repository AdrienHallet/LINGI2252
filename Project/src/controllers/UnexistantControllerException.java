package controllers;

public class UnexistantControllerException extends Exception {

    private String type;

    public UnexistantControllerException(String unknownController){
        this.type = unknownController;
    }

    public String toString(){
        return String.format("%s (Unknown type: %s)", super.toString(), type);
    }
}
