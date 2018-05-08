package it.polimi.ingsw.model.achievement;

public class CardFactory extends AbstractFactory {

    @Override
    CardAchievement getCardAchievement (PublicAchievementNames publicAchievementNames) {

        if(publicAchievementNames == PublicAchievementNames.COLORDIAGONALS)
            return new ColorDiagonals();
        else if(publicAchievementNames == PublicAchievementNames.COLORVARIETY)
            return new ColorVariety();
        else if(publicAchievementNames == PublicAchievementNames.COLUMNCOLORVARIETY)
            return new ColumnColorVariety();
        else if(publicAchievementNames == PublicAchievementNames.COLUMNSHADEVARIETY)
            return new ColumnShadeVariety();
        else if(publicAchievementNames == PublicAchievementNames.ROWCOLORVARIETY)
            return new RowColorVariety();
        else if(publicAchievementNames == PublicAchievementNames.ROWSHADEVARIETY)
            return new RowShadeVariety();
        else if(publicAchievementNames == PublicAchievementNames.SHADE12)
            return new Shades(1,2);
        else if(publicAchievementNames == PublicAchievementNames.SHADE34)
            return new Shades(3,4);
        else if(publicAchievementNames == PublicAchievementNames.SHADE56)
            return new Shades(5,6);
        else if(publicAchievementNames == PublicAchievementNames.SHADEVARIETY)
            return new ShadeVariety();
        return null;
    }
}
