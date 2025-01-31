package org.rental.main.Menu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private Label MenuLabel;

    @FXML
    protected void PelangganButtonClick() {
        Stage currentStage = (Stage) MenuLabel.getScene().getWindow();
        currentStage.close();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/rental/main/Pelanggan.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 450);
            Stage stage = new Stage();
            stage.setTitle("Menu Pelanggan");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void MobilButtonClick() {
        Stage currentStage = (Stage) MenuLabel.getScene().getWindow();
        currentStage.close();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/rental/main/Mobil.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 450);
            Stage stage = new Stage();
            stage.setTitle("Menu Mobil");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void TransaksiButtonClick() {
        Stage currentStage = (Stage) MenuLabel.getScene().getWindow();
        currentStage.close();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/rental/main/Transaksi.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 910, 600);
            Stage stage = new Stage();
            stage.setTitle("Menu Transaksi");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
