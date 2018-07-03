package it.polimi.ingsw.server.model.achievement;

public interface AbstractCardFactory {

    /**
     * An achievement is selected from the corresponding PublicAchievementNames Enum instance and returned as a CardAchievement Object.
     * @param publicAchievementNames the PublicAchievementNames Enum instance corresponding to the achievement the user wants to extract
     * @return a CardAchievement Object representing an achievement
     */
    CardAchievement extractCardAchievement(PublicAchievementNames publicAchievementNames);
}

