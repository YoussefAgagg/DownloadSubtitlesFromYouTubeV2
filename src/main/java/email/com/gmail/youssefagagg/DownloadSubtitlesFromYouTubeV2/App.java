package email.com.gmail.youssefagagg.DownloadSubtitlesFromYouTubeV2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @SuppressWarnings("exports")
	@Override
    public void start(Stage stage) throws IOException {
    	FXMLLoader fxmlLoader=loadFXML("downsubfromyoutube");
    	
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
      
        stage.show();
        DownSubController controller=fxmlLoader.getController();
        stage.setOnCloseRequest(v->{
        	controller.appClosed();
        });
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml).load());
    }

    private static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
    }

    public static void main(String[] args) {
        launch();
    }

}