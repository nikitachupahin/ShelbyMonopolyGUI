package GameLogic;

enum Type{
    PROPERTY,
    CHANCE,
    TAX,
    REST_ZONE,
    JAIL,
    TO_JAIL
}

public class Cell {
    private Type type;
    private String name;
    private int coordinates;
    private Cell next;

    public Cell(Type type, String name, int coordinates, Cell next){
        this.type = type;
        this.name = name;
        this.coordinates = coordinates;
        this.next = next;
    }

    public Type getType(){
        return this.type;
    }

    public String getName(){
        return this.name;
    }

    public int getCoord(){
        return this.coordinates;
    }

    public Cell getNext(){
        return this.next;
    }

    public void setNext(Cell next){
        this.next = next;
    }
}
