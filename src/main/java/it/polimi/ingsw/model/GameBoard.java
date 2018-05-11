package it.polimi.ingsw.model;

import it.polimi.ingsw.model.achievement.*;
import it.polimi.ingsw.model.exception.NotValidValueException;

import java.util.*;

public class GameBoard {
    private static final int NUMBER_OF_PUBLIC_ACHIEVEMENTS = 3;
    private RoundTrack roundTrack;
    private ArrayList<Player> players = new ArrayList<>();
    private DiceBag diceBag;
    private ArrayList<CardAchievement> publicAchievements = new ArrayList<>();
    private ArrayList<Die> diceStock = new ArrayList<>();

    public GameBoard (Map<String, String> schemes) throws NotValidValueException {

        this.roundTrack = new RoundTrack();
        this.diceBag = new DiceBag();

        List<PublicAchievementNames> publicAchievementList = Arrays.asList(PublicAchievementNames.values());
        Collections.shuffle(publicAchievementList);
        AbstractCardFactory abstractFactory = new CardFactory();
        for ( int i = 0; i < NUMBER_OF_PUBLIC_ACHIEVEMENTS; i++ ) {
            CardAchievement publicAchievementsFactory = abstractFactory.extractCardAchievement(publicAchievementList.get(i));
           this.publicAchievements.add(publicAchievementsFactory);
        }

        List<Color> privateAchievementsList = Arrays.asList(Color.values());
        Collections.shuffle(privateAchievementsList);
        int i = 0;

        for ( Map.Entry<String, String> elem: schemes.entrySet() ) {
            players.add(new Player(elem.getKey(), new Dashboard(elem.getValue()), new PrivateAchievement(privateAchievementsList.get(i))));
            players.get(i).getDashboard().setOwner(players.get(i));
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

    public ArrayList<Die> getDiceStock() {
        return new ArrayList<>(this.diceStock);
    }

    public void setDiceStock(ArrayList<Die> diceStock) {
        this.diceStock.addAll(diceStock);
    }

    public void removeDieFromDiceStock ( Die die ){
        if ( die != null )
            this.diceStock.remove(diceStock.indexOf(die));
    }
}