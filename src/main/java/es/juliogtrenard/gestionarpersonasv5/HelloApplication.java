package es.juliogtrenard.gestionarpersonasv5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal de la aplicación que extiende {@link Application}.
 * Esta clase se encarga de iniciar la interfaz gráfica de usuario
 * y cargar la vista definida en el archivo FXML.
 */
public class HelloApplication extends Application {
    /**
     * Metodo principal que inicia la aplicación.
     *
     * @param args Argumentos de la línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Metodo que se llama al iniciar la aplicación.
     *
     * @param stage El escenario principal donde se muestra la interfaz gráfica.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 450);

        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/img/library_icon.png"))));

        stage.setTitle("PERSONAS");
        stage.setScene(scene);

        stage.setMinWidth(700);
        stage.setMinHeight(400);

        stage.setTitle("PERSONAS");
        stage.setScene(scene);
        stage.show();
    }
}