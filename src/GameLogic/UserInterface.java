package GameLogic;

import java.util.List;

public interface UserInterface {

    /* [ Basic Output Functions ] */
    void notify(String msg);

    void inform(String msg);

    void displayMessage(String msg);

    void displayResult(List<Player> playerList);

    void rollDice(int dice1, int dice2);

    void playerMove(Cell beg, Cell end, int steps);

    void changePropertyOwner(Property property, Player player);

    void playerGotoJail(Player player);

    void showPlayerInfo(Player player);

    void playerUpdateMoney(Player player, int amount);

    int askNumberOfPlayers(int MIN, int MAX);

    String askPlayerName(int playerNumber);

    boolean askForBuying(Property property);

    boolean askPayFine();
}
