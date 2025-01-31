package org.rental.main.Pelanggan;

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

public class PelangganController {
    private int selectedPelangganId;

    @FXML
    private Label PelangganLabel;

    @FXML
    private Button SaveButton;

    @FXML
    private TextField NamaField;

    @FXML
    private TextField AlamatField;

    @FXML
    private TextField NoTelpField;

    @FXML
    private TextField EmailField;

    @FXML
    private TextField SearchField;

    @FXML
    private TableView<PelangganData> PelangganTable;

    @FXML
    private TableColumn<PelangganData, Integer> colIdPelanggan;

    @FXML
    private TableColumn<PelangganData, Integer> colNomor;

    @FXML
    private TableColumn<PelangganData, String> colNama;

    @FXML
    private TableColumn<PelangganData, String> colAlamat;

    @FXML
    private TableColumn<PelangganData, String> colNoTelepon;

    @FXML
    private TableColumn<PelangganData, String> colEmail;

    @FXML
    private TableColumn<PelangganData, String> colAction;

    private ObservableList<PelangganData> pelangganList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colAction.setCellFactory(new Callback<TableColumn<PelangganData, String>, TableCell<PelangganData, String>>() {
            @Override
            public TableCell<PelangganData, String> call(TableColumn<PelangganData, String> param) {
                return new TableCell<PelangganData, String>() {
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
                                PelangganData pelanggan = getTableView().getItems().get(getIndex());

                                // Menambahkan konfirmasi sebelum menghapus
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Konfirmasi Hapus");
                                alert.setHeaderText("Apakah Anda yakin ingin menghapus pelanggan?");
                                alert.setContentText("Pelanggan yang dihapus tidak bisa dikembalikan.");

                                // Menunggu hasil dari konfirmasi
                                alert.showAndWait().ifPresent(response -> {
                                    if (response == ButtonType.OK) {
                                        // Jika OK dipilih, lanjutkan dengan penghapusan
                                        deleteCustomer(pelanggan.getIdPelanggan());
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
                PelangganTable.setItems(pelangganList);
                PelangganTable.refresh(); // Memastikan tombol delete muncul
            } else {
                ObservableList<PelangganData> filteredList = pelangganList.filtered(pelanggan ->
                        pelanggan.getNama().toLowerCase().contains(keyword) ||
                                pelanggan.getAlamat().toLowerCase().contains(keyword) ||
                                pelanggan.getNoTelepon().toLowerCase().contains(keyword) ||
                                pelanggan.getEmail().toLowerCase().contains(keyword)
                );
                PelangganTable.setItems(filteredList);
            }
        });


        // Mengatur properti kolom
        colNomor.setCellValueFactory(new PropertyValueFactory<>("nomor"));
        colIdPelanggan.setCellValueFactory(new PropertyValueFactory<>("idPelanggan"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        colNoTelepon.setCellValueFactory(new PropertyValueFactory<>("noTelepon"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));



        // Menghubungkan tabel dengan list
        PelangganTable.setItems(pelangganList);

        // Memuat data dari API
        loadDataFromAPI();
    }

    private void loadDataFromAPI() {
        new Thread(() -> {
            try {
                // URL API
                URL url = new URL(ApiUrl.getapiUrl()+"api/pelanggan");
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
                Type listType = new TypeToken<List<PelangganData>>() {}.getType();
                List<PelangganData> result = gson.fromJson(response.toString(), listType);

                // Memperbarui tabel di UI thread
                Platform.runLater(() -> {
                    pelangganList.clear();
                    int nomor = 1;
                    for (PelangganData pelanggan : result) {
                        pelanggan.setNomor(nomor++); // Menambahkan nomor urut
                        pelangganList.add(pelanggan);
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
        if (NamaField.getText().trim().isEmpty() ||
                AlamatField.getText().trim().isEmpty() ||
                NoTelpField.getText().trim().isEmpty() ||
                EmailField.getText().trim().isEmpty()) {

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

    private void saveCustomer() {
        if (!isInputValid()) return;

        try {
            String nama = NamaField.getText();
            String alamat = AlamatField.getText();
            String noTelepon = NoTelpField.getText();
            String email = EmailField.getText();

            String json = String.format("{\"nama\":\"%s\", \"alamat\":\"%s\", \"noTelepon\":\"%s\", \"email\":\"%s\"}",
                    nama, alamat, noTelepon, email);

            URL url = new URL(ApiUrl.getapiUrl()+"api/pelanggan");
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
                showAlert("Error", "Email sudah ada. Kode respons: " + responseCode, Alert.AlertType.ERROR);
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

    private void updateCustomer() {
        if (!isInputValid()) return;

        try {
            String nama = NamaField.getText();
            String alamat = AlamatField.getText();
            String noTelepon = NoTelpField.getText();
            String email = EmailField.getText();

            // Membuat JSON data
            String json = String.format("{\"nama\":\"%s\", \"alamat\":\"%s\", \"noTelepon\":\"%s\", \"email\":\"%s\"}",
                    nama, alamat, noTelepon, email);

            // Membuat koneksi ke API
            URL url = new URL(ApiUrl.getapiUrl()+"api/pelanggan/" + selectedPelangganId);
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

    private void deleteCustomer(int pelangganId) {
        new Thread(() -> {
            try {
                // URL untuk menghapus pelanggan berdasarkan ID
                URL url = new URL(ApiUrl.getapiUrl()+"api/pelanggan/" + pelangganId);
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
        NamaField.setText("");
        AlamatField.setText("");
        NoTelpField.setText("");
        EmailField.setText("");
        selectedPelangganId = -1;
        SaveButton.setText("Save");
    }

    @FXML
    protected void BackButtonClick() {
        Stage currentStage = (Stage) PelangganLabel.getScene().getWindow();
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
            saveCustomer();
        } else if (SaveButton.getText().equals("Edit")) {
            updateCustomer();
        }
    }

    @FXML
    protected void CancelButtonClick() {
        clearFields();
    }

    @FXML
    protected void PelangganTableClick() {
        if (PelangganTable.getSelectionModel().getSelectedItem() != null) {
            // Ambil data pelanggan dari baris yang dipilih
            PelangganData selectedPelanggan = PelangganTable.getSelectionModel().getSelectedItem();

            // Isi field dengan data dari pelanggan yang dipilih
            NamaField.setText(selectedPelanggan.getNama());
            AlamatField.setText(selectedPelanggan.getAlamat());
            NoTelpField.setText(selectedPelanggan.getNoTelepon());
            EmailField.setText(selectedPelanggan.getEmail());

            // Simpan ID pelanggan untuk keperluan edit atau delete
            selectedPelangganId = selectedPelanggan.getIdPelanggan();

            SaveButton.setText("Edit");
        }
    }
}
