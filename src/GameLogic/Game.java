package GameLogic;

import java.util.List;

enum MonopolyMap {
    Jail,
    Go,
    Central,
    WanChai,
    PayTax,
    Stanley,
    PassbyJail,
    ShekO,
    MongKok,
    Chance1,
    TsingYi,
    FreeParking,
    Shatin,
    Chance2,
    TuenMun,
    TaiPo,
    GotoJail,
    SaiKung,
    YuenLong,
    Chance3,
    TaiO;
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
    private int numberOfPlayers;
    private List<Player> playerList;
    private Player currentPlayer;
}
