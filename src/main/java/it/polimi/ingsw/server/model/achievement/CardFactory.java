package it.polimi.ingsw.server.model.achievement;

public class CardFactory extends AbstractCardFactory {

    @Override
    public CardAchievement extractCardAchievement(PublicAchievementNames publicAchievementNames) {

        switch (publicAchievementNames) {
            case COLORDIAGONALS:
                return new ColorDiagonals();
            case COLORVARIETY:
                return new ColorVariety();
            case COLUMNCOLORVARIETY:
                return new ColumnColorVariety();
            case COLUMNSHADEVARIETY:
                return new ColumnShadeVariety();
            case ROWCOLORVARIETY:
                return new RowColorVariety();
            case ROWSHADEVARIETY:
                return new RowShadeVariety();
            case SHADE12:
                return new Shades(1, 2);
            case SHADE34:
                return new Shades(3, 4);
            case SHADE56:
                return new Shades(5, 6);
            case SHADEVARIETY:
                return new ShadeVariety();
            default:
                throw new IllegalArgumentException();
        }
    }
}
