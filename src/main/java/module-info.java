module org.rental.main {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    // requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;

    exports org.rental.main;
    opens org.rental.main to javafx.fxml;

    exports org.rental.main.Menu;
    opens org.rental.main.Menu to javafx.fxml;

    exports org.rental.main.Pelanggan;
    opens org.rental.main.Pelanggan to javafx.fxml, com.google.gson;

    exports org.rental.main.Mobil;
    opens org.rental.main.Mobil to javafx.fxml, com.google.gson;

    exports org.rental.main.Transaksi;
    opens org.rental.main.Transaksi to javafx.fxml, com.google.gson;
}