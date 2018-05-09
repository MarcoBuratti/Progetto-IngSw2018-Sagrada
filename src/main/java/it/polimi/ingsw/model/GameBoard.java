package it.polimi.ingsw.model;

import it.polimi.ingsw.model.achievement.*;

import java.util.*;

public class GameBoard {
    private static final int NUMBER_OF_PUBLIC_ACHIEVEMENTS = 3;
    private RoundTrack roundTrack;
    private ArrayList<Player> players = new ArrayList<>();
    private DiceBag diceBag;
    private ArrayList<CardAchievement> publicAchievements = new ArrayList<>();

    public GameBoard (Map<String, String> schemes) {

        this.roundTrack = new RoundTrack();
        this.diceBag = new DiceBag();

        List<PublicAchievementNames> publicAchievementList = Arrays.asList(PublicAchievementNames.values());
        Collections.shuffle(publicAchievementList);
        AbstractCardFactory abstractFactory = new CardFactory();
        for ( int i = 0; i < NUMBER_OF_PUBLIC_ACHIEVEMENTS; i++ ) {
            CardAchievement publicAchievementsFactory = abstractFactory.getCardAchievement(publicAchievementList.get(i));
           this.publicAchievements.add(publicAchievementsFactory);
        }

        List<Color> privateAchievementsList = Arrays.asList(Color.values());
        Collections.shuffle(privateAchievementsList);
        int i = 0;

        for ( String key: schemes.keySet() ) {
            players.add(new Player(key, new Dashboard(schemes.get(key)), new PrivateAchievement(privateAchievementsList.get(i))));
            i++;
        }
    }

    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    public DiceBag getDiceBag() {
        return diceBag;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<CardAchievement> getPublicAchievements() {
        return publicAchievements;
    }
}