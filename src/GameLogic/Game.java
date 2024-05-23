package GameLogic;

import java.util.*;

enum MonopolyMap {
    Jail(0, "Jail", Type.JAIL),
    Go(1, "GO", Type.REST_ZONE),
    Central(2, "Central", Type.PROPERTY, 850, 90),
    WanChai(3, "Wan Chai", Type.PROPERTY, 750, 70),
    PayTax(4, "Pay Tax", Type.TAX),
    Stanley(5, "Stanley", Type.PROPERTY, 650, 65),
    PassbyJail(6, "Pass by Jail", Type.REST_ZONE),
    ShekO(7, "Shek O", Type.PROPERTY, 350, 15),
    MongKok(8, "Mong Kok", Type.PROPERTY, 550, 35),
    Chance1(9, "Chance 1", Type.CHANCE),
    TsingYi(10, "Tsing Yi", Type.PROPERTY, 450, 20),
    FreeParking(11, "Free Parking", Type.REST_ZONE),
    Shatin(12, "Shatin", Type.PROPERTY, 650, 70),
    Chance2(13, "Chance 2", Type.CHANCE),
    TuenMun(14, "Tuen Mun", Type.PROPERTY, 350, 25),
    TaiPo(15, "Tai Po", Type.PROPERTY, 550, 20),
    GotoJail(16, "Go to Jail", Type.TO_JAIL),
    SaiKung(17, "Sai Kung", Type.PROPERTY, 400, 15),
    YuenLong(18, "Yuen Long", Type.PROPERTY, 450, 25),
    Chance3(19, "Chance 3", Type.CHANCE),
    TaiO(20, "Tai O", Type.PROPERTY, 650, 30);


    private final String name;
    private final int price;
    private final int rent;
    private final int position;
    private final Type type;


   MonopolyMap(int position, String name, Type type){
        this.position = position;
        this.name = name;
        this.type = type;
        this.price = 0;
        this.rent = 0;
    }
    MonopolyMap(int position, String name, Type type, int price, int rent){
        this.position = position;
        this.name = name;
        this.type = type;
        this.price = price;
        this.rent = rent;
    }

    public Type getType(){
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    public int getRent() {
        return this.rent;
    }

    public int getPosition() {
        return this.position;
    }
}

public class Game {
    // Constants
    private final int FIRSTSQUARE = 1, LASTSQUARE = 20;
    private final int JAILPOSITION = 0, JAILNEXT = 7;
    private final int MINPLAYERS = 2, MAXPLAYERS = 4;
    private final int FINE = 90, SALARY = 1500, TAXDIV = 10, ROUND = 5;
    private final int JAILDAYS = 3;
    private final int GAMELENGTH = 100;

    // Game Data / Variables
    private int amountOfPlayers;
    private List<Player> playerList;
    private Map<Integer,Cell> cellSet;
    private int round;
    private int roundStep;
    private Player currentPlayer;
    private UserInterface GUI;

    public Game(UserInterface userInterface){
        this.GUI = userInterface;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public Map<Integer, Cell> getCellSet() {
        return cellSet;
    }

    private Player getNextPlayer() {
        int currentIndex = playerList.indexOf(currentPlayer);
        int nextIndex = (currentIndex + 1) % amountOfPlayers;
        while (true) {
            if (playerList.get(nextIndex).isOnline()) {
                return playerList.get(nextIndex);
            }
            nextIndex = (nextIndex + 1) % amountOfPlayers;
        }
    }

    private void setNextPlayer() {
        int lastPlayerNumber = currentPlayer.getPlayerNum();
        currentPlayer = this.getNextPlayer();
        if(currentPlayer.getPlayerNum()<lastPlayerNumber) this.round+=1;
        this.roundStep = 0;
    }

    private int[] rollDice(){
        final int DICEMIN = 1, DICEMAX = 6;
        int dice1 = getRandomNumber(DICEMIN, DICEMAX);
        int dice2 = getRandomNumber(DICEMIN, DICEMAX);
        GUI.rollDice(dice1, dice2);
        int[] s = new int[2];
        s[0]=dice1;
        s[1]=dice2;
        return s;
    }
    private boolean randomChoice(){
        return getRandomNumber(0, 1) != 0;
    }

    private int randomChance(){
        final int CHANCEMULTIPLE = 10, CHANCEMIN = -30, CHANCEMAX = 20;
        return getRandomNumber(CHANCEMIN, CHANCEMAX) * CHANCEMULTIPLE;
    }


    private int getRandomNumber(int min, int max){
        Random random = new Random();
        return random.nextInt(max-min+1) + min;
    }

    private boolean isGameOver(){
        return getNextPlayer() == this.currentPlayer || this.round > GAMELENGTH;
    }

    private boolean askPayFine(){
        return GUI.askPayFine();
    }

    private boolean checkPlayerLoss(Player player){
        if(!player.isOnline())return true;

        if(player.getMoneyAmount() <= 0 || !player.isOnline()){
            Cell tmp = cellSet.get(FIRSTSQUARE);
            do{
                if(tmp instanceof Property){
                    if(((Property) tmp).getOwner() == player) {
                        ((Property) tmp).setOwner(null);
                        GUI.changePropertyOwner(((Property) tmp),player);
                    }
                }
                tmp = tmp.getNext();
            }while(tmp != cellSet.get(FIRSTSQUARE));
            player.endGame();
            GUI.notify("Player "+player.getName()+" has no money left and is out of game.");
            return true;
        }
        return false;
    }
}
