package it.polimi.ingsw.server.model;

public enum SchemesEnum {

    AURORA_SAGRADIS("Aurora_Sagradis"), AURORAE_MAGNIFICUS("Aurorae_Magnificus"), BATLLO("Batllo"), BELLESGUARD("Bellesguard"),
    CHROMATIC_SPLENDOR("Chromatic_Splendor"), COMITAS("Comitas"), FIRELIGHT("Firelight"), FIRMITAS("Firmitas"), FRACTAL_DROPS("Fractal_Drops"),
    FULGOR_DEL_CIELO("Fulgor_del_Cielo"), GRAVITAS("Gravitas"), INDUSTRIA("Industria"), KALEIDOSCOPIC_DREAM("Kaleidoscopic_Dream"), LUX_ASTRAM("Lux_Astram"), LUX_MUNDI("Lux_Mundi"),
    LUX_CELESTIAL("Luz_Celestial"), RIPPLES_OF_LIGHT("Ripples_of_Light"), SHADOW_THIEF("Shadow_Thief"), SUNS_GLORY("Sun's_Glory"), SUN_CATCHER("Sun_Catcher"),
    SYMPHONY_OF_LIGHT("Symphony_of_Light"), VIA_LUX("Via_Lux"), VIRTUS("Virtus"), WATER_OF_LIFE("Water_of_Life");

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