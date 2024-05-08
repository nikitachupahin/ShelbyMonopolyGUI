package GameLogic;

public interface UserInterface {

    void inform(String msg);

    void displayMessage(String msg);

    void rollDice(int dice1, int dice2);

    void playerMove(Cell beg, Cell end, int steps);

    void playerGotoJail(Player player);

    void showPlayerInfo(Player player);

    void playerUpdateMoney(Player player, int amount);

    int askNumberOfPlayers(int MIN, int MAX);

    String askPlayerName(int playerNumber);

    boolean askForBuying(Property property);

    boolean askPayFine();
}
