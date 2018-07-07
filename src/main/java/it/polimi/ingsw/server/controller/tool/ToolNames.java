package it.polimi.ingsw.server.controller.tool;

public enum ToolNames {
    COPPER_FOIL_BURNISHER("Alesatore_per_lamina_di_rame"),   //0 OK DEFINITIVO
    CORK_BAKED_STRAIGHTEDGE("Riga_in_Sughero"),           //1 OK DEFINITIVO
    EGLOMISE_BRUSH("Pennello_per_Eglomise"),                //2 OK DEFINITIVO
    FLUX_BRUSH("Pennello_per_Pasta_Salda"),               //3 OK DEFINITIVO
    FLUX_REMOVER("Diluente_per_Pasta_Salda"),             //4 OK DEFINITIVO
    GLAZING_HAMMER("Martelletto"),                          //5 OK DEFINITIVO
    GRINDING_STONE("Tampone_Diamantato"),                  //6 OK DEFINITIVO
    GROZING_PLIERS("Pinza_Sgrossatrice"),                 //7 OK DEFINITIVO
    LATHEKIN("Lathekin"),                                 //8 OK DEFINITIVO
    LENS_CUTTER("Taglierina_Circolare"),                   //9 OK DEFINITIVO
    RUNNING_PLIERS("Tenaglia_a_Rotelle"),                    //10 OK DEFINITIVO
    TAP_WHEEL("Taglierina_Manuale");                        //11 OK DEFINITIVO

    private final String name;

    ToolNames(String name) {
        this.name = name;

    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}

