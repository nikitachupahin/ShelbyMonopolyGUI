package GameLogicGUI;

import GameLogic.Game;
import javafx.application.Application;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.*;



enum Request{

    ShowNotice(0),

    AskNewOrLoadGame(1),

    AskNumberOfPlayers(2),

    AskPlayerName(3),

    LoadGame(4),

    SaveGame(5),

    DisplayMessage(6),

    Refresh(7),

    StepOne(8),

    RollDice(9),

    AskPayFine(10),

    AskForBuying(11),

    ;

    private int ID;


    int getID(){
        return this.ID;
    }


    Request (int ID){
        this.ID = ID;
    }
}



public class MonopolyStage extends Application{

    private final int WindowWIDTH = 960, WindowHEIGHT=720;
    private final int SQUARES = 20, PLAYERS = 4;

    //Widgets
    private ArrayList<Node> playerIcon; // ImageView
    private ArrayList<ImageView> playerLocation;
    private ArrayList<Label> playerName;
    private ArrayList<Label> playerMoney;
    private Map<Integer,Button> ownerDisplay;
    private Map<Integer,FlowPane> squarePane;
    private ModuleLayer.Controller controller;

    private GUI gui;
    private Parent root;
    private Stage primaryStage;
    private Thread gameThread;
    private Game game;
    private Object[] requests;


    public void simpleNoticeWindow(String msg){
        Alert alert = new Alert(Alert.AlertType.NONE, msg, new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
        alert.setTitle("Monopoly Game");
        alert.setHeaderText("Notice");
        alert.initOwner(primaryStage);
        alert.showAndWait();
    }



    public static void main(String[] args) {
        launch(args);
    }


    public void setRequests(int index, Object value){
        requests[index] = value;
    }


    public GUI getGUIinterface(){
        return this.gui;
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
