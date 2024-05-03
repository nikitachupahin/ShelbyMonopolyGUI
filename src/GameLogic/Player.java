package GameLogic;

enum Color {
    RED("#F33",1),
    GREEN("#3F3",2),
    BLUE("#33F",3),
    YELLOW("#EE4",3);

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
    private boolean status;
    private String name;
    private Color color;
    private boolean continueGame;
    private int moneyAmount;
    private Cell coordinates;

    public Player(String name, boolean continueGame, int moneyAmount, Cell coordinates, Color color){
        this.name = name;
        this.continueGame = continueGame;
        this.moneyAmount = moneyAmount;
        this.coordinates = coordinates;
        this.color = color;
        this.status = true;
    }

    public String getName(){
        return name;
    }

    public boolean getGameStatus(){
        return continueGame;
    }

    public int getMoneyAmount(){
        return moneyAmount;
    }

    public Cell getCoordinates(){
        return coordinates;
    }

    public Color getColor(){
        return color;
    }

    public void endGame(){
        this.continueGame = false;
    }

    public void changeMoneyAmount(int moneyAmount){
        this.moneyAmount = moneyAmount;
    }

    public void changeCoordinates(Cell coordinates){
        this.coordinates = coordinates;
    }

    public boolean isOnline() { return status; }
}
