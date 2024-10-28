package es.juliogtrenard.gestionarpersonasv5.controladores;

import es.juliogtrenard.gestionarpersonasv5.modelos.Persona;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controlador para la interfaz gráfica de la gestión de personas.
 * Esta clase maneja la lógica para agregar personas a una lista,
 * validando la entrada del usuario y actualizando la vista.
 */
public class HelloController {
    /**
     * Lista de {@link Persona} que se muestra en la tabla.
     */
    public static ArrayList<Persona> listaPersonas;

    /**
     * Tabla que muestra la lista de personas.
     */
    @FXML
    private TableView<Persona> tvTabla;

    @FXML
    private TextField txtFiltrarNombre;

    /**
     * Inicializa la lista de personas.
     */
    @FXML
    public void initialize() {
        listaPersonas = new ArrayList<>();

        txtFiltrarNombre.setOnKeyReleased(event -> {
            System.out.println("Evento onKeyReleased llamado");
            String nombre = txtFiltrarNombre.getText().toLowerCase();
            System.out.println("Texto del campo de texto: " + nombre);
            tvTabla.getItems().forEach(persona -> {
                System.out.println("Persona: " + persona.getNombre());
                if (persona.getNombre().toLowerCase().contains(nombre)) {
                    System.out.println("Condición de filtro cumplida");
                    persona.setVisible(true);
                } else {
                    persona.setVisible(false);
                }
            });
            tvTabla.refresh();
        });
    }

    /**
     * Maneja el evento de agregar una nueva persona a la lista.
     * Abre una ventana modal con el formulario para ingresar los datos de la nueva persona.
     *
     * @param event El evento que activa este método.
     */
    @FXML
    public void agregarPersona(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/juliogtrenard/gestionarpersonasv5/modal.fxml"));
        Parent root = loader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("Nueva Persona");

        stage.setMinWidth(500);
        stage.setMinHeight(200);
        stage.setMaxWidth(500);
        stage.setMaxHeight(200);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.showAndWait();

        ControladorModal controller = loader.getController();
        Persona persona = controller.getPersona();
        if (persona != null) {
            listaPersonas.add(persona);
            tvTabla.getItems().add(persona);
        }
    }

    /**
     * Maneja el evento de modificar una persona existente en la lista.
     * Abre una ventana modal con el formulario para editar los datos de la persona seleccionada.
     * Si la persona se edita correctamente, se actualiza en la lista y en la tabla.
     *
     * @param event El evento que activa este metodo.
     */
    @FXML
    public void modificarPersona(ActionEvent event) throws IOException {
        Persona personaSeleccionada = tvTabla.getSelectionModel().getSelectedItem();
        if (personaSeleccionada == null) {
            error();
            return;
        }

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/juliogtrenard/gestionarpersonasv5/modal.fxml"));
        Parent root = loader.load();

        ControladorModal controller = loader.getController();
        controller.setPersona(personaSeleccionada);

        stage.setScene(new Scene(root));
        stage.setTitle("Editar Persona");

        stage.setMinWidth(500);
        stage.setMinHeight(200);
        stage.setMaxWidth(500);
        stage.setMaxHeight(200);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.showAndWait();

        if (controller.getPersona() != null) {
            int indice = tvTabla.getSelectionModel().getSelectedIndex();
            listaPersonas.set(indice, controller.getPersona());
            tvTabla.getItems().set(indice, controller.getPersona());
        }
    }

    /**
     * Maneja el evento de eliminar una persona de la lista.
     * Elimina la persona seleccionada de la lista y de la tabla.
     *
     * @param event El evento que activa este metodo.
     */
    @FXML
    public void eliminarPersona(ActionEvent event) {
        Persona personaSeleccionada = tvTabla.getSelectionModel().getSelectedItem();
        if (personaSeleccionada == null) {
            error();
            return;
        }

        listaPersonas.remove(personaSeleccionada);
        tvTabla.getItems().remove(personaSeleccionada);

        confirmacion();
    }

    /**
     * Muestra una alerta de error cuando no se selecciona una persona.
     */
    private void error() {
        Alert alerta = new Alert(Alert.AlertType.ERROR, "Seleccione una persona.");
        alerta.setHeaderText(null);
        alerta.setTitle("ERROR:");
        Stage stage = (Stage) alerta.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/img/library_icon.png"))));
        alerta.showAndWait();
    }

    /**
     * Muestra una alerta de confirmación cuando se elimina una persona.
     */
    private void confirmacion() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Persona eliminada correctamente.");
        alerta.setHeaderText(null);
        alerta.setTitle("ERROR:");
        Stage stage = (Stage) alerta.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/img/library_icon.png"))));
        alerta.showAndWait();
    }
}