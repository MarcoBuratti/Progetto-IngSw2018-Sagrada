package it.polimi.ingsw.controller;

public class Timer implements Runnable{

    private int time;
    private Turn turn;

    public Timer(Turn turn) {
        this.time = 100*1000;
        this.turn = turn;
    }

    @Override
    public void run() {

        try {
            Thread.sleep(time);
            if(!turn.isTurnIsOver()) {
                turn.setTurnIsOver(true);
                notifyAll();
            }
            } catch (InterruptedException e) {
                e.printStackTrace();
        }

    }
}
