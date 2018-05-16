package it.polimi.ingsw.model.rmi.interfaces;

import it.polimi.ingsw.model.DiceBag;
import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.RoundTrack;
import it.polimi.ingsw.model.achievement.CardAchievement;

import java.rmi.Remote;
import java.util.ArrayList;

public interface GameBoardInterface extends Remote {

    RoundTrack getRoundTrack();
    DiceBag getDiceBag();
    ArrayList<Player> getPlayers();
    ArrayList<CardAchievement> getPublicAchievements();
    ArrayList<Die> getDraftPool();
}
