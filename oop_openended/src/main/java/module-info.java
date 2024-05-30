module task.oop_openended {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens task.oop_openended to javafx.fxml;
    exports task.oop_openended;
}