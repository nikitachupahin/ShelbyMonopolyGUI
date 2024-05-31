package GameLogic;

import java.io.*;
import java.util.*;
import GameLogicGUI.*;


enum MonopolyMap implements Serializable{
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
    private String name;
    private int price;
    private int rent;
    private int position;
    private Type type;


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
}



public class Game implements Serializable, Runnable{
    /* [ User Interface ] */
    private UserInterface UI;

    /* [ Constants ] */
    private final int FIRSTSQUARE = 1, LASTSQUARE = 20;
    private final int JAILPOSITION = 0, JAILNEXT = 7;
    private final int MINPLAYERS = 2, MAXPLAYERS = 6;
    private final int FINE = 90, SALARY = 1500, TAXDIV = 10, ROUND = 5;
    private final int JAILDAYS = 3;
    private final int GAMELENGTH = 100;

    /* [ Compulsory Game Data / Variables ] */
    private int numberOfPlayers;
    private List<Player> playerList;
    private Map<Integer,Cell> squareSet;
    private int round;
    private int roundStep;//Detail in method runGame
    private Player currentPlayer;


    public List<Player> getPlayerList() {
        return playerList;
    }


    public Map<Integer, Cell> getSquareSet() {
        return squareSet;
    }



    private Player getNextPlayer() {
        int i=0;
        boolean foundCurrentPlayer = false;
        while(true){
            if(foundCurrentPlayer){
                if(playerList.get(i).isOnline()) break;
            }
            if(playerList.get(i) == currentPlayer) foundCurrentPlayer = true;
            i++;
            i%=numberOfPlayers;
        }
        return playerList.get(i);
    }


    private void setNextPlayer() {
        int lastPlayerNumber = currentPlayer.getPlayerNum();
        currentPlayer = this.getNextPlayer();
        if(currentPlayer.getPlayerNum()<lastPlayerNumber) this.round+=1;
        this.roundStep = 0;
    }

    private int getRandomNumber(int min, int max){
        Random random = new Random();
        return random.nextInt(max-min+1) + min;
    }

    private int[] rollDice(){
        final int DICEMIN = 1, DICEMAX = 6;
        int dice1 = getRandomNumber(DICEMIN, DICEMAX);
        int dice2 = getRandomNumber(DICEMIN, DICEMAX);
        UI.rollDice(dice1, dice2);
        int[] s = new int[2];
        s[0]=dice1;
        s[1]=dice2;
        return s;
    }

    private int randomChance(){
        final int CHANCEMULTIPLE = 10, CHANCEMIN = -30, CHANCEMAX = 20;
        return getRandomNumber(CHANCEMIN, CHANCEMAX) * CHANCEMULTIPLE;
    }

    private boolean isGameEnds(){
        return getNextPlayer()==this.currentPlayer || this.round>GAMELENGTH;
    }

    private boolean askPayFine(){
        return UI.askPayFine();
    }

    private boolean checkPlayerOut(Player player){
        if(!player.isOnline())return true;
        if(player.getMoneyAmount()<=0||!player.isOnline()){
            Cell tmp = squareSet.get(FIRSTSQUARE);
            do{
                if(tmp instanceof Property){
                    if(((Property) tmp).getOwner()==player) {
                        ((Property) tmp).setOwner(null);
                        UI.changePropertyOwner(((Property) tmp),player);
                    }
                }
                tmp = tmp.getNext();
            }while(tmp!=squareSet.get(FIRSTSQUARE));
            player.quitGame();
            UI.notify("Player "+player.getName()+" has no money left and is out of game.");
            return true;
        }
        return false;
    }


    private void moveCurrentPlayer(int steps){
        int i;
        Cell tmp = currentPlayer.getCoordiantes();
        for(i=1;i<=steps;i++) tmp = tmp.getNext();
        UI.playerMove(currentPlayer.getCoordiantes(), tmp, steps);
        if(currentPlayer.getCoordiantes().getCoord()>tmp.getCoord()){
            UI.displayMessage("Player passes through GO square and gain salary of HKD 1500.");
            currentPlayer.updateMoney(SALARY);
            UI.playerUpdateMoney(currentPlayer,SALARY);
        }
        currentPlayer.setLocation(tmp);
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
        UI.displayResult(this.getSortedPlayers());
    }


    private void gameOver(){
        UI.notify("Game Over");
        this.showScoreBoard();
    }


    public void runGame() {
        int userChoice;
        int[] dice;
        while (!this.isGameEnds()) {
            UI.displayMessage("\nRound " + this.round);
            UI.showPlayerInfo(currentPlayer);

            /* Round Step 0 */
            // [1] Continue [2] Report [3] Set Auto [4] Retire
            if (roundStep == 0) {  // Удалите условие currentPlayer.isAuto()
                userChoice = UI.step1ChooseOperation();
                switch (userChoice) {
                    case 2:
                        this.showScoreBoard();
                        continue;
                    case 3:
                        // Удалите функционал автопилота
                        // currentPlayer.setAuto(true);
                        break;
                    case 4:
                        currentPlayer.quitGame();
                        this.checkPlayerOut(currentPlayer);
                        UI.notify("Player " + currentPlayer.getName() + " quits game.");
                        this.setNextPlayer();
                        continue;
                    default:
                        break;
                }
                roundStep++;
            }

            /* Round Step 1 */
            // roll dice and move
            if (roundStep == 1) {
                dice = this.rollDice();
                // If player is in jail
                if (currentPlayer.isInJail()) {
                    UI.displayMessage(currentPlayer.getName() + " is in jail (" + currentPlayer.getJailDays() + " days left).");
                    if (dice[0] == dice[1]) {
                        currentPlayer.setJailDays(0);
                    } else if (this.askPayFine()) {
                        if (currentPlayer.getMoneyAmount() < FINE) {
                            UI.notify("You can't pay fine because your money is less than 90.");
                        } else {
                            currentPlayer.setJailDays(0);
                            currentPlayer.updateMoney(-FINE);
                            UI.playerUpdateMoney(currentPlayer, -FINE);
                        }
                    }
                    if (currentPlayer.getJailDays() == 1) {
                        UI.displayMessage(currentPlayer.getName() + " has to pay fine on the last day.");
                        currentPlayer.updateMoney(-FINE);
                        UI.playerUpdateMoney(currentPlayer, -FINE);
                        currentPlayer.setJailDays(0);
                        if (this.checkPlayerOut(currentPlayer)) {
                            this.setNextPlayer();
                            continue;
                        }
                    }
                }
                // Not in jail / just out of jail
                if (!currentPlayer.isInJail()) {
                    this.moveCurrentPlayer(dice[0] + dice[1]);
                }
                roundStep++;
            }

            /* Round Step 2 */
            // Check the new square
            if (roundStep == 2) {
                switch (currentPlayer.getCoordiantes().getType()) {
                    case TAX:
                        int tax = (((currentPlayer.getMoneyAmount() + ROUND) / TAXDIV) + ROUND) / TAXDIV * TAXDIV;
                        UI.displayMessage("Player " + currentPlayer.getName() + " should pay tax of HKD" + tax + ".");
                        currentPlayer.updateMoney(-tax);
                        UI.playerUpdateMoney(currentPlayer, -tax);
                        break;
                    case JAIL:
                        if (currentPlayer.isInJail()) currentPlayer.setJailDays(currentPlayer.getJailDays() - 1);
                        break;
                    case CHANCE:
                        int chanceMoney = this.randomChance();
                        UI.displayMessage("Player " + currentPlayer.getName() + " gets a chance!");
                        currentPlayer.updateMoney(chanceMoney);
                        UI.playerUpdateMoney(currentPlayer, chanceMoney);
                        if (this.checkPlayerOut(currentPlayer)) {
                            this.setNextPlayer();
                            continue;
                        }
                        break;
                    case TO_JAIL:
                        UI.displayMessage("Player " + currentPlayer.getName() + " goes to jail.");
                        currentPlayer.setJailDays(JAILDAYS);
                        currentPlayer.setLocation(squareSet.get(JAILPOSITION));
                        UI.playerGotoJail(currentPlayer);
                        break;
                    case PROPERTY:
                        Property curProperty = (Property) currentPlayer.getCoordiantes();
                        UI.displayMessage("Player " + currentPlayer.getName() + " arrives at " + curProperty.getName() + " (Price:" + curProperty.getPrice() + ", Rent:" + curProperty.getRent() + ").");
                        if (curProperty.getOwner() != null && curProperty.getOwner() != currentPlayer) {
                            UI.displayMessage("This property belongs to " + curProperty.getOwner().getName() + ". " + currentPlayer.getName() + " should pay HKD " + curProperty.getRent() + ".");
                            currentPlayer.updateMoney(-curProperty.getRent());
                            curProperty.getOwner().updateMoney(curProperty.getRent());
                            UI.playerUpdateMoney(currentPlayer, -curProperty.getRent());
                            UI.playerUpdateMoney(curProperty.getOwner(), curProperty.getRent());
                            if (this.checkPlayerOut(currentPlayer)) {
                                this.setNextPlayer();
                                continue;
                            }
                        } else if (curProperty.getOwner() == null) {
                            boolean buy;

                            buy = UI.askForBuying(curProperty);
                            if (buy) {
                                if (currentPlayer.getMoneyAmount() > curProperty.getPrice()) {
                                    UI.changePropertyOwner(curProperty, currentPlayer);
                                    UI.playerUpdateMoney(currentPlayer, -curProperty.getPrice());
                                    curProperty.setOwner(currentPlayer);
                                    currentPlayer.updateMoney(-curProperty.getPrice());
                                } else {
                                    UI.displayMessage("Failed because money is not enough.");
                                }
                            }
                        } else {
                            UI.displayMessage("Nothing happens.");
                        }
                        break;
                    default:
                        break;
                }
                roundStep = 0;
            }
            UI.showPlayerInfo(currentPlayer);
            this.setNextPlayer();
        }
        this.gameOver();
    }

    private boolean initNewGame(){
        /* Init Map */
        this.squareSet = new HashMap<Integer, Cell>();
        for (MonopolyMap mapinfo: MonopolyMap.values()) {
            switch (mapinfo.getType()){
                case PROPERTY:
                    squareSet.put(mapinfo.getPosition(),new Property(mapinfo.getType(), mapinfo.getName(), mapinfo.getPosition(), null, mapinfo.getPrice(), mapinfo.getRent()));
                    break;
                default:
                    squareSet.put(mapinfo.getPosition(), new Cell(mapinfo.getType(), mapinfo.getName(), mapinfo.getPosition(), null));
                    break;
            }
        }
        for(int i = 1; i <= LASTSQUARE; i++) {
            squareSet.get(i).setNext(squareSet.get((i+1)%(LASTSQUARE+1)));
        }
        squareSet.get(LASTSQUARE).setNext(squareSet.get(1));
        squareSet.get(JAILPOSITION).setNext(squareSet.get(JAILNEXT));
        /* Init Players */
        this.playerList = new ArrayList<Player>();
        this.numberOfPlayers = UI.askNumberOfPlayers(MINPLAYERS,MAXPLAYERS);
        List<Colors> color = new ArrayList<Colors>();
        color.add(Colors.RED);
        color.add(Colors.BLUE);
        color.add(Colors.YELLOW);
        color.add(Colors.GREEN);
        color.add(Colors.PURPLE);
        color.add(Colors.BLACK);
        String playerName;
        for(int i=0;i<this.numberOfPlayers;i++){
            playerName = UI.askPlayerName(i+1);
            playerList.add(new Player(playerName,color.get(i),squareSet.get(1)));
        }
        /* Init parameter */
        this.round = 1;
        this.roundStep = 0;
        this.currentPlayer = playerList.get(0);
        return true;
    }

    public Game(boolean GUI, InputStream inputStream){
        if(GUI) this.UI = new MonopolyStage().getGUIinterface();
    }

    public Game(UserInterface UI){ this.UI = UI; }

    @Override
    public void run() {
        boolean initSuccessfully = this.initNewGame();
        if (!initSuccessfully) {
            UI.notify("Failed to initialize new game.");
            return;
        }
        runGame();
    }
}
