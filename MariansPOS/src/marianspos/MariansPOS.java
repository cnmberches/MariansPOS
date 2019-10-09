package marianspos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MariansPOS extends Application {
    Parent mainMenu;
    static Stage stage;
    
    @Override
    public void start(Stage stage) throws Exception {
        mainMenu = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        this.stage = stage;
        Scene scene = new Scene(mainMenu);
        this.stage.setResizable(false);
        this.stage.sizeToScene();
        this.stage.setTitle("Maria's Point of Sales System");
        this.stage.setScene(scene);
        this.stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
