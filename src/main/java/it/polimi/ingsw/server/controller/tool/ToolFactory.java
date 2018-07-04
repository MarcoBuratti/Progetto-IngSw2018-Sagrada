package it.polimi.ingsw.server.controller.tool;

public class ToolFactory implements AbstractToolFactory {
    @Override
    public Tool getTool(ToolNames toolName) {
        switch (toolName) {
            case GROZING_PLIERS:
                return new SetDieTool(false, ToolNames.GROZING_PLIERS);
            case EGLOMISE_BRUSH:
                return new ReplaceDieTool(false, true, false, false, ToolNames.EGLOMISE_BRUSH);
            case COPPER_FOIL_BURNISHER:
                return new ReplaceDieTool(true, false, false, false, ToolNames.COPPER_FOIL_BURNISHER);
            case LATHEKIN:
                return new ReplaceDieTool(true, true, false, false, ToolNames.LATHEKIN);
            case LENS_CUTTER:
                return new ChangeDieTool(false, ToolNames.LENS_CUTTER);
            case FLUX_BRUSH:
                return new SetDieTool(true, ToolNames.FLUX_BRUSH);
            case GLAZING_HAMMER:
                return new RerollTool(false, ToolNames.GLAZING_HAMMER);
            case RUNNING_PLIERS:
                return new TwoTurnTool(false, ToolNames.RUNNING_PLIERS);
            case CORK_BAKED_STRAIGHTEDGE:
                return new SpecialPlacementTool(true, true, false, ToolNames.CORK_BAKED_STRAIGHTEDGE);
            case GRINDING_STONE:
                return new SetDieTool(false, ToolNames.GRINDING_STONE);
            case FLUX_REMOVER:
                return new ChangeDieTool(true, ToolNames.FLUX_REMOVER);
            case TAP_WHEEL:
                return new ReplaceDieTool(true, true, true, false, ToolNames.TAP_WHEEL);
            default:
                throw new IllegalArgumentException();
        }
    }
}
