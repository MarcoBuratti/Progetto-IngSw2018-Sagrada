package it.polimi.ingsw.server.model;

public enum SchemeCardsEnum {
    CARD1("Aurorae_Magnificus", "Aurora_Sagradis"), CARD2("Bellesguard", "Batllo"), CARD3("Comitas", "Chromatic_Splendor"),
    CARD4("Firelight", "Sun's_Glory"), CARD5("Fractal_Drops", "Ripples_of_Light"), CARD6("Fulgor_del_Cielo", "Luz_Celestial"),
    CARD7("Industria", "Via_Lux"), CARD8("Kaleidoscopic_Dream", "Firmitas"), CARD9("Lux_Mundi", "Lux_Astram"),
    CARD10("Sun_Catcher", "Shadow_Thief"), CARD11("Virtus", "Symphony_of_Light"), CARD12("Water_of_Life", "Gravitas");

    private String firstScheme;
    private String secondScheme;

    SchemeCardsEnum (String name1, String name2) {
        this.firstScheme = name1;
        this.secondScheme = name2;
    }

    public String getFirstScheme () {
        return this.firstScheme;
    }

    public String getSecondScheme () {
        return this.secondScheme;
    }

    @Override
    public String toString() {
        return "First Scheme: " + this.firstScheme + " , Second Scheme: " + this.secondScheme;
    }
}
