package org.rental.main.Mobil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.rental.main.ApiUrl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MobilController {
    private int selectedMobilId;

    @FXML
    private Label MobilLabel;

    @FXML
    private Button SaveButton;

    @FXML
    private TextField SearchField;

    @FXML
    private TextField MerkField;

    @FXML
    private TextField ModelField;

    @FXML
    private TextField TahunField;

    @FXML
    private TextField PlatField;

    @FXML
    private TextField HargaSewaField;

    @FXML
    private ChoiceBox<String> StatusChoiceBox;

    @FXML
    private TableView<MobilData> MobilTable;

    @FXML
    private TableColumn<MobilData, Integer> colNomor;

    @FXML
    private TableColumn<MobilData, Integer> colIdMobil;

    @FXML
    private TableColumn<MobilData, String> colMerk;

    @FXML
    private TableColumn<MobilData, String> colModel;

    @FXML
    private TableColumn<MobilData, String> colTahun;

    @FXML
    private TableColumn<MobilData, String> colPlat;

    @FXML
    private TableColumn<MobilData, String> colHargaSewa;

    @FXML
    private TableColumn<MobilData, String> colStatus;

    @FXML
    private TableColumn<MobilData, String> colAction;

    private ObservableList<MobilData> mobilList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        ObservableList<String> statusList = FXCollections.observableArrayList(
                "tersedia", "disewa"
        );
        StatusChoiceBox.setItems(statusList);
        StatusChoiceBox.setValue("tersedia");

        TahunField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Regex hanya mengizinkan angka
                TahunField.setText(newValue.replaceAll("[^\\d]", "")); // Hapus karakter non-angka
            }
        });

        HargaSewaField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Regex hanya mengizinkan angka
                HargaSewaField.setText(newValue.replaceAll("[^\\d]", "")); // Hapus karakter non-angka
            }
        });

        colAction.setCellFactory(new Callback<TableColumn<MobilData, String>, TableCell<MobilData, String>>() {
            @Override
            public TableCell<MobilData, String> call(TableColumn<MobilData, String> param) {
                return new TableCell<MobilData, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Membuat tombol Delete
                            Button deleteButton = new Button("Delete");
                            deleteButton.setOnAction(event -> {
                                // Mendapatkan data pelanggan yang akan dihapus
                                MobilData mobil = getTableView().getItems().get(getIndex());

                                // Menambahkan konfirmasi sebelum menghapus
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Konfirmasi Hapus");
                                alert.setHeaderText("Apakah Anda yakin ingin menghapus pelanggan?");
                                alert.setContentText("Pelanggan yang dihapus tidak bisa dikembalikan.");

                                // Menunggu hasil dari konfirmasi
                                alert.showAndWait().ifPresent(response -> {
                                    if (response == ButtonType.OK) {
                                        // Jika OK dipilih, lanjutkan dengan penghapusan
                                        deleteCar(mobil.getIdMobil());
                                    }
                                });
                            });
                            setGraphic(deleteButton);
                        }
                    }
                };
            }
        });

        SearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String keyword = newValue.trim().toLowerCase();
            if (keyword.isEmpty()) {
                // Reset data dan tampilkan semua data
                MobilTable.setItems(mobilList);
                MobilTable.refresh(); // Memastikan tombol delete muncul
            } else {
                ObservableList<MobilData> filteredList = mobilList.filtered(mobil ->
                        mobil.getMerk().toLowerCase().contains(keyword) ||
                                mobil.getModel().toLowerCase().contains(keyword) ||
                                mobil.getTahun().toLowerCase().contains(keyword) ||
                                mobil.getPlatNomor().toLowerCase().contains(keyword) ||
                                mobil.getHargaSewa().toLowerCase().contains(keyword) ||
                                mobil.getStatus().toLowerCase().contains(keyword)
                );
                MobilTable.setItems(filteredList);
            }
        });


        // Mengatur properti kolom
        colNomor.setCellValueFactory(new PropertyValueFactory<>("nomor"));
        colMerk.setCellValueFactory(new PropertyValueFactory<>("merk"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colTahun.setCellValueFactory(new PropertyValueFactory<>("tahun"));
        colPlat.setCellValueFactory(new PropertyValueFactory<>("platNomor"));
        colHargaSewa.setCellValueFactory(new PropertyValueFactory<>("hargaSewa"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));



        // Menghubungkan tabel dengan list
        MobilTable.setItems(mobilList);

        // Memuat data dari API
        loadDataFromAPI();
    }

    private void loadDataFromAPI() {
        new Thread(() -> {
            try {
                // URL API
                URL url = new URL(ApiUrl.getapiUrl()+"api/mobil");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                // Parsing JSON ke List
                Gson gson = new Gson();
                Type listType = new TypeToken<List<MobilData>>() {}.getType();
                List<MobilData> result = gson.fromJson(response.toString(), listType);

                // Memperbarui tabel di UI thread
                Platform.runLater(() -> {
                    mobilList.clear();
                    int nomor = 1;
                    for (MobilData mobil : result) {
                        mobil.setNomor(nomor++); // Menambahkan nomor urut
                        mobilList.add(mobil);
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Gagal memuat data");
                    alert.setContentText("Error: " + e.getMessage());
                    alert.showAndWait();
                });
            }
        }).start();
    }

    private boolean isInputValid() {
        if (MerkField.getText().trim().isEmpty() ||
                ModelField.getText().trim().isEmpty() ||
                TahunField.getText().trim().isEmpty() ||
                PlatField.getText().trim().isEmpty() ||
                HargaSewaField.getText().trim().isEmpty())
        {

            // Membuat alert untuk peringatan
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Semua field harus diisi!");

            // Menampilkan alert
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private void saveCar() {
        if (!isInputValid()) return;

        try {
            String merk = MerkField.getText();
            String model = ModelField.getText();
            String tahun = TahunField.getText();
            String plat = PlatField.getText();
            String hargaSewa = HargaSewaField.getText();
            String status = StatusChoiceBox.getValue();

            String json = String.format("{\"merk\":\"%s\", \"model\":\"%s\", \"tahun\":\"%s\", \"platNomor\":\"%s\", \"hargaSewaPerHari\":\"%s\", \"status\":\"%s\"}",
                    merk, model, tahun, plat, hargaSewa, status);

            URL url = new URL(ApiUrl.getapiUrl()+"api/mobil");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.getOutputStream().write(json.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                loadDataFromAPI();
                clearFields();
            } else {
                showAlert("Error", "Gagal menyimpan data. Kode respons: " + responseCode, Alert.AlertType.ERROR);
            }
        } catch (Exception ex) {
            showAlert("Error", "Gagal menyimpan data: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Tidak ada header
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateCar() {
        if (!isInputValid()) return;

        try {
            String merk = MerkField.getText();
            String model = ModelField.getText();
            String tahun = TahunField.getText();
            String plat = PlatField.getText();
            String hargaSewa = HargaSewaField.getText();
            String status = StatusChoiceBox.getValue();

            // Membuat JSON data
            String json = String.format("{\"merk\":\"%s\", \"model\":\"%s\", \"tahun\":\"%s\", \"platNomor\":\"%s\", \"hargaSewaPerHari\":\"%s\", \"status\":\"%s\"}",
                    merk, model, tahun, plat, hargaSewa, status);

            // Membuat koneksi ke API
            URL url = new URL(ApiUrl.getapiUrl()+"api/mobil/" + selectedMobilId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.getOutputStream().write(json.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Memuat ulang data
                loadDataFromAPI();
                clearFields();
                SaveButton.setText("Save");

                // Menampilkan pesan sukses
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Berhasil");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Data berhasil diperbarui!");
                successAlert.showAndWait();
            } else {
                // Menampilkan pesan gagal
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Gagal");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Gagal mengubah data. Kode respons: " + responseCode);
                errorAlert.showAndWait();
            }
        } catch (Exception ex) {
            // Menampilkan pesan error saat terjadi exception
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Gagal mengubah data: " + ex.getMessage());
            errorAlert.showAndWait();
        }
    }

    private void deleteCar(int mobilId) {
        new Thread(() -> {
            try {
                // URL untuk menghapus pelanggan berdasarkan ID
                URL url = new URL(ApiUrl.getapiUrl()+"api/mobil/" + mobilId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Memuat ulang data setelah penghapusan
                    Platform.runLater(() -> {
                        loadDataFromAPI();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText("Data berhasil dihapus");
                        alert.showAndWait();
                    });
                } else {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Gagal menghapus data");
                        alert.setContentText("Kode respons: " + responseCode);
                        alert.showAndWait();
                    });
                }
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Gagal menghapus data");
                    alert.setContentText("Pesan kesalahan: " + ex.getMessage());
                    alert.showAndWait();
                });
            }
        }).start();
    }

    private void clearFields() {
        selectedMobilId = -1;
        MerkField.setText("");
        ModelField.setText("");
        TahunField.setText("");
        PlatField.setText("");
        HargaSewaField.setText("");
        StatusChoiceBox.setValue("tersedia");
        SaveButton.setText("Save");
    }

    @FXML
    protected void BackButtonClick() {
        Stage currentStage = (Stage) MobilLabel.getScene().getWindow();
        currentStage.close();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/rental/main/Menu.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 241, 400);
            Stage stage = new Stage();
            stage.setTitle("Menu");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void SaveButtonClick() {
        if (SaveButton.getText().equals("Save")) {
            saveCar();
        } else if (SaveButton.getText().equals("Edit")) {
            updateCar();
        }
    }

    @FXML
    protected void CancelButtonClick() {
        clearFields();
    }

    @FXML
    protected void MobilTableClick() {
        if (MobilTable.getSelectionModel().getSelectedItem() != null) {
            // Ambil data pelanggan dari baris yang dipilih
            MobilData selectedMobil = MobilTable.getSelectionModel().getSelectedItem();

            // Isi field dengan data dari pelanggan yang dipilih
            MerkField.setText(selectedMobil.getMerk());
            ModelField.setText(selectedMobil.getModel());
            TahunField.setText(selectedMobil.getTahun());
            PlatField.setText(selectedMobil.getPlatNomor());
            HargaSewaField.setText(selectedMobil.getHargaSewa());
            StatusChoiceBox.setValue(selectedMobil.getStatus());

            // Simpan ID pelanggan untuk keperluan edit atau delete
            selectedMobilId = selectedMobil.getIdMobil();

            SaveButton.setText("Edit");
        }
    }
}
