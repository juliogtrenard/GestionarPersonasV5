module es.juliogtrenard.gestionarpersonasv5 {
    requires javafx.controls;
    requires javafx.fxml;


    opens es.juliogtrenard.gestionarpersonasv5 to javafx.fxml;
    exports es.juliogtrenard.gestionarpersonasv5;
    exports es.juliogtrenard.gestionarpersonasv5.controladores;
    exports es.juliogtrenard.gestionarpersonasv5.modelos;
    opens es.juliogtrenard.gestionarpersonasv5.controladores to javafx.fxml;
    opens es.juliogtrenard.gestionarpersonasv5.modelos to javafx.fxml;
}