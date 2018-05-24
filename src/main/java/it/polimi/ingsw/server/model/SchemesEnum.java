package it.polimi.ingsw.server.model;

public enum SchemesEnum {

    AURORA_SAGRADIS("Aurora Sagradis"), AURORAE_MAGNIFICUS("Aurorae Magnificus"), BATLLO("Batllo"), BELLESGUARD("Bellesguard"),
    CHROMATIC_SPLENDOR("Chromatic Splendor"), COMITAS("Comitas"), FIRELIGHT("Firelight"), FIRMITAS("Firmitas"), FRACTAL_DROPS("Fractal Drops"),
    FULGOR_DEL_CIELO("Fulgor del Cielo"), GRAVITAS("Gravitas"), INDUSTRIA("Industria"), KALEIDOSCOPIC_DREAM("Kaleidoscopic Dream"), LUX_ASTRAM("Lux Astram"), LUX_MUNDI("Lux Mundi"),
    LUX_CELESTIAL("Lux Celestial"), RIPPLES_OF_LIGHT("Ripples of Light"), SHADOW_THIEF("Shadow Thief"), SUNS_GLORY("Sun's Glory"),
    SYMPHONY_OF_LIGHT("Symphony of Light"), VIA_LUX("Via Lux"), VIRTUS("Virtus"), WATER_OF_LIFE("Water of Life");

    private String name;

    SchemesEnum(String name) {
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