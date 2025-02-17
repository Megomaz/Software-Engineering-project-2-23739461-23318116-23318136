module comp20050.hexagonalboard {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens comp20050.hexagonalboard to javafx.fxml;
    exports comp20050.hexagonalboard;
}