package GameLogic;

import GameLogicGUI.MonopolyStage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
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

    private boolean hasPlayerLost(Player player){
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

    private void movePlayer(int steps){
        int i;
        Cell nextStep = currentPlayer.getCoordinates();
        for(i = 1; i <= steps; i++) {
            nextStep = nextStep.getNext();
        }
        GUI.playerMove(currentPlayer.getCoordinates(), nextStep, steps);
        if(nextStep.getCoord() < currentPlayer.getCoordinates().getCoord()){
            GUI.displayMessage("Player passes through GO square and gain salary of HKD 1500.");
            currentPlayer.changeMoneyAmount(currentPlayer.getMoneyAmount() + SALARY);
            GUI.playerUpdateMoney(currentPlayer,SALARY);
        }
        currentPlayer.changeCoordinates(nextStep);
    }

    public ArrayList getSortedPlayers(){
        ArrayList<Player> sortedPlayerList = new ArrayList<Player>();
        for ( Player p : playerList ) {
            sortedPlayerList.add(p);
        }
        sortedPlayerList.sort(new PlayerComparator());
        return sortedPlayerList;
    }

    private void showScoreBoard() {
        GUI.displayResult(this.getSortedPlayers());
    }

    private void gameOver(){
        GUI.notify("Game Over");
        this.showScoreBoard();
    }

    public void runGame(){
        int userChoice;
        int[] dice;
        while(!this.isGameOver()){
            GUI.displayMessage("\nRound "+this.round);
            GUI.showPlayerInfo(currentPlayer);
            // [1] Continue [2] Retire
            if(roundStep == 0) {
                userChoice = GUI.makeChoice();
                if(userChoice == 2) {
                    currentPlayer.endGame();
                    this.hasPlayerLost(currentPlayer);
                    GUI.notify("Player " + currentPlayer.getName() + " quits game.");
                    this.setNextPlayer();
                }
                roundStep++;
            }

            if(roundStep == 1){
                dice = this.rollDice();
                if(currentPlayer.getJailStatus() == true){
                    GUI.displayMessage(currentPlayer.getName()+" is in jail ");
                    if(dice[0]==dice[1]){
                        currentPlayer.setJailStatus(false);
                    }
                    else if(this.askPayFine()){
                        if(currentPlayer.getMoneyAmount()<FINE){
                            GUI.notify("You can't pay fine because your money is less than 90.");
                        }
                        else{
                            currentPlayer.setJailStatus(false);
                            currentPlayer.changeMoneyAmount(currentPlayer.getMoneyAmount() - FINE);
                            GUI.playerUpdateMoney(currentPlayer,-FINE);
                        }
                    }
                }

                if(!currentPlayer.getJailStatus() == false){
                    this.movePlayer(dice[0]+dice[1]);
                }
                roundStep++;
            }
            if(roundStep == 2){
                switch (currentPlayer.getCoordinates().getType()){
                    case TAX:
                        int tax = (((currentPlayer.getMoneyAmount() + ROUND) / TAXDIV) + ROUND) / TAXDIV * TAXDIV;
                        GUI.displayMessage("Player "+currentPlayer.getName()+" should pay tax of HKD"+tax+".");
                        currentPlayer.changeMoneyAmount(currentPlayer.getMoneyAmount() - tax);
                        GUI.playerUpdateMoney(currentPlayer,-tax);
                        break;
                    case CHANCE:
                        int chanceMoney = this.randomChance();
                        GUI.displayMessage("Player "+currentPlayer.getName()+" gets a chance!");
                        currentPlayer.changeMoneyAmount(currentPlayer.getMoneyAmount() + chanceMoney);
                        GUI.playerUpdateMoney(currentPlayer,chanceMoney);
                        if(this.hasPlayerLost(currentPlayer)){
                            this.setNextPlayer(); continue;
                        }
                        break;
                    case TO_JAIL:
                        GUI.displayMessage("Player "+currentPlayer.getName()+" goes to jail.");
                        currentPlayer.setJailStatus(true);
                        currentPlayer.changeCoordinates(cellSet.get(JAILPOSITION));
                        GUI.playerGotoJail(currentPlayer);
                        break;
                    case PROPERTY:
                        Property curProperty = (Property)currentPlayer.getCoordinates();
                        GUI.displayMessage("Player "+currentPlayer.getName()+" arrives at "+curProperty.getName()+" (Price:"+curProperty.getPrice()+", Rent:"+curProperty.getRent()+").");
                        if( curProperty.getOwner() != null && curProperty.getOwner() != currentPlayer ){
                            GUI.displayMessage("This property belongs to "+curProperty.getOwner().getName()+". "+currentPlayer.getName()+" should pay HKD "+curProperty.getRent()+".");
                            currentPlayer.changeMoneyAmount(currentPlayer.getMoneyAmount() -curProperty.getRent() );
                            curProperty.getOwner().changeMoneyAmount(currentPlayer.getMoneyAmount() + curProperty.getRent());
                            GUI.playerUpdateMoney(currentPlayer,-curProperty.getRent());
                            GUI.playerUpdateMoney(curProperty.getOwner(), curProperty.getRent());
                            if(this.hasPlayerLost(currentPlayer)){
                                this.setNextPlayer();
                                continue;
                            }
                        }
                        else if(curProperty.getOwner() == null){
                            boolean buy;
                            buy = GUI.askForBuying(curProperty);
                            if(buy) {
                                if(currentPlayer.getMoneyAmount() > curProperty.getPrice()){
                                    GUI.changePropertyOwner (curProperty, currentPlayer);
                                    GUI.playerUpdateMoney (currentPlayer,-curProperty.getPrice());
                                    curProperty.setOwner(currentPlayer);
                                    currentPlayer.changeMoneyAmount(currentPlayer.getMoneyAmount() - curProperty.getPrice() );
                                }
                                else{
                                    GUI.displayMessage("Failed because money is not enough.");
                                }
                            }
                        }
                        else{
                            GUI.displayMessage("Nothing happens.");
                        }
                        break;
                    default:
                        break;
                }
                roundStep = 0;
            }
            GUI.showPlayerInfo(currentPlayer);
            this.setNextPlayer();
        }
        this.gameOver();
    }

    private boolean initNewGame(){
        /* Init Map */
        this.cellSet = new HashMap<Integer, Cell>();
        for (MonopolyMap mapinfo : MonopolyMap.values()) {
            switch (mapinfo.getType()){
                case PROPERTY:
                    cellSet.put(mapinfo.getPosition(),new Property(mapinfo.getType(), mapinfo.getName(), mapinfo.getPosition(), null, mapinfo.getPrice(), mapinfo.getRent()));
                    break;
                default:
                    cellSet.put(mapinfo.getPosition(), new Cell(mapinfo.getType(), mapinfo.getName(), mapinfo.getPosition(), null));
                    break;
            }
        }
        for(int i = 1; i <= LASTSQUARE; i++) {
            cellSet.get(i).setNext(cellSet.get((i+1)%(LASTSQUARE+1)));
        }
        cellSet.get(LASTSQUARE).setNext(cellSet.get(1));
        cellSet.get(JAILPOSITION).setNext(cellSet.get(JAILNEXT));
        /* Init Players */
        //!!! Whether there can be computer players at the beginning of the game?
        this.playerList = new ArrayList<Player>();
        this.amountOfPlayers = GUI.askNumberOfPlayers(MINPLAYERS,MAXPLAYERS);
        List<Color> color = new ArrayList<Color>();
        color.add(Color.RED);
        color.add(Color.BLUE);
        color.add(Color.YELLOW);
        color.add(Color.GREEN);
        color.add(Color.PURPLE);
        color.add(Color.BLACK);
        String playerName;
        for(int i=0;i<this.amountOfPlayers;i++){
            playerName = GUI.askPlayerName(i+1);
            playerList.add(new Player(playerName,color.get(i),cellSet.get(1)));
        }
        /* Init parameter */
        this.round = 1;
        this.roundStep = 0;
        this.currentPlayer = playerList.get(0);
        return true;
    }

    public Game(boolean GUI, InputStream inputStream){
        this.GUI = new MonopolyStage().getGUIinterface();
    }

    public void test() {
        this.initNewGame();
    }

    public void run() {
        boolean initSuccessfully = false;
        while(!initSuccessfully) {
            initSuccessfully = this.initNewGame();
        }
        runGame();
    }
}
