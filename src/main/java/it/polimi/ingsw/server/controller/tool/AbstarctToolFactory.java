package it.polimi.ingsw.server.controller.tool;

public abstract class AbstarctToolFactory {
    public abstract Tool getTool(ToolNames toolName);
}
