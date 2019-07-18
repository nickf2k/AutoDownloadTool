package gov.niptex.process;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle("USPTO Patent Download");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("icon.png"))));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Ban muon thoat?");
            Optional<ButtonType> optional= alert.showAndWait();
            if (optional.get()==ButtonType.OK){
                Platform.exit();
                System.exit(0);
//                primaryStage.fireEvent(new WindowEvent(primaryStage,WindowEvent.WINDOW_CLOSE_REQUEST));
            }else{
                event.consume();
            }
        });
    }
}
