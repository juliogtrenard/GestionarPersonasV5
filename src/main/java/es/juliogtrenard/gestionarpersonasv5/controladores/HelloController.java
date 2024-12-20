package es.juliogtrenard.gestionarpersonasv5.controladores;

import es.juliogtrenard.gestionarpersonasv5.modelos.Persona;
import javafx.collections.FXCollections;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    /**
     * Campo de texto para escribir el nombre a filtrar
     */
    @FXML
    private TextField txtFiltrarNombre;

    /**
     * Inicializa la lista de personas. Además, llama a la función filtrarLista
     */
    @FXML
    public void initialize() {
        listaPersonas = new ArrayList<>();

        filtrarLista();
    }

    /**
     * Filtra la lista de personas en la tabla por el nombre ingresado.
     * La lista se actualiza en tiempo real cada vez que se presiona una tecla en el campo de texto.
     */
    private void filtrarLista() {
        txtFiltrarNombre.setOnKeyReleased(_ -> {
            // Permite hacer comparaciones sin tener en cuenta si el usuario escribe en mayúsculas o minúsculas.
            String nombre = txtFiltrarNombre.getText().toLowerCase();

            // Se define un Predicate, que es una interfaz funcional que representa una condición que se puede evaluar.
            // En este caso, el predicate verifica si el nombre de cada persona, convertido a minúsculas, contiene la cadena nombre que se ingresó en el cuadro de texto.
            Predicate<Persona> predicate = persona -> persona.getNombre().toLowerCase().contains(nombre);

            // Hace lo siguiente:
            // listaPersonas.stream(): Se crea un flujo (stream) a partir de la lista original de personas.
            // .filter(predicate): Se aplica el filtro definido anteriormente, lo que significa que solo las personas que cumplen la condición del predicate serán incluidas.
            // .collect(Collectors.toList()): Se recolectan los elementos filtrados en una nueva lista.
            // FXCollections.observableArrayList(...): Se convierte esta lista en una lista observable, lo que permite que la tabla se actualice automáticamente cuando los datos cambian.
            // tvTabla.setItems(...): Se establece esta lista observable como el nuevo contenido de la tabla tvTabla.
            tvTabla.setItems(FXCollections.observableArrayList(listaPersonas.stream().filter(predicate).collect(Collectors.toList())));
        });
    }

    /**
     * Maneja el evento de agregar una nueva persona a la lista.
     * Abre una ventana modal con el formulario para ingresar los datos de la nueva persona.
     *
     * @param event El evento que activa este metodo.
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
            error("Seleccione una persona.");
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
     */
    @FXML
    public void eliminarPersona() {
        Persona personaSeleccionada = tvTabla.getSelectionModel().getSelectedItem();
        if (personaSeleccionada == null) {
            error("Seleccione una persona.");
            return;
        }

        listaPersonas.remove(personaSeleccionada);
        tvTabla.getItems().remove(personaSeleccionada);

        confirmacion("Persona eliminada correctamente.");
    }

    /**
     * Muestra una alerta de error cuando no se selecciona una persona.
     *
     * @param mensaje El mensaje de la alerta.
     */
    private void error(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR, mensaje);
        alerta.setHeaderText(null);
        alerta.setTitle("ERROR:");
        Stage stage = (Stage) alerta.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/img/library_icon.png"))));
        alerta.showAndWait();
    }

    /**
     * Muestra una alerta de confirmación cuando se elimina una persona.
     *
     * @param mensaje El mensaje de la alerta.
     */
    private void confirmacion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION, mensaje);
        alerta.setHeaderText(null);
        alerta.setTitle("INFO:");
        Stage stage = (Stage) alerta.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/img/library_icon.png"))));
        alerta.showAndWait();
    }

    /**
     * Maneja el evento de exportar los datos de la tabla a un archivo CSV.
     * Abre un cuadro de diálogo para guardar el archivo y escribe los datos
     * de las personas, incluyendo sus nombres, apellidos y edades, en el archivo CSV.
     *
     * @param event El evento que activa este metodo.
     */
    public void exportar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Establecer el directorio inicial en el directorio del proyecto
        File dirInicial = new File(System.getProperty("user.dir"));
        fileChooser.setInitialDirectory(dirInicial);

        // Obtiene el Stage a partir del evento
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File f = fileChooser.showSaveDialog(stage);

        if (f != null) {
            // Si el archivo no tiene la extensión .csv, se renombra
            if (!f.getName().toLowerCase().endsWith(".csv")) {
                f = new File(f.getAbsolutePath() + ".csv");
            }

            // Escribir los datos
            try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
                // Escribir encabezados
                w.write("Nombre,Apellidos,Edad");
                w.newLine();

                // Escribir datos de la tabla
                for (Persona persona : tvTabla.getItems()) {
                    w.write(persona.getNombre() + "," + persona.getApellidos() + "," + persona.getEdad());
                    w.newLine();
                }

                confirmacion("Archivo CSV exportado correctamente.");
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());

                error("Error al exportar el archivo CSV.");
            }
        }
    }

    public void importar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar archivo CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File dirInicial = new File(System.getProperty("user.dir"));
        fileChooser.setInitialDirectory(dirInicial);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File f = fileChooser.showOpenDialog(stage);

        if (f != null) {
            try (BufferedReader r = new BufferedReader(new FileReader(f))) {
                String linea;
                String primeraLinea = r.readLine();
                while ((linea = r.readLine()) != null) {
                    String[] campos = linea.split(",");
                    if (campos.length >= 3) {
                        String nombre = campos[0];
                        String apellidos = campos[1];
                        int edad = Integer.parseInt(campos[2]);

                        Persona persona = new Persona(nombre, apellidos, edad);
                        listaPersonas.add(persona);
                        tvTabla.getItems().add(persona);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al importar el archivo CSV: " + e.getMessage());
                error("Error al importar el archivo CSV.");
            }
            confirmacion("Archivo CSV importado correctamente.");
        }
    }
}