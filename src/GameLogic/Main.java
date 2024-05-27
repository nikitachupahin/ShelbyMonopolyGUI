package GameLogic;

import GameLogicGUI.*;



import java.util.InputMismatchException;
import java.util.Scanner;



public class Main {

    public static void main(String args[]) {
        boolean GUI = true;

        if(!GUI){
            Game game = new Game(GUI, null);
            game.run();
        }
        else{
            new MonopolyStage().main(new String[0]);
        }
    }
}
