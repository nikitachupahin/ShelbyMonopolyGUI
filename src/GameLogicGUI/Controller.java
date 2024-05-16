package GameLogicGUI;

import GameLogic.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class Controller {

    @FXML
    protected Button
            yesButton, noButton, retireButton;


    protected boolean yesButtonAvailable, noButtonAvailable, retireButtonAvailable;

    protected boolean yes, no, save, load, retire;


    @FXML
    protected TextArea messageArea;

    @FXML
    protected ImageView dice1, dice2;

    private Stage primaryStage;
    private Scene scene;
    private MonopolyStage monopoly;


    public void clearAllButton(){
        yes=false;
        no=false;
        save=false;
        load=false;
        retire=false;
    }



    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }



    public void setScene(Scene scene) {
        this.scene = scene;
    }



    public Stage getPrimaryStage() {
        return primaryStage;
    }



    public void setMonopoly(MonopolyStage monopoly) {
        this.monopoly = monopoly;
    }

    public TextArea getMessageArea() {
        return messageArea;
    }


    public Button getYesButton() {
        return yesButton;
    }



    public Button getNoButton() {
        return noButton;
    }



    public void setYesButtonAvailable(boolean yesButtonAvailable) {
        this.yesButtonAvailable = yesButtonAvailable;
    }



    public void setNoButtonAvailable(boolean noButtonAvailable) {
        this.noButtonAvailable = noButtonAvailable;
    }


    public void setRetireButtonAvailable(boolean retireButtonAvailable) {
        this.retireButtonAvailable = retireButtonAvailable;
    }


    public boolean getSave() {
        return save;
    }

    public boolean getLoad() {
        return load;
    }

    public boolean getRetire() {
        return retire;
    }

    public boolean getNo() {
        return no;
    }

    public boolean getYes() {
        return yes;
    }



    public void setNo(boolean no) {
        this.no = no;
    }



    public void setYes(boolean yes) {
        this.yes = yes;
    }



    public void setRetire(boolean retire) {
        this.retire = retire;
    }



    public ImageView getFirstDice() {
        return dice1;
    }


    public ImageView getSecondDice() {
        return dice2;
    }



    public Button getRetireButton() {
        return retireButton;
    }
}
