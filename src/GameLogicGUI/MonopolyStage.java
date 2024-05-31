package GameLogicGUI;

import GameLogic.Cell;
import GameLogic.Game;
import GameLogic.Player;
import GameLogic.Property;
import javafx.application.Application;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.*;




import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.*;

import static java.lang.Thread.sleep;


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
    private final int SQUARES = 20, PLAYERS = 6;

    //Widgets
    private ArrayList<Node> playerIcon; // ImageView
    private ArrayList<ImageView> playerLocation;
    private ArrayList<Label> playerName;
    private ArrayList<Label> playerMoney;
    private Map<Integer,Button> ownerDisplay;
    private Map<Integer,FlowPane> squarePane;
    private Controller controller;

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



    public void showNotice(){
        final int ID = Request.ShowNotice.getID();
        String msg = requests[ID].toString();

        simpleNoticeWindow(msg);

        this.requests[ID] = false;
        this.gui.setReply(ID,true);
    }

    public void displayMessage() {
        final int ID = Request.DisplayMessage.getID();
        String msg = requests[ID].toString();

        controller.getMessageArea().appendText("\n"+msg);

        this.requests[ID] = false;
        this.gui.setReply(ID,true);
    }


    public void askNewOrLoadGame() {
        final int ID = Request.AskNewOrLoadGame.getID();
        int s = -1;
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please choose to begin a new game or load game.",
                    new ButtonType("New Game", ButtonBar.ButtonData.YES),
                    new ButtonType("Load Game", ButtonBar.ButtonData.NO));
            alert.setTitle("Monopoly");
            alert.setHeaderText("New Game?");
            alert.initOwner(this.primaryStage);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().getButtonData() == ButtonBar.ButtonData.YES) s = 1;
            else s = 0;
        }
        catch (IllegalStateException e) {
            System.out.println("Exceiption");
            s = -1;
        }
        this.requests[ID]=false;
        this.gui.setReply(ID,s);
    }


    public void askNumberOfPlayers() {
        final int ID = Request.AskNumberOfPlayers.getID();
        int s = 0;
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please choose the number of players.",
                    new ButtonType("2"),
                    new ButtonType("3"),
                    new ButtonType("4"),
                    new ButtonType("5"),
                    new ButtonType("6"));

            alert.setTitle("Monopoly");
            alert.setHeaderText("Number of players");
            alert.initOwner(this.primaryStage);
            Optional<ButtonType> buttonType = alert.showAndWait();
            for(int i=2;i<=6;i++) {
                if(buttonType.get().getText().equals(i+""))s=i;
            }
        }
        catch (IllegalStateException e) {
            System.out.println("Exceiption");
            s = -1;
        }
        this.requests[ID]=false;
        this.gui.setReply(ID,s);
    }


    public void askPlayerName() {
        final int ID = Request.AskPlayerName.getID();
        String s = "null";

        TextField nameInput = new TextField ();
        nameInput.clear();
        nameInput.setText("Player "+requests[ID]);

        Button confirmButton = new Button();
        confirmButton.setText("Confirm");

        ImageView tmpPlayerIcon = new ImageView();
        final int PLAYERWIDTH=40, PLAYERHEIGHT=65;
        tmpPlayerIcon.setImage(new Image((getClass().getResource("img/players/player"+requests[ID]+".png")).toExternalForm(),PLAYERWIDTH,PLAYERHEIGHT,true,false));

        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        grid.add(tmpPlayerIcon,0,0);
        grid.add(new Label("Please input the name of Player "+requests[ID]), 1, 0);
        grid.add(nameInput, 1, 1);
        grid.add(confirmButton, 2, 2);

        final int SMALLWIDTH=350, SMALLHEIGHT=150;
        Scene scene = new Scene(new Group(), SMALLWIDTH,SMALLHEIGHT);
        scene.getStylesheets().add(getClass().getResource("guistyle.css").toExternalForm());

        Group root = (Group) scene.getRoot();
        root.getChildren().add(grid);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Player Name");

        confirmButton.setOnAction((ActionEvent e) -> {
            //System.out.println("clicked");
            stage.close();
        });
        stage.showAndWait();
        s = nameInput.getText();

        this.requests[ID]=false;
        this.gui.setReply(ID,s);
    }

    private String simpleInputWindow(String labelText,String title){

        if(labelText==null) labelText = "Please input:";
        if(title==null) title = "Monopoly";
        String s = "null";

        TextField userInput = new TextField ();
        userInput.clear();

        Button confirmButton = new Button();
        confirmButton.setText("Confirm");

        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        grid.add(new Label(labelText), 1, 0);
        grid.add(userInput, 1, 1);
        grid.add(confirmButton, 2, 2);

        final int SMALLWIDTH=350, SMALLHEIGHT=120;
        Scene scene = new Scene(new Group(), SMALLWIDTH,SMALLHEIGHT);
        scene.getStylesheets().add(getClass().getResource("guistyle.css").toExternalForm());

        Group root = (Group) scene.getRoot();
        root.getChildren().add(grid);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);

        confirmButton.setOnAction((ActionEvent e) -> {
            stage.close();
        });
        stage.showAndWait();
        s = userInput.getText();

        return s;
    }


    public void loadGame() {
        final int ID = Request.LoadGame.getID();
        String s;
        s = simpleInputWindow("Please input a game data file:","Load Game");
        this.requests[ID]=false;
        this.gui.setReply(ID,s);
    }


    public void saveGame() {
        final int ID = Request.SaveGame.getID();
        String s;
        s = simpleInputWindow("Please name this game data file:","Save Game");
        this.requests[ID]=false;
        this.gui.setReply(ID,s);
    }


    public boolean stepOne() {
        final int ID = Request.StepOne.getID();
        int s = -1;
        // [1] Continue [4] Retire [5] Save [6] load
        controller.getYesButton().setText("Roll Dice");
        controller.getNoButton().setText("Nothing");
        controller.getRetireButton().setText("Retire");
        controller.setYesButtonAvailable(true);

        controller.setRetireButtonAvailable(true);
        if (controller.getSave()) { s = 5; }
        if (controller.getLoad()) { s = 6; }
        if (controller.getRetire()) { s = 4; }

        if (controller.getYes()) { s = 1; }

        if (s >= 1 && s <= 6) {
            controller.clearAllButton();
            controller.setYesButtonAvailable(false);

            controller.setRetireButtonAvailable(false);
            this.requests[ID] = false;
            this.gui.setReply(ID, s);
            return true;
        } else {
            controller.setYes(false);

            controller.setRetire(false);
            return false;
        }
    }



    public void rollDice(){
        final int ID = Request.RollDice.getID();
        int[] dices = (int[]) requests[ID];

        final int DICE = 80;
        controller.getDice1().setImage(new Image((getClass().getResource("img/dice/dice"+dices[0]+".png")).toExternalForm(),DICE,DICE,true,false));
        controller.getDice2().setImage(new Image((getClass().getResource("img/dice/dice"+dices[1]+".png")).toExternalForm(),DICE,DICE,true,false));

        this.requests[ID] = false;
        this.gui.setReply(ID,true);
    }


    public boolean askForBuying(){
        final int ID = Request.AskForBuying.getID();
        int s=-1;
        controller.getYesButton().setText("Buy this!");
        controller.getNoButton().setText("Ignore");
        controller.setYesButtonAvailable(true);
        controller.setNoButtonAvailable(true);
        if(controller.getNo()) { s=2; }
        if(controller.getYes()) { s=1; }

        if(s>=1&&s<=2){
            controller.clearAllButton();
            controller.setYesButtonAvailable(false);
            controller.setNoButtonAvailable(false);
            controller.setRetireButtonAvailable(false);
            this.requests[ID]=false;
            this.gui.setReply(ID,s);
            return true;
        }
        else{
            controller.setYes(false);
            controller.setNo(false);
            return false;
        }
    }


    public boolean askPayFine(){
        final int ID = Request.AskPayFine.getID();
        int s=-1;
        controller.getYesButton().setText("Pay fine");
        controller.getNoButton().setText("Ignore");
        controller.setYesButtonAvailable(true);
        controller.setNoButtonAvailable(true);
        if(controller.getNo()) { s=2; }
        if(controller.getYes()) { s=1; }

        if(s>=1&&s<=2){
            controller.clearAllButton();
            controller.setYesButtonAvailable(false);
            controller.setNoButtonAvailable(false);
            controller.setRetireButtonAvailable(false);
            this.requests[ID]=false;
            this.gui.setReply(ID,s);
            return true;
        }
        else {
            controller.setYes(false);
            controller.setNo(false);
            return false;
        }
    }



    private void refreshPad(){
        final int ID = Request.Refresh.getID();

        final int PLAYERWIDTH=40, PLAYERHEIGHT=65;
        try{
            List<Player> sortedPlayers = this.game.getSortedPlayers(), players = this.game.getPlayerList();
            int i=0;
            for (Player p:sortedPlayers) {
                ((ImageView)playerIcon.get(i)).setImage(new Image((getClass().getResource("img/players/player"+p.getPlayerNum()+".png")).toExternalForm(),PLAYERWIDTH,PLAYERHEIGHT,true,false));
                playerName.get(i).setText("#"+p.getPlayerNum()+" "+p.getName());
                if(p.isOnline())playerMoney.get(i).setText("HKD"+p.getMoneyAmount());
                else playerMoney.get(i).setText("Offline");
                i++;
            }
            Map<Integer, GameLogic.Cell> squares = this.game.getSquareSet();
            for (Cell s:squares.values()){
                Collection<ImageView> c = new HashSet();
                for(Player p:players){
                    if(p.getCoordiantes()==s){
                        c.add(playerLocation.get(p.getPlayerNum()-1));
                    }
                    if(s instanceof Property){
                        if(((Property)s).getOwner()==p){
                            ownerDisplay.get(s.getCoord()).setStyle("-fx-background-color: "+(p.getColor()));
                        }
                    }
                }
                squarePane.get(s.getCoord()).getChildren().removeAll(c);
                squarePane.get(s.getCoord()).getChildren().addAll(c);
            }
        }catch (NullPointerException e){ System.out.println("."); }

        this.requests[ID] = false;
        this.gui.setReply(ID,true);
    }


    public void checkRequests() {
        boolean continueFlag;
        int sleeptime = 10;
        final int LONGSLEEP = 300;
        do {
            boolean existRequest = false;
            int rID = 0;
            for (Request r : Request.values()) {
                if (!requests[r.getID()].equals(false)) {
                    existRequest = true;
                    rID = r.getID();
                    System.out.println("Receive request " + rID);
                }
            }
            if (!existRequest) {
                continueFlag = false;
            } else {
                continueFlag = true;
                if (rID == Request.Refresh.getID())
                    this.refreshPad();
                if (rID == Request.ShowNotice.getID())
                    this.showNotice();
                if (rID == Request.DisplayMessage.getID())
                    this.displayMessage();
                if (rID == Request.AskNewOrLoadGame.getID()) {
                    this.askNewOrLoadGame();
                    sleeptime = LONGSLEEP;
                }
                if (rID == Request.AskNumberOfPlayers.getID())
                    this.askNumberOfPlayers();
                if (rID == Request.AskPlayerName.getID())
                    this.askPlayerName();
                if (rID == Request.LoadGame.getID())
                    this.loadGame();
                if (rID == Request.SaveGame.getID())
                    this.saveGame();
                if (rID == Request.StepOne.getID())
                    if (!this.stepOne()) continueFlag = false;
                if (rID == Request.RollDice.getID())
                    this.rollDice();
                if (rID == Request.AskForBuying.getID())
                    if (!this.askForBuying()) continueFlag = false;
                if (rID == Request.AskPayFine.getID())
                    if (!this.askPayFine()) continueFlag = false;
                this.refreshPad();
                try {
                    sleep(sleeptime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (continueFlag);
        System.out.println("Wait for next operation.");
    }



    private boolean initWidgets(Parent root){
        final int PROPERTY = 12;
        int i, propertyCount=0, squareCount=0, playerCount=0;
        for(i=0;i<=SQUARES;i++){
            ownerDisplay.put(i,(Button)root.lookup("#owner"+i));
            if(ownerDisplay.get(i)!=null)propertyCount++;

            squarePane.put(i,(FlowPane) root.lookup("#squarePane"+i));
            if(squarePane.get(i)!=null)squareCount++;
        }
        final int SMALLPLAYERW=24,SMALLPLAYERH=39;
        for(i=0;i<PLAYERS;i++){
            playerIcon.add((Node)root.lookup("#playerIcon"+(i+1)));
            if(playerIcon.get(i)!=null)playerCount++;

            playerName.add((Label)root.lookup("#nameLabel"+(i+1)));
            if(playerName.get(i)!=null)playerCount++;

            playerMoney.add((Label)root.lookup("#moneyLabel"+(i+1)));
            if(playerMoney.get(i)!=null)playerCount++;

            playerLocation.add(new ImageView());
            playerLocation.get(i).setImage(new Image((getClass().getResource("img/players/player"+(i+1)+".png")).toExternalForm(),SMALLPLAYERW,SMALLPLAYERH,true,false));
        }

        return playerCount == PLAYERS * 3 && squareCount == SQUARES + 1 && propertyCount == PROPERTY;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gamepad.fxml"));
        Parent root = fxmlLoader.load();
        this.root = root;

        System.out.print("Loading Widgets ... ");
        if(initWidgets(root)) System.out.println("Done");
        else System.out.println("Fail");

        Scene scene = new Scene(root, WindowWIDTH, WindowHEIGHT);
        scene.getStylesheets().add(getClass().getResource("guistyle.css").toExternalForm());

        controller = fxmlLoader.getController();
        controller.setPrimaryStage(primaryStage);
        controller.setScene(scene);
        controller.setMonopoly(this);

        primaryStage.setResizable(false);
        primaryStage.setTitle("Monopoly");
        primaryStage.setScene(scene);

        final int WAITPROGRESS = 500;
        sleep(WAITPROGRESS);
        primaryStage.show();

        System.out.println("BEGINS!");
        this.checkRequests();

    }


    @Override
    public void init(){
        final int NUMBEROFREQUESTS = 20;
        this.gui = new GUI(this);

        this.game = new Game(this.gui);
        this.gameThread = new Thread(game);

        this.playerIcon = new ArrayList<>();
        this.playerName = new ArrayList<>();
        this.playerMoney = new ArrayList<>();
        this.ownerDisplay = new HashMap<>();
        this.squarePane = new HashMap<>();
        this.playerLocation = new ArrayList<>();
        this.requests = new Object[NUMBEROFREQUESTS];
        Arrays.fill(requests,false);

        this.gameThread.start();
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

}

