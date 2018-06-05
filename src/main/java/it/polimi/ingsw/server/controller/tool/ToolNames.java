package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.model.Color;

public enum ToolNames {
    COPPER_FOIL_BURNISHER("Alesatore_per_lamina_di_rame", Color.RED),
    CORK_BAKED_STRAIGHTEDGE("Riga_in_Sughero", Color.YELLOW),
    EGLOMISE_BRUSH("Pennello_per_Eglomise", Color.BLUE),
    FLUX_BRUSH("Pennello_per_Pasta_Salda", Color.VIOLET),
    FLUX_REMOVER("Diluente_per_Pasta_Salda", Color.VIOLET),
    GLAZING_HAMMER("Martelletto", Color.BLUE),
    GRINDING_STONE("Tampone_Diamantato", Color.GREEN),
    GROZING_PLIERS("Pinza_Sgrossatrice", Color.VIOLET),
    LATHEKIN("Lathekin", Color.YELLOW),
    LENS_CUTTER("Taglierina_circolare", Color.GREEN),
    RUNNING_PLIERS("Tenaglia_a_Rotelle", Color.RED),
    TAP_WHEEL("Taglierina_Manuale", Color.BLUE);

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

