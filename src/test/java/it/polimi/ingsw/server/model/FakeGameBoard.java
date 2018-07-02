package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.tool.Tool;

import java.util.ArrayList;

public class FakeGameBoard extends GameBoard {

    private ArrayList<Tool> tools;

    /**
     * {@inheritDoc}
     */
    public FakeGameBoard(ArrayList<Player> players) {
        super(players);
    }

    /**
     * We need to override this method as the attribute tools is private in GameBoard
     * @return an ArrayList containing the extracted tools
     */
    @Override
    public ArrayList<Tool> getTools() {
        return this.tools;
    }

    /**
     * Allows the user to set the selected tools instead of extracting them randomly.
     * @param tools an ArrayList containing the tools the user wants to use
     */
    public void setTools(ArrayList<Tool> tools) {
        this.tools = tools;

    }
}
