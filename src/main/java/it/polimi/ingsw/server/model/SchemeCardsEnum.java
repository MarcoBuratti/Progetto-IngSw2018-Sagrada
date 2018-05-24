package it.polimi.ingsw.server.model;

public enum SchemeCardsEnum {
    CARD1("Aurorae Magnificus", "Aurora Sagradis"), CARD2("Bellesguard", "Batllo"), CARD3("Comitas", "Chromatic Splendor"),
    CARD4("Firelight", "Sun's Glory"), CARD5("Fractal Drops", "Ripples of Light"), CARD6("Fulgor del Cielo", "Luz Celestial"),
    CARD7("Industria", "Via Lux"), CARD8("Kaleidoscopic Dream", "Firmitas"), CARD9("Lux Mundi", "Lux Astram"),
    CARD10("Sun Catcher", "Shadow Thief"), CARD11("Virtus", "Symphony of Light"), CARD12("Water of Life", "Gravitas");

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
