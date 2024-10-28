package es.juliogtrenard.gestionarpersonasv5.controladores;

import es.juliogtrenard.gestionarpersonasv5.modelos.Persona;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Controlador para la ventana modal que se abre al agregar una nueva persona.
 */
public class ControladorModal {
    /**
     * Campo de texto para ingresar el nombre de la nueva persona.
     */
    @FXML
    private TextField txtNombre;

    /**
     * Campo de texto para ingresar los apellidos de la nueva persona.
     */
    @FXML
    private TextField txtApellidos;

    /**
     * Campo de texto para ingresar la edad de la nueva persona.
     */
    @FXML
    private TextField txtEdad;

    /**
     * La {@link Persona} que se va a añadir a la lista y tabla.
     */
    private Persona persona;

    /**
     * Maneja el evento de cerrar la ventana modal.
     *
     * @param event El evento que activa este método.
     */
    @FXML
    public void cerrarVentana(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    /**
     * Maneja el evento de agregar una nueva persona a la lista.
     * Valida las entradas del usuario y, si son correctas,
     * crea una nueva instancia de {@link Persona} y la añade a la tabla.
     *
     * @param event El evento que activa este método.
     */
    @FXML
    public void guardarPersona(ActionEvent event) {
        String errores = validarEntradas();

        if (!errores.isEmpty()) {
            mostrarAlertaErrores(errores);
            return;
        }

        if (esPersonaRepetida()) {
            mostrarAlertaErrores("Persona repetida");
            limpiarCampos();
        } else {
            if (persona != null) {
                persona.setNombre(txtNombre.getText());
                persona.setApellidos(txtApellidos.getText());
                persona.setEdad(Integer.parseInt(txtEdad.getText()));

                mostrarAlertaValido("Persona modificada correctamente.");
            } else {
                crearPersona();
            }

            cerrarVentana(event);
        }
    }

    /**
     * Valida las entradas del usuario y devuelve un string
     * con los errores encontrados, si los hay.
     *
     * @return Un string con los errores de validación.
     */
    private String validarEntradas() {
        return errores();
    }

    /**
     * Muestra una alerta con el mensaje de error proporcionado.
     *
     * @param errores El mensaje de error a mostrar.
     */
    private void mostrarAlertaErrores(String errores) {
        Alert alerta = new Alert(Alert.AlertType.ERROR, errores);
        alerta.setHeaderText(null);
        alerta.setTitle("ERROR:");
        Stage stage = (Stage) alerta.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/img/library_icon.png"))));
        alerta.showAndWait();
    }

    /**
     * Verifica si la persona que se intenta agregar ya existe
     * en la lista.
     *
     * @return {@code true} si la persona ya está en la lista,
     * {@code false} en caso contrario.
     */
    private boolean esPersonaRepetida() {
        for (Persona p : HelloController.listaPersonas) {
            if (esIgualPersona(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Compara una persona existente con los datos ingresados.
     *
     * @param p La persona a comparar.
     * @return {@code true} si los datos son iguales,
     * {@code false} en caso contrario.
     */
    private boolean esIgualPersona(Persona p) {
        return p.getNombre().equalsIgnoreCase(txtNombre.getText().trim()) && p.getApellidos().equalsIgnoreCase(txtApellidos.getText().trim()) && p.getEdad() == Integer.parseInt(txtEdad.getText());
    }

    /**
     * Realiza las validaciones de las entradas del usuario y
     * devuelve un string con los errores encontrados.
     *
     * @return Un string con los errores de validación.
     */
    private String errores() {
        String errores = "";

        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String edadInput = txtEdad.getText().trim();

        if (nombre.isEmpty()) {
            errores += "Debes introducir tu nombre.\n";
        } else if (!nombre.matches("^[A-Za-záéíóúÁÉÍÓÚÑñ\\s]+$")) {
            errores += "El nombre no puede contener números.\n";
        }

        if (apellidos.isEmpty()) {
            errores += "Debes introducir al menos un apellido.\n";
        } else if (!apellidos.matches("^[A-Za-záéíóúÁÉÍÓÚÑñ\\s]+$")) {
            errores += "Los apellidos no pueden contener números.\n";
        }

        int edad;
        try {
            edad = Integer.parseInt(edadInput);
        } catch (NumberFormatException e) {
            return errores + "El campo 'Edad' debe ser numérico.\n";
        }

        if (edad < 1 || edad > 100) {
            errores += "Introduce una edad válida (1-100).\n";
        }

        return errores;
    }

    /**
     * Crea una nueva instancia de {@link Persona} con los datos ingresados
     * y la añade a la lista y a la tabla. Muestra una alerta de éxito.
     */
    private void crearPersona() {
        String nombre = txtNombre.getText();
        String apellidos = txtApellidos.getText();
        int edad = Integer.parseInt(txtEdad.getText());

        persona = new Persona(nombre, apellidos, edad);

        mostrarAlertaValido("Persona añadida correctamente.");
    }

    /**
     * Muestra una alerta con el mensaje de confirmación proporcionado.
     */
    private void mostrarAlertaValido(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION, mensaje);
        alerta.setHeaderText(null);
        alerta.setTitle("INFO:");
        Stage stage = (Stage) alerta.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/img/library_icon.png"))));
        alerta.showAndWait();
    }

    /**
     * Devuelve la persona que se va a añadir.
     *
     * @return La persona que se va a añadir.
     */
    public Persona getPersona() {
        return persona;
    }

    /**
     * Establece la persona que se va a editar.
     * Rellena los campos de la ventana modal con los datos de la persona.
     *
     * @param persona La persona que se va a editar.
     */
    public void setPersona(Persona persona) {
        this.persona = persona;
        txtNombre.setText(persona.getNombre());
        txtApellidos.setText(persona.getApellidos());
        txtEdad.setText(String.valueOf(persona.getEdad()));
    }

    /**
     * Limpia los campos de entrada de texto.
     */
    private void limpiarCampos() {
        txtApellidos.clear();
        txtEdad.clear();
        txtNombre.clear();
    }
}