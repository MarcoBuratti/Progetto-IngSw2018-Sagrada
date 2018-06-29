package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.model.Color;

public enum ToolNames {
    COPPER_FOIL_BURNISHER("Alesatore_per_lamina_di_rame", Color.RED),   //0 OK
    CORK_BAKED_STRAIGHTEDGE("Riga_in_Sughero", Color.YELLOW),           //1
    EGLOMISE_BRUSH("Pennello_per_Eglomise", Color.BLUE),                //2 OK
    FLUX_BRUSH("Pennello_per_Pasta_Salda", Color.VIOLET),               //3
    FLUX_REMOVER("Diluente_per_Pasta_Salda", Color.VIOLET),             //4
    GLAZING_HAMMER("Martelletto", Color.BLUE),                          //5 OK
    GRINDING_STONE("Tampone_Diamantato", Color.GREEN),                  //6
    GROZING_PLIERS("Pinza_Sgrossatrice", Color.VIOLET),                 //7 OK
    LATHEKIN("Lathekin", Color.YELLOW),                                 //8
    LENS_CUTTER("Taglierina_Circolare", Color.GREEN),                   //9 OK
    RUNNING_PLIERS("Tenaglia_a_Rotelle", Color.RED),                    //10
    TAP_WHEEL("Taglierina_Manuale", Color.BLUE);                        //11

    private final Color color;
    private final String name;

    ToolNames(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public Color getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}

