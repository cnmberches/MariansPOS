/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marianspos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class MariansPOS extends Application {
    Parent mainMenu;
    static Stage stage;
    
    @Override
    public void start(Stage stage) throws Exception {
        mainMenu = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        this.stage = stage;
        Scene scene = new Scene(mainMenu);
        scene.getStylesheets().add("MainMenu.css");
        this.stage.setResizable(false);
        this.stage.sizeToScene();
        this.stage.setTitle("Maria's Point of Sales System");
        this.stage.setScene(scene);
        this.stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
