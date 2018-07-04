package it.polimi.ingsw.server.controller.tool;

public interface AbstractToolFactory {
    /**
     * Returns a tool created by using the toolName parameter.
     * @param toolName an Enum instance represeting the name of one of the tools
     * @return a Tool Object representing the chosen tool
     */
    Tool getTool(ToolNames toolName);
}
