package GameLogicGUI;


import GameLogic.Cell;
import GameLogic.Player;
import GameLogic.Property;
import GameLogic.UserInterface;



public class GUI implements UserInterface {
    private MonopolyStage gameStage;

    private final int numberForReplies = 20;
    private Object[] reply;


    @Override
    public void notify(String message) {
        final int ID = Request.ShowNotice.getID();
        synchronized (this) {
            try {
                while (reply[ID].equals(-1)) {
                    this.gameStage.setRequests(ID, message);
                    this.wait();
                }
                this.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Show notice DONE.");
        reply[ID] = -1;
    }

    @Override
    public void inform(String message) {

    }

    @Override
    public void displayMessage(String message) {
        final int ID = Request.DisplayMessage.getID();
        synchronized (this) {
            try {
                while (reply[ID].equals(-1)) {
                    this.gameStage.setRequests(ID, message);
                    this.wait();
                }
                this.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Display message DONE.");
        reply[ID] = -1;
    }

    @Override
    public void rollDice(int dice1, int dice2) {

    }

    @Override
    public void playerMove(Cell beg, Cell end, int steps) {

    }

    @Override
    public void playerGotoJail(Player player) {

    }

    @Override
    public void showPlayerInfo(Player player) {

    }

    @Override
    public void playerUpdateMoney(Player player, int amount) {

    }

    @Override
    public int askNumberOfPlayers(int MIN, int MAX) {
        return 0;
    }

    @Override
    public String askPlayerName(int playerNumber) {
        return null;
    }

    @Override
    public boolean askForBuying(Property property) {
        return false;
    }

    @Override
    public boolean askPayFine() {
        return false;
    }
}