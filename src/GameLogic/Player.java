package GameLogic;

import java.io.Serializable;

enum Colors implements Serializable{

    RED("#F33",1),

    BLUE("#33F",2),

    YELLOW("#EE4",3),

    GREEN("#3F3",4),

    PURPLE("#E4E",5),

    BLACK("#443",6);
    private final String name;
    private final int colorNumber;



    Colors(String name, int colorNumber) {
        this.name = name;
        this.colorNumber = colorNumber;
    }


    public String getName() {
        return this.name;
    }
}


public class Player implements Serializable, Cloneable {

    private boolean status;//Whether this player is in game or quited/lose.
    private String name;
    private int money;
    private Cell location;
    private int jailDays;
    private int playerNo;
    private boolean auto;
    private Colors color;

    private final int ORIGINALMONEY = 2000;



    public Player(String name, Colors color, Cell location) {
        this.status = true;
        this.name = name;
        this.money = ORIGINALMONEY;
        this.location = location;
        this.jailDays = 0;
        this.playerNo = color.ordinal()+1;
        this.auto = false;
        this.color = color;
    }




    public String getName() {
        return name;
    }


    public int getMoneyAmount() {
        return money;
    }


    public Cell getCoordiantes() {
        return location;
    }


    public int getPlayerNum() {
        return playerNo;
    }


    public String getColor() {
        return color.getName();
    }


    public int getJailDays() {
        return jailDays;
    }


    public boolean isOnline(){
        return status;
    }

    public boolean isInJail(){
        return !(jailDays == 0);
    }


    public boolean isAuto() {
        return auto;
    }


    public void updateMoney(int amount) {
        this.money += amount;
    }


    public void setLocation(Cell location){
        this.location = location;
    }


    public void setJailDays(int jailDays){
        this.jailDays = jailDays;
    }


    public void quitGame() {
        this.status = false;
    }

}
