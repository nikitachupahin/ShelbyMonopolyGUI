package GameLogicGUI;


import GameLogic.Cell;
import GameLogic.Player;
import GameLogic.Property;
import GameLogic.UserInterface;

import java.util.Arrays;
import java.util.List;


public class GUI implements UserInterface {
    private MonopolyStage gameStage;

    private final int NUMBEROFREPLIES = 20;
    private Object[] reply;


    @Override
    public void notify(String msg) {
        final int ID = Request.ShowNotice.getID();
        synchronized (this){
            try{
                while (reply[ID].equals(-1)) {
                    this.gameStage.setRequests(ID, msg);
                    this.wait();
                }
                this.notifyAll();
            }catch (InterruptedException e) { e.printStackTrace(); }
        }
        System.out.println("Show notice DONE.");
        reply[ID]=-1;
    }

    @Override
    public void inform(String msg) {

    }

    @Override
    public void displayMessage(String msg) {
        final int ID = Request.DisplayMessage.getID();
        synchronized (this){
            try{
                while (reply[ID].equals(-1)) {
                    this.gameStage.setRequests(ID, msg);
                    this.wait();
                }
                this.notifyAll();
            }catch (InterruptedException e) { e.printStackTrace(); }
        }
        System.out.println("Display message DONE.");
        reply[ID]=-1;
    }



    /* [ Game Display Methods ] */
    @Override
    public void playerUpdateMoney(Player player, int amount) {
        if(amount>0){
            this.displayMessage("#"+player.getPlayerNum()+" "+player.getName()+" gets HKD"+amount+".");
        }
        else{
            this.displayMessage("#"+player.getPlayerNum()+" "+player.getName()+" loses HKD"+amount+".");
        }
    }
    @Override
    public void changePropertyOwner(Property property, Player player) {
        this.displayMessage(player.getName()+" owns "+property.getName()+".");
        final int ID = Request.Refresh.getID();
        synchronized (this){
            try{
                while (reply[ID].equals(-1)) {
                    this.gameStage.setRequests(ID, true);
                    this.wait();
                }
                this.notifyAll();
            }catch (InterruptedException e) { e.printStackTrace(); }
        }
        System.out.println("Refresh DONE.");
        reply[ID]=-1;
    }
    @Override
    public void playerGotoJail(Player player) {
        final int ID = Request.Refresh.getID();
        synchronized (this){
            try{
                while (reply[ID].equals(-1)) {
                    this.gameStage.setRequests(ID, true);
                    this.wait();
                }
                this.notifyAll();
            }catch (InterruptedException e) { e.printStackTrace(); }
        }
        System.out.println("Refresh DONE.");
        reply[ID]=-1;
    }
    @Override
    public void rollDice(int dice1, int dice2) {
        final int ID = Request.RollDice.getID();
        int[] dices=new int[2];
        dices[0]=dice1;
        dices[1]=dice2;
        synchronized (this){
            try{
                while (reply[ID].equals(-1)) {
                    this.gameStage.setRequests(ID, dices);
                    this.wait();
                }
                this.notifyAll();
            }catch (InterruptedException e) { e.printStackTrace(); }
        }
        System.out.println("Dices display DONE.");
        reply[ID]=-1;
    }
    @Override
    public void playerMove(Cell beg, Cell end, int steps) {
        this.displayMessage("Move "+steps+" steps.");
        final int ID = Request.Refresh.getID();
        synchronized (this){
            try{
                while (reply[ID].equals(-1)) {
                    this.gameStage.setRequests(ID, true);
                    this.wait();
                }
                this.notifyAll();
            }catch (InterruptedException e) { e.printStackTrace(); }
        }
        System.out.println("Refresh DONE.");
        reply[ID]=-1;
    }

    @Override
    public int makeChoice() {
        return 0;
    }

    @Override
    public void showPlayerInfo(Player player) {
        this.displayMessage("Now player #" + player.getPlayerNum() + " " + player.getName() + " is at #" + player.getCoordiantes().getCoord() + " " + player.getCoordiantes().getName() + ".");
    }
    @Override
    public void displayResult(List<Player> playerList) {
        final int ID = Request.Refresh.getID();
        synchronized (this){
            try{
                while (reply[ID].equals(-1)) {
                    this.gameStage.setRequests(ID, true);
                    this.wait();
                }
                this.notifyAll();
            }catch (InterruptedException e) { e.printStackTrace(); }
        }
        System.out.println("Refresh DONE.");
        reply[ID]=-1;
    }

    @Override
    public int askNumberOfPlayers(int MIN, int MAX) {
        final int ID = Request.AskNumberOfPlayers.getID();
        int s = 0;
        synchronized (this){
            try{
                while (reply[ID].equals(-1)) {
                    this.gameStage.setRequests(ID, true);
                    this.wait();
                }
                if(reply[ID] instanceof Integer) s = (Integer) reply[ID];
                else s = 0;
                this.notifyAll();
            }catch (InterruptedException e) { e.printStackTrace(); }
        }
        System.out.println("Number of players DONE. - "+s);
        reply[ID]=-1;
        return s;
    }
    @Override
    public String askPlayerName(int playerNumber) {
        final int ID = Request.AskPlayerName.getID();
        String s = "null";
        synchronized (this){
            try{
                while (reply[ID].equals(-1)) {
                    this.gameStage.setRequests(ID, playerNumber);
                    this.wait();
                }
                if( reply[ID] instanceof String ) s = (String) reply[ID];
                else s = "null";
                this.notifyAll();
            }catch (InterruptedException e) { e.printStackTrace(); }
        }
        if( s.length()==0 )s="Player "+playerNumber;
        System.out.println("Name of player "+playerNumber+" DONE. - "+s);
        reply[ID]=-1;
        return s;
    }
    @Override
    public boolean askForBuying(Property property) {
        final int ID = Request.AskForBuying.getID();
        boolean s = false;
        reply[ID] = -1;

        synchronized (this){
            try{
                while (reply[ID].equals(-1)) {
                    this.gameStage.setRequests(ID, true);
                    this.wait();
                }
                s = reply[ID].equals(1);
                this.notifyAll();
            }catch (InterruptedException e) { e.printStackTrace(); }
        }
        System.out.println("Ask for buying DONE. - "+s);
        reply[ID]=-1;
        return s;
    }
    @Override
    public boolean askPayFine() {
        final int ID = Request.AskPayFine.getID();
        boolean s = false;
        reply[ID] = -1;

        synchronized (this){
            try{
                while (reply[ID].equals(-1)) {
                    this.gameStage.setRequests(ID, true);
                    this.wait();
                }
                s = reply[ID].equals(1);
                this.notifyAll();
            }catch (InterruptedException e) { e.printStackTrace(); }
        }
        System.out.println("Ask pay fine DONE. - "+s);
        reply[ID]=-1;
        return s;
    }

    @Override
    public int step1ChooseOperation() {
        final int ID = Request.StepOne.getID();
        int s=1;
        reply[ID] = -1;

        synchronized (this){
            try{
                while (reply[ID].equals(-1)) {
                    this.gameStage.setRequests(ID, true);
                    this.wait();
                }
                if(reply[ID] instanceof Integer) s = (Integer) reply[ID];
                else s = 1;
                this.notifyAll();
            }catch (InterruptedException e) { e.printStackTrace(); }
        }
        System.out.println("Step one DONE. - "+s);
        reply[ID]=-1;
        return s;
    }


    public void setReply(int index, Object value){
        //System.out.println("Set: "+this);
        synchronized(this) {
            if (index < NUMBEROFREPLIES) {
                reply[index] = value;
            }
            this.notifyAll();
        }
    }

    public MonopolyStage getGameStage() {
        return gameStage;
    }


    public GUI(MonopolyStage gameStage){
        this.gameStage = gameStage;
        this.reply = new Object[NUMBEROFREPLIES];
        Arrays.fill(reply,-1);
    }

}