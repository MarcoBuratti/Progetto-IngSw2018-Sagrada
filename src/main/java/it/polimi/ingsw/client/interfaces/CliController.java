package it.polimi.ingsw.client.interfaces;

public interface CliController {
    boolean nameController(String s);
    boolean connectionController(String s);
    boolean ipController(String s);
    boolean portController(String s);
    boolean schemeController(String s);
}