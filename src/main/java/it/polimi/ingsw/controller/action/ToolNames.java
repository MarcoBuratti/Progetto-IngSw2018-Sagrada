package it.polimi.ingsw.controller.action;

import it.polimi.ingsw.model.Color;

public enum ToolNames {
    COPPER_FOIL_BURNISHER("Copper Foil Burnisher",Color.RED),
    //CORK_BAKED_STRAIGHTEDGE("Cork Baked Straightedge",),
    EGLOMISE_BRUSH("Eglomise Brush",Color.BLUE),
    FLUX_BRUSH("Flux Brush",Color.VIOLET),
    FLUX_REMOVER("Flux Remover",Color.VIOLET),
    GLAZING_HAMMER("Glazing Hammer",Color.BLUE),
    GRINDING_STONE("Grinding Stone",Color.GREEN),
    GROZING_PLIERS("Grozing Pliers",Color.VIOLET),
    LATHEKIN("Lathekin",Color.YELLOW),
    LENS_CUTTER("Lens Cutter",Color.GREEN),
    //RUNNING_PLIERS("Running Pliers",),
    TAP_WHEEL("Tap Wheel",Color.BLUE);

    private final Color color;
    private final String name;

    ToolNames(String name,Color color){
        this.name=name;
        this.color=color;
    }

    public String getName(){
        return this.name;
    }

    public Color getColor(){return this.color;}
    @Override
    public String toString(){
        return this.getName();
    }
}

