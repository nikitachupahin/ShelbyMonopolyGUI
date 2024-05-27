package GameLogic;

enum Color {
    RED("#F33",1),
    BLUE("#33F",2),
    YELLOW("#EE4",3),
    GREEN("#3F3",4),
    PURPLE("#E4E",5),
    BLACK("#443",6);

    private String colorCode;
    private int playerNumber;

    private Color(String colorCode, int playerNumber) {
        this.colorCode = colorCode;
        this.playerNumber = playerNumber;
    }

    public String getColorCode(){
        return colorCode;
    }
}

public class Player {
    private int playerNum;
    private boolean isInJail;
    private boolean status;
    private String name;
    private Color color;
    private boolean continueGame;
    private int moneyAmount;
    private Cell coordinates;

    public Player(int playerNum, String name, boolean continueGame, int moneyAmount, Cell coordinates, Color color) {
        this.playerNum = playerNum;
        this.isInJail = false;
        this.name = name;
        this.continueGame = continueGame;
        this.moneyAmount = moneyAmount;
        this.coordinates = coordinates;
        this.color = color;
        this.status = true;
    }

    public Player(String name, Color color, Cell location) {
        this.status = true;
        this.name = name;
        this.moneyAmount = 2000;
        this.coordinates = location;
        this.isInJail = false;
        this.playerNum = color.ordinal()+1;
        this.color = color;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public String getName() {
        return name;
    }

    public boolean getGameStatus() {
        return continueGame;
    }

    public int getMoneyAmount() {
        return moneyAmount;
    }

    public Cell getCoordinates() {
        return coordinates;
    }

    public Color getColor() {
        return color;
    }

    public void endGame() {
        this.continueGame = false;
    }

    public void changeMoneyAmount(int moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public void changeCoordinates(Cell coordinates) {
        this.coordinates = coordinates;
    }

    public boolean getJailStatus(){
        return isInJail;
    }

    public void setJailStatus(boolean isInJail){
        this.isInJail = isInJail;
    }

    public boolean isOnline() {
        return status;
    }
}
