package GameLogic;

public class Property extends Cell{
    private int price;
    private int rent;
    private Player owner;

    public Property(Type type, String name, int coordinates, Cell next, int price, int rent){
        super(type, name, coordinates, next);
        this.price = price;
        this.rent = rent;
    }

    public int getPrice(){
        return price;
    }

    public int getRent(){
        return rent;
    }

    public Player getOwner(){
        return owner;
    }

    public void setOwner(Player owner){
        this.owner = owner;
    }
}
