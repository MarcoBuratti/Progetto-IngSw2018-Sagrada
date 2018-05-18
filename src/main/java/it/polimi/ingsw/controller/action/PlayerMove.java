package it.polimi.ingsw.controller.action;

import it.polimi.ingsw.model.Die;

import java.util.Optional;

public class PlayerMove {
    private int[] intMatrixParameters;
    private Optional<Boolean> twoReplace;
    private Optional<Boolean> needPlacement;

    private Optional<Integer> indexDie;
    private Optional<Die> die2;
    private Optional<Integer> setOnDie;

    private String typeMove;
    private Optional<String> nameTool;

    public PlayerMove(String typeMove){
        if(typeMove.equals("gothrough"))
            this.typeMove=typeMove;
        else
            throw new IllegalArgumentException();
    }

    public PlayerMove(String typeMove, int indexDie, int[] intParameters){
        if(typeMove.equals("setdie")){
            this.typeMove=typeMove;
            this.intMatrixParameters=intParameters.clone();
            this.indexDie=Optional.of(indexDie);
        }
    }

    public PlayerMove(String typeMove,int[] intParameters) {
        if (typeMove.equals("usetool")) {
            this.typeMove = typeMove;
            this.intMatrixParameters = intParameters.clone();
            if (intParameters.length > 4) {
                this.twoReplace = Optional.of(true);
            } else
                this.twoReplace = Optional.of(false);
        }
    }

    public PlayerMove(String typeMove,int indexDie,int newValue) {
        if (typeMove.equals("usetool")) {
            this.typeMove = typeMove;
            this.indexDie=Optional.of(indexDie);
            this.setOnDie=Optional.of(newValue);
        }
    }

    public Integer getIntParameters(int index) {
        if(intMatrixParameters.length>0 && index < intMatrixParameters.length)
                return intMatrixParameters[index];
        else
           throw new IllegalArgumentException();
    }

    public Optional<Boolean> getNeedPlacement() {
        return needPlacement;
    }

    public Boolean getTwoReplace() {
        if(twoReplace.isPresent())
            return twoReplace.get();
        else
            throw new IllegalArgumentException();
    }

    public Integer getIndexDie() {
        if(indexDie.isPresent())
            return indexDie.get();
        else
            throw new IllegalArgumentException();
    }

    public Optional<Die> getDie2() {
        return die2;
    }

    public Integer getSetOnDie() {
        if(setOnDie.isPresent())
            return setOnDie.get();
        else
            throw new IllegalArgumentException();
    }

    public String getTypeMove() {
        return typeMove;
    }
}


