package GameLogic;

import org.junit.*;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class Tests {

    private PlayerComparator comparator;

    @Before
    public void setUp() {
        comparator = new PlayerComparator();
    }

    @Test
    public void testCompare_OnePlayerOnline() {
        Player player1 = new Player("Player 1", Colors.RED, null);
        player1.updateMoney(1000);

        Player player2 = new Player("Player 2", Colors.BLUE, null);
        player2.updateMoney(500);
        player2.quitGame();

        int result = comparator.compare(player1, player2);

        assertEquals(-1, result);
    }

    @Test
    public void testSortingPlayers() {
        Player player1 = new Player("Player 1", Colors.RED, null);
        player1.updateMoney(1000);

        Player player2 = new Player("Player 2", Colors.BLUE, null);
        player2.updateMoney(500);

        Player player3 = new Player("Player 3", Colors.GREEN, null);
        player3.updateMoney(1500);

        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);

        Collections.shuffle(players);

        Collections.sort(players, comparator);

        assertEquals("Player 3", players.get(0).getName());
        assertEquals("Player 1", players.get(1).getName());
        assertEquals("Player 2", players.get(2).getName());
    }

    @Test
    public void testPlayer() {
        final int initialMoney = 2000;
        final String playerName = "NAME";
        final String playerColorHex = "#F33";

        assertEquals(playerColorHex, Colors.RED.getName());

        Player player = new Player(playerName, Colors.RED, null);
        Player player1 = new Player("TEST", Colors.BLACK, null);
        PlayerComparator playerComparator = new PlayerComparator();

        playerComparator.compare(player, player1);

        player1.updateMoney(1);
        playerComparator.compare(player, player1);

        assertEquals(playerName, player.getName());
        assertEquals(initialMoney, player.getMoneyAmount());
        Assert.assertNull(player.getCoordinates());
        assertEquals(1, player.getPlayerNum());
        assertEquals(0, player.getJailDays());
        assertEquals(playerColorHex, player.getColor());

        player.setJailDays(0);
        player.setLocation(null);
        Assert.assertFalse(player.isInJail());
        Assert.assertTrue(player.isOnline());

        player.updateMoney(0);
        player.quitGame();
    }

    @Test
    public void testProperty() {
        final String propertyName = "aaa";
        final int propertyCoord = 1;
        final Type propertyType = Type.PROPERTY;

        Property property = new Property(propertyType, propertyName, propertyCoord, null, 0, 0);

        Assert.assertNull(property.getOwner());
        assertEquals(0, property.getRent());
        assertEquals(0, property.getPrice());
        assertEquals(propertyName, property.getName());
        assertEquals(propertyCoord, property.getCoord());
        assertEquals(propertyType, property.getType());
        Assert.assertNull(property.getNext());

        property.setOwner(null);
        property.setNext(null);
        Assert.assertNull(property.getOwner());
        Assert.assertNull(property.getNext());
    }

    @Test
    public void testMonopolyMapValues() {
        assertEquals("Jail", MonopolyMap.Jail.getName());
        assertEquals(Type.JAIL, MonopolyMap.Jail.getType());
        assertEquals(0, MonopolyMap.Jail.getPrice());
        assertEquals(0, MonopolyMap.Jail.getRent());
        assertEquals(0, MonopolyMap.Jail.getPosition());

        assertEquals("Central", MonopolyMap.Central.getName());
        assertEquals(Type.PROPERTY, MonopolyMap.Central.getType());
        assertEquals(850, MonopolyMap.Central.getPrice());
        assertEquals(90, MonopolyMap.Central.getRent());
        assertEquals(2, MonopolyMap.Central.getPosition());

        assertEquals("GO", MonopolyMap.Go.getName());
        assertEquals(Type.REST_ZONE, MonopolyMap.Go.getType());
        assertEquals(1, MonopolyMap.Go.getPosition());
        assertEquals(0, MonopolyMap.Go.getPrice());
        assertEquals(0, MonopolyMap.Go.getRent());
    }
}
