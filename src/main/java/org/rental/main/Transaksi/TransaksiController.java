package org.rental.main.Transaksi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javafx.util.Callback;
import org.rental.main.ApiUrl;
import org.rental.main.Mobil.MobilData;
import org.rental.main.Pelanggan.PelangganData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class TransaksiController {
    // Data Click Pelanggan
    private int idP;
    private int idPp;
    private int idPelangganIndex;
    private String namaPelanggan;
    private String  alamatPelanggan;
    private String  noTelpPelanggan;
    private String  emailPelanggan;

    private String namaPelangganTransaksi;
    private String  alamatPelangganTransaksi;
    private String  noTelpPelangganTransaksi;
    private String  emailPelangganTransaksi;

    private String namaPelangganTransaksip;
    private String  alamatPelangganTransaksip;
    private String  noTelpPelangganTransaksip;
    private String  emailPelangganTransaksip;


    // Data Click Mobil
    private int idM;
    private int idMobilIndex;
    private String merkMobil;
    private String modelMobil;
    private String tahunMobil;
    private String platMobil;
    private String hargaSewaMobil;
    private String statusMobil;

    private String merkMobilTransaksi;
    private String modelMobilTransaksi;
    private String tahunMobilTransaksi;
    private String platMobilTransaksi;
    private String hargaSewaMobilTransaksi;
    private String statusMobilTransaksi;

    private int idMm;
    private String merkMobilTransaksit;
    private String modelMobilTransaksit;
    private String tahunMobilTransaksit;
    private String platMobilTransaksit;
    private String hargaSewaMobilTransaksit;
    private String statusMobilTransaksit;

    private int selectedTransaksiId;


    private String PelangganFieldp;
    private String MobilFieldp;
    private String TanggalMulaiDatePickerp;
    private String TanggalSelesaiDatePickerp;
    private String TotalHargaFieldp;
    private String DendaFieldp;
    // hell nah im so fckup

    @FXML
    private Label TransaksiLabel;

    @FXML
    private Button SaveButton;
    @FXML
    private Button CancelButton;

    @FXML
    private Button  BackButton;

    @FXML
    private TextField PelangganField;

    @FXML
    private TextField MobilField;

    @FXML
    private DatePicker TanggalMulaiDatePicker;

    @FXML
    private DatePicker TanggalSelesaiDatePicker;

    @FXML
    private TextField TotalHargaField;

    @FXML
    private TextField DendaField;

    @FXML
    private ChoiceBox<String> StatusChoiceBox;

    @FXML
    private TextField SearchFieldPelanggan;

    @FXML
    private TextField SearchFieldMobil;

    @FXML
    private TextField SearchFieldTransaksi;

    // data tabel mobil
    @FXML
    private TableView<MobilData> MobilTable;

    @FXML
    private TableColumn<MobilData, Integer> colNomorMobil;

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


    // data tabel pelanggan
    @FXML
    private TableView<PelangganData> PelangganTable;

    @FXML
    private TableColumn<PelangganData, Integer> colIdPelanggan;

    @FXML
    private TableColumn<PelangganData, Integer> colNomorPelanggan;

    @FXML
    private TableColumn<PelangganData, String> colNama;

    @FXML
    private TableColumn<PelangganData, String> colAlamat;

    @FXML
    private TableColumn<PelangganData, String> colNoTelepon;

    @FXML
    private TableColumn<PelangganData, String> colEmail;

    private ObservableList<PelangganData> pelangganList = FXCollections.observableArrayList();


    // data tabel transaksi
    @FXML
    private TableView<TransaksiData> TransaksiTable;

    @FXML
    private TableColumn<TransaksiData, Integer> colNomorTransaksi;

    @FXML
    private TableColumn<TransaksiData, String> colIdPelangganJoin;

    @FXML
    private TableColumn<TransaksiData, String> colIdMobilJoin;

    @FXML
    private TableColumn<TransaksiData, Integer> colIdTransaksi;

    @FXML
    private TableColumn<TransaksiData, String> colTanggalMulai;

    @FXML
    private TableColumn<TransaksiData, String> colTanggalSelesai;

    @FXML
    private TableColumn<TransaksiData, String> colTotalHarga;

    @FXML
    private TableColumn<TransaksiData, String> colDenda;

    @FXML
    private TableColumn<TransaksiData, String> colStatusTransaksi;

    @FXML
    private TableColumn<TransaksiData, String> colActionTransaksi;

    private ObservableList<TransaksiData> transaksiList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        TanggalMulaiDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            calculateTotalHarga();
        });

        // Listener untuk TanggalSelesaiDatePicker
        TanggalSelesaiDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            calculateTotalHarga();
        });

        colActionTransaksi.setCellFactory(new Callback<TableColumn<TransaksiData, String>, TableCell<TransaksiData, String>>() {
            @Override
            public TableCell<TransaksiData, String> call(TableColumn<TransaksiData, String> param) {
                return new TableCell<TransaksiData, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Mendapatkan data transaksi dari baris tempat tombol diklik
                            TransaksiData transaksi = getTableView().getItems().get(getIndex());

                            PelangganFieldp = transaksi.getPelanggan().getNama();
                            MobilFieldp = transaksi.getMobil().getPlatNomor();
                            TanggalMulaiDatePickerp = transaksi.getTanggalMulai();
                            TanggalSelesaiDatePickerp = transaksi.getTanggalSelesai();
                            TotalHargaFieldp = transaksi.getTotalHarga();
                            DendaFieldp = transaksi.getDenda();

                            selectedTransaksiId = transaksi.getIdTransaksi();

                            // Masukkan data ke variabel transaksi
                            idMm = transaksi.getMobil().getIdMobil();
                            idPp = transaksi.getPelanggan().getIdPelanggan();
                            namaPelangganTransaksip = transaksi.getPelanggan().getNama();
                            alamatPelangganTransaksip = transaksi.getPelanggan().getAlamat();
                            noTelpPelangganTransaksip = transaksi.getPelanggan().getNoTelepon();
                            emailPelangganTransaksip = transaksi.getPelanggan().getEmail();

                            merkMobilTransaksit = transaksi.getMobil().getMerk();
                            modelMobilTransaksit = transaksi.getMobil().getModel();
                            tahunMobilTransaksit = String.valueOf(transaksi.getMobil().getTahun());
                            platMobilTransaksit = transaksi.getMobil().getPlatNomor();
                            hargaSewaMobilTransaksit = String.valueOf(transaksi.getMobil().getHargaSewa());
                            statusMobilTransaksit = transaksi.getMobil().getStatus();

                            // Membuat tombol Delete
                            Button deleteButton = new Button("Delete");
                            deleteButton.setOnAction(event -> {
                                // Menambahkan konfirmasi sebelum menghapus
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Konfirmasi Hapus");
                                alert.setHeaderText("Apakah Anda yakin ingin menghapus transaksi?");
                                alert.setContentText("Transaksi yang dihapus tidak bisa dikembalikan.");

                                alert.showAndWait().ifPresent(response -> {
                                    if (response == ButtonType.OK) {
                                        deleteTransaksi(transaksi.getIdTransaksi());
                                    }
                                });
                            });

                            // Membuat tombol Selesai hanya jika status transaksi belum selesai
                            Button selesaiButton = null;
                            if (!"selesai".equalsIgnoreCase(transaksi.getStatus())) {  // Hanya tampilkan jika status bukan "selesai"
                                selesaiButton = new Button("Selesai");
                                selesaiButton.setOnAction(event -> {
                                    // Menampilkan konfirmasi sebelum menyelesaikan transaksi
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Konfirmasi Selesai");
                                    alert.setHeaderText("Apakah Anda yakin ingin menyelesaikan transaksi?");
                                    alert.setContentText("Transaksi yang selesai tidak bisa dikembalikan.");

                                    alert.showAndWait().ifPresent(response -> {
                                        if (response == ButtonType.OK) {
                                            transaksiSelesai(transaksi.getIdTransaksi());
                                        }
                                    });
                                });
                            }

                            // Menggunakan HBox untuk meletakkan tombol berdampingan
                            HBox hbox = new HBox(10, deleteButton);
                            if (selesaiButton != null) {
                                hbox.getChildren().add(selesaiButton);  // Hanya tambahkan tombol "Selesai" jika status bukan "selesai"
                            }
                            hbox.setAlignment(Pos.CENTER);  // Menyelaraskan tombol ke tengah

                            setGraphic(hbox);  // Set graphic ke HBox yang berisi tombol
                            // clearFields();
                        }
                    }
                };
            }
        });


        SearchFieldPelanggan.textProperty().addListener((observable, oldValue, newValue) -> {
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

        SearchFieldMobil.textProperty().addListener((observable, oldValue, newValue) -> {
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

        SearchFieldTransaksi.textProperty().addListener((observable, oldValue, newValue) -> {
            String keyword = newValue.trim().toLowerCase();
            if (keyword.isEmpty()) {
                // Reset data dan tampilkan semua data
                TransaksiTable.setItems(transaksiList);
                TransaksiTable.refresh();
            } else {
                ObservableList<TransaksiData> filteredList = transaksiList.filtered(transaksi ->
                        transaksi.getMobil().getPlatNomor().toLowerCase().contains(keyword) ||
                                transaksi.getPelanggan().getNama().toLowerCase().contains(keyword) ||
                                transaksi.getTanggalMulai().toLowerCase().contains(keyword) ||
                                transaksi.getTanggalSelesai().toLowerCase().contains(keyword) ||
                                transaksi.getTotalHarga().toLowerCase().contains(keyword) ||
                                transaksi.getStatus().toLowerCase().contains(keyword)
                );
                TransaksiTable.setItems(filteredList);
            }
        });

        DendaField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Regex hanya mengizinkan angka
                DendaField.setText(newValue.replaceAll("[^\\d]", "")); // Hapus karakter non-angka
            }
        });

        TotalHargaField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Regex hanya mengizinkan angka
                TotalHargaField.setText(newValue.replaceAll("[^\\d]", "")); // Hapus karakter non-angka
            }
        });

        // Tabel Mobil
        colNomorMobil.setCellValueFactory(new PropertyValueFactory<>("nomor"));
        colIdPelanggan.setCellValueFactory(new PropertyValueFactory<>("idPelanggan"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        colNoTelepon.setCellValueFactory(new PropertyValueFactory<>("noTelepon"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Tabel Pelanggan
        colNomorPelanggan.setCellValueFactory(new PropertyValueFactory<>("nomor"));
        colMerk.setCellValueFactory(new PropertyValueFactory<>("merk"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colTahun.setCellValueFactory(new PropertyValueFactory<>("tahun"));
        colPlat.setCellValueFactory(new PropertyValueFactory<>("platNomor"));
        colHargaSewa.setCellValueFactory(new PropertyValueFactory<>("hargaSewa"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Tabel transaksi
        colNomorTransaksi.setCellValueFactory(new PropertyValueFactory<>("nomor"));
        colIdTransaksi.setCellValueFactory(new PropertyValueFactory<>("idTransaksi"));
        colTanggalMulai.setCellValueFactory(new PropertyValueFactory<>("tanggalMulai"));
        colTanggalSelesai.setCellValueFactory(new PropertyValueFactory<>("tanggalSelesai"));
        colTotalHarga.setCellValueFactory(new PropertyValueFactory<>("totalHarga"));
        colDenda.setCellValueFactory(new PropertyValueFactory<>("denda"));
        colStatusTransaksi.setCellValueFactory(new PropertyValueFactory<>("status"));
        colIdPelangganJoin.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPelanggan().getNama())
        );
        colIdMobilJoin.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getMobil().getMerk() + " " + data.getValue().getMobil().getPlatNomor())
        );




        // Menghubungkan tabel mobil dengan list
        PelangganTable.setItems(pelangganList);

        // Menghubungkan tabel mobil dengan list
        MobilTable.setItems(mobilList);

        // Menghubungkan tabel mobil dengan list
        TransaksiTable.setItems(transaksiList);

        // Memuat data dari API
        loadDataMobilFromAPI();
        loadDataPelangganFromAPI();
        loadDataTransaksiFromAPI();
    }


    private void loadDataTransaksiFromAPI() {
        new Thread(() -> {
            try {
                // URL API
                URL url = new URL(ApiUrl.getapiUrl() + "api/transaksi");
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
                Type listType = new TypeToken<List<TransaksiData>>() {}.getType();
                List<TransaksiData> result = gson.fromJson(response.toString(), listType);

                // Memperbarui tabel di UI thread
                Platform.runLater(() -> {
                    transaksiList.clear();
                    int nomor = 1;
                    for (TransaksiData transaksi : result) {
                        transaksi.setNomor(nomor++); // Menambahkan nomor urut
                        transaksiList.add(transaksi);
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


    private void loadDataMobilFromAPI() {
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

    private void loadDataPelangganFromAPI() {
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

    private void saveTransaksi() {
        if (!isInputValid()) return;

        try {
            // Data Pelanggan
            String pelangganId = String.valueOf(idPelangganIndex); // ID pelanggan yang diambil dari pelanggan yang dipilih
            String pelangganNama = namaPelanggan; // Nama pelanggan yang diambil dari pelanggan yang dipilih
            String pelangganAlamat = alamatPelanggan; // Alamat pelanggan
            String pelangganNoTelepon = noTelpPelanggan; // No telepon pelanggan
            String pelangganEmail = emailPelanggan; // Email pelanggan

            // Data Mobil
            String mobilId = String.valueOf(idMobilIndex); // ID mobil yang diambil dari mobil yang dipilih
            String mobilMerk = merkMobil; // Merk mobil
            String mobilModel = modelMobil; // Model mobil
            String mobilTahun = String.valueOf(tahunMobil); // Tahun mobil
            String mobilPlatNomor = platMobil; // Plat nomor mobil
            String mobilHargaSewaPerHari = hargaSewaMobil; // Harga sewa per hari mobil
            String mobilStatus = statusMobil; // Status mobil

            // Data Transaksi
            String tanggalMulai = TanggalMulaiDatePicker.getValue().toString(); // Ambil tanggal mulai
            String tanggalSelesai = TanggalSelesaiDatePicker.getValue().toString(); // Ambil tanggal selesai

            // Menghitung jumlah hari antara tanggal mulai dan tanggal selesai
            LocalDate mulai = TanggalMulaiDatePicker.getValue();
            LocalDate selesai = TanggalSelesaiDatePicker.getValue();
            long jumlahHari = ChronoUnit.DAYS.between(mulai, selesai); // Menghitung selisih hari

            // Menghitung total harga berdasarkan jumlah hari
            double mobilHargaSewa = Double.parseDouble(mobilHargaSewaPerHari);
            double totalHarga = mobilHargaSewa * jumlahHari; // Total harga dihitung berdasarkan harga sewa per hari dan jumlah hari

            // Format total harga, menghilangkan .0 jika total harga adalah angka bulat
            String totalHargaString = (totalHarga % 1 == 0) ? String.format("%.0f", totalHarga) : String.format("%.2f", totalHarga);

            // Ambil denda dari input
            String denda = "0"; //DendaField.getText(); // Ambil denda
            // Status transaksi, misalnya "berlangsung"
            String statusTransaksi = "berlangsung";

            // Format JSON sesuai dengan struktur yang diinginkan
            String json = String.format("{\"pelanggan\":{" +
                            "\"idPelanggan\":%s, " +
                            "\"nama\":\"%s\", " +
                            "\"alamat\":\"%s\", " +
                            "\"noTelepon\":\"%s\", " +
                            "\"email\":\"%s\"}, " +
                            "\"mobil\":{" +
                            "\"idMobil\":%s, " +
                            "\"merk\":\"%s\", " +
                            "\"model\":\"%s\", " +
                            "\"tahun\":%s, " +
                            "\"platNomor\":\"%s\", " +
                            "\"hargaSewaPerHari\":%s, " +
                            "\"status\":\"%s\"}, " +
                            "\"tanggalMulai\":\"%s\", " +
                            "\"tanggalSelesai\":\"%s\", " +
                            "\"totalHarga\":%s, " +
                            "\"denda\":%s, " +
                            "\"status\":\"%s\"}",
                    pelangganId, pelangganNama, pelangganAlamat, pelangganNoTelepon, pelangganEmail,
                    mobilId, mobilMerk, mobilModel, mobilTahun, mobilPlatNomor, mobilHargaSewaPerHari, mobilStatus,
                    tanggalMulai, tanggalSelesai, totalHargaString, denda, statusTransaksi);
            // System.out.println("JSON yang Save: " + json); // Debug

            // URL untuk API transaksi
            URL url = new URL(ApiUrl.getapiUrl() + "api/transaksi");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.getOutputStream().write(json.getBytes());

            // Cek response dari API
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Jika transaksi berhasil disimpan, ubah status mobil menjadi "disewa"
                updateCar(mobilId, "disewa");

                loadDataTransaksiFromAPI();  // Update data setelah berhasil menyimpan transaksi
                clearFields(); // Bersihkan input field
            } else {
                showAlert("Error", "Gagal menyimpan transaksi. Kode respons: " + responseCode, Alert.AlertType.ERROR);
            }
        } catch (Exception ex) {
            showAlert("Error", "Gagal menyimpan data transaksi: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void updateCar(String mobilId, String newStatus) {
        try {
            // Ambil data mobil yang sudah disimpan di variabel sementara
            String merk = merkMobil;
            String model = modelMobil;
            String tahun = tahunMobil;
            String plat = platMobil;
            String hargaSewa = hargaSewaMobil;

            // Format JSON untuk mengubah status mobil
            String json = String.format("{\"merk\":\"%s\", \"model\":\"%s\", \"tahun\":\"%s\", \"platNomor\":\"%s\", \"hargaSewaPerHari\":\"%s\", \"status\":\"%s\"}",
                    merk, model, tahun, plat, hargaSewa, newStatus);

            // Kirim permintaan PUT ke API
            URL url = new URL(ApiUrl.getapiUrl() + "api/mobil/" + mobilId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.getOutputStream().write(json.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Jika berhasil, muat ulang data mobil
                loadDataMobilFromAPI();
            } else {
                showAlert("Error", "Gagal mengubah status mobil. Kode respons: " + responseCode, Alert.AlertType.ERROR);
            }
        } catch (Exception ex) {
            showAlert("Error", "Gagal mengubah status mobil: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void updateCarT(String mobilId, String newStatus) {
        try {
            int id = idM;
            String merk = merkMobilTransaksi;
            String model = modelMobilTransaksi;
            String tahun = String.valueOf(tahunMobilTransaksi);
            String plat = platMobilTransaksi;
            String hargaSewa = hargaSewaMobilTransaksi;

            // Format JSON untuk mengubah status mobil
            String json = String.format("{\"merk\":\"%s\", \"model\":\"%s\", \"tahun\":\"%s\", \"platNomor\":\"%s\", \"hargaSewaPerHari\":\"%s\", \"status\":\"%s\"}",
                    merk, model, tahun, plat, hargaSewa, newStatus);

            // Kirim permintaan PUT ke API
            URL url = new URL(ApiUrl.getapiUrl() + "api/mobil/" + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.getOutputStream().write(json.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Jika berhasil, muat ulang data mobil
                loadDataMobilFromAPI();
            } else {
                showAlert("Error", "Gagal mengubah status mobil. Kode respons: " + responseCode, Alert.AlertType.ERROR);
            }
        } catch (Exception ex) {
            showAlert("Error", "Gagal mengubah status mobil: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
// KODE APAAN INI
//    private void calculateTotalHarga() {
//        if (TanggalMulaiDatePicker.getValue() != null && TanggalSelesaiDatePicker.getValue() != null && hargaSewaMobil != null) {
//            // Hitung selisih hari antara tanggal mulai dan tanggal selesai
//            LocalDate mulai = TanggalMulaiDatePicker.getValue();
//            LocalDate selesai = TanggalSelesaiDatePicker.getValue();
//            long jumlahHari = ChronoUnit.DAYS.between(mulai, selesai);
//
//            // Pastikan jumlah hari tidak negatif
//            if (jumlahHari < 0) {
//                showAlert("Error", "Tanggal selesai tidak boleh sebelum tanggal mulai.", Alert.AlertType.ERROR);
//                return;
//            }
//
//            // Hitung total harga
//            double hargaSewaPerHari = Double.parseDouble(hargaSewaMobil);
//            double totalHarga = hargaSewaPerHari * jumlahHari;
//
//            // Format total harga, menghilangkan .0 jika total harga adalah angka bulat
//            String totalHargaString = (totalHarga % 1 == 0) ? String.format("%.0f", totalHarga) : String.format("%.2f", totalHarga);
//
//            // Update TotalHargaField
//            TotalHargaField.setText(totalHargaString);
//        }
//    }

    private void calculateTotalHarga() {
        // Check if all required fields are filled
        if (TanggalMulaiDatePicker.getValue() == null ||
                TanggalSelesaiDatePicker.getValue() == null ||
                hargaSewaMobil == null || hargaSewaMobil.isEmpty()) {
            return;
        }

        try {
            LocalDate mulai = TanggalMulaiDatePicker.getValue();
            LocalDate selesai = TanggalSelesaiDatePicker.getValue();

            // Validate dates
            if (selesai.isBefore(mulai)) {
                showAlert("Error", "Tanggal selesai tidak boleh sebelum tanggal mulai.", Alert.AlertType.ERROR);
                TotalHargaField.setText("0");
                return;
            }

            // Calculate days between dates
            long jumlahHari = ChronoUnit.DAYS.between(mulai, selesai);

            // Ensure minimum rental period is 1 day
            if (jumlahHari < 1) {
                jumlahHari = 1;
            }

            // Parse and validate rental price
            double hargaSewaPerHari;
            try {
                hargaSewaPerHari = Double.parseDouble(hargaSewaMobil);
                if (hargaSewaPerHari <= 0) {
                    throw new NumberFormatException("Harga sewa harus lebih dari 0");
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Harga sewa mobil tidak valid: " + e.getMessage(), Alert.AlertType.ERROR);
                return;
            }

            // Calculate total price
            double totalHarga = hargaSewaPerHari * jumlahHari;

            // Format the result
            String totalHargaString;
            if (totalHarga % 1 == 0) {
                totalHargaString = String.format("%.0f", totalHarga);
            } else {
                totalHargaString = String.format("%.2f", totalHarga);
            }

            // Update the field
            TotalHargaField.setText(totalHargaString);

        } catch (Exception e) {
            showAlert("Error", "Gagal menghitung total harga: " + e.getMessage(), Alert.AlertType.ERROR);
            TotalHargaField.setText("0");
        }
    }

//    private void showAlert(String title, String message, Alert.AlertType alertType) {
//        Alert alert = new Alert(alertType);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }

    private boolean isInputValid() {
        if (PelangganField.getText().trim().isEmpty() ||
                MobilField.getText().trim().isEmpty() ||
                TanggalMulaiDatePicker.getValue() == null ||
                TanggalSelesaiDatePicker.getValue() == null
        ){

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

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Tidak ada header
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void deleteTransaksi(int transaksiId) {
        // String mobilId = String.valueOf(idM);
        new Thread(() -> {
            try {
                // URL untuk menghapus pelanggan berdasarkan ID
                URL url = new URL(ApiUrl.getapiUrl()+"api/transaksi/" + transaksiId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    updateCar(String.valueOf(idMm), "tersedia");
                    // Memuat ulang data setelah penghapusan
                    Platform.runLater(() -> {
                        loadDataTransaksiFromAPI();
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

    private void updateTransaksi() {
        if (!isInputValid()) return;

        try {
            // Data Pelanggan
            String pelangganId = String.valueOf(idP); // ID pelanggan yang diambil dari pelanggan yang dipilih
            String pelangganNama = namaPelangganTransaksi; // Nama pelanggan yang diambil dari pelanggan yang dipilih
            String pelangganAlamat = alamatPelangganTransaksi; // Alamat pelanggan
            String pelangganNoTelepon = noTelpPelangganTransaksi; // No telepon pelanggan
            String pelangganEmail = emailPelangganTransaksi; // Email pelanggan

            // Data Mobil
            String mobilId = String.valueOf(idM); // ID mobil yang diambil dari mobil yang dipilih
            String mobilMerk = merkMobilTransaksi; // Merk mobil
            String mobilModel = modelMobilTransaksi; // Model mobil
            String mobilTahun = String.valueOf(tahunMobilTransaksi); // Tahun mobil
            String mobilPlatNomor = platMobilTransaksi; // Plat nomor mobil
            String mobilHargaSewaPerHari = hargaSewaMobilTransaksi; // Harga sewa per hari mobil
            String mobilStatus = statusMobilTransaksi; // Status mobil

            // Data Transaksi
            String tanggalMulai = TanggalMulaiDatePicker.getValue().toString(); // Ambil tanggal mulai
            String tanggalSelesai = TanggalSelesaiDatePicker.getValue().toString(); // Ambil tanggal selesai

            // Menghitung jumlah hari antara tanggal mulai dan tanggal selesai
            LocalDate mulai = TanggalMulaiDatePicker.getValue();
            LocalDate selesai = TanggalSelesaiDatePicker.getValue();
            double jumlahHari = ChronoUnit.DAYS.between(mulai, selesai); // Menghitung selisih hari

            // Menghitung total harga berdasarkan jumlah hari
            double hargaSewaMobil = Double.parseDouble(mobilHargaSewaPerHari);
            double totalHarga = hargaSewaMobil * jumlahHari; // Total harga dihitung berdasarkan harga sewa per hari dan jumlah hari

            // Format total harga, menghilangkan .0 jika total harga adalah angka bulat
            String totalHargaString = (totalHarga % 1 == 0) ? String.format("%.0f", totalHarga) : String.format("%.2f", totalHarga);

            // Ambil denda dari input
            String denda = DendaField.getText(); // Ambil denda
            // Status transaksi, misalnya "berlangsung"
            String statusTransaksi = "berlangsung";

            // Format JSON sesuai dengan struktur yang diinginkan
            String json = String.format("{\"pelanggan\":{" +
                            "\"idPelanggan\":%s, " +
                            "\"nama\":\"%s\", " +
                            "\"alamat\":\"%s\", " +
                            "\"noTelepon\":\"%s\", " +
                            "\"email\":\"%s\"}, " +
                            "\"mobil\":{" +
                            "\"idMobil\":%s, " +
                            "\"merk\":\"%s\", " +
                            "\"model\":\"%s\", " +
                            "\"tahun\":%s, " +
                            "\"platNomor\":\"%s\", " +
                            "\"hargaSewaPerHari\":%s, " +
                            "\"status\":\"%s\"}, " +
                            "\"tanggalMulai\":\"%s\", " +
                            "\"tanggalSelesai\":\"%s\", " +
                            "\"totalHarga\":%s, " +
                            "\"denda\":%s, " +
                            "\"status\":\"%s\"}",
                    pelangganId, pelangganNama, pelangganAlamat, pelangganNoTelepon, pelangganEmail,
                    mobilId, mobilMerk, mobilModel, mobilTahun, mobilPlatNomor, mobilHargaSewaPerHari, mobilStatus,
                    tanggalMulai, tanggalSelesai, totalHargaString, denda, statusTransaksi);
            System.out.println("data edit = " + json);

            // URL untuk API transaksi, sesuaikan dengan ID transaksi yang ingin diupdate
            URL url = new URL(ApiUrl.getapiUrl() + "api/transaksi/" + selectedTransaksiId);  // Ganti selectedTransaksiId dengan ID transaksi yang sesuai
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");  // Mengubah metode menjadi PUT untuk update
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.getOutputStream().write(json.getBytes());

            // Cek response dari API
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                loadDataTransaksiFromAPI();  // Update data setelah berhasil mengupdate transaksi
                clearFields(); // Bersihkan input field
                showAlert("Berhasil", "Data transaksi berhasil diperbarui!", Alert.AlertType.INFORMATION);  // Menampilkan pesan sukses
            } else {
                showAlert("Error", "Gagal memperbarui transaksi. Kode respons: " + responseCode, Alert.AlertType.ERROR);  // Menampilkan pesan error
            }
        } catch (Exception ex) {
            showAlert("Error", "Gagal memperbarui data transaksi: " + ex.getMessage(), Alert.AlertType.ERROR);
            ex.printStackTrace(); // Print stack trace di konsol untuk debugging
        }
    }

    private void transaksiSelesai(int transaksiId) {
        try {
            // Data Pelanggan
            String pelangganId = String.valueOf(idPp); // ID pelanggan yang diambil dari pelanggan yang dipilih
            String pelangganNama = namaPelangganTransaksip; // Nama pelanggan yang diambil dari pelanggan yang dipilih
            String pelangganAlamat = alamatPelangganTransaksip; // Alamat pelanggan
            String pelangganNoTelepon = noTelpPelangganTransaksip; // No telepon pelanggan
            String pelangganEmail = emailPelangganTransaksip; // Email pelanggan

            // Data Mobil
            String mobilId = String.valueOf(idMm); // ID mobil yang diambil dari mobil yang dipilih
            String mobilMerk = merkMobilTransaksit; // Merk mobil
            String mobilModel = modelMobilTransaksit; // Model mobil
            String mobilTahun = String.valueOf(tahunMobilTransaksit); // Tahun mobil
            String mobilPlatNomor = platMobilTransaksit; // Plat nomor mobil
            String mobilHargaSewaPerHari = hargaSewaMobilTransaksit; // Harga sewa per hari mobil
            String mobilStatus = statusMobilTransaksit; // Status mobil

            // Data Transaksi
            String tanggalMulai = TanggalMulaiDatePickerp; // Ambil tanggal mulai
            String tanggalSelesai = TanggalSelesaiDatePickerp; // Ambil tanggal selesai

            // Menghitung jumlah hari antara tanggal mulai dan tanggal selesai
            LocalDate mulai = LocalDate.parse(TanggalMulaiDatePickerp);
            LocalDate selesai = LocalDate.parse(TanggalSelesaiDatePickerp);
            double jumlahHari = ChronoUnit.DAYS.between(mulai, selesai); // Menghitung selisih hari

            // Menghitung total harga berdasarkan jumlah hari
            double hargaSewaMobil = Double.parseDouble(mobilHargaSewaPerHari);
            double totalHarga = hargaSewaMobil * jumlahHari; // Total harga dihitung berdasarkan harga sewa per hari dan jumlah hari

            // Format total harga, menghilangkan .0 jika total harga adalah angka bulat
            String totalHargaString = (totalHarga % 1 == 0) ? String.format("%.0f", totalHarga) : String.format("%.2f", totalHarga);

            // Ambil denda dari input
            String denda = DendaFieldp; // Ambil denda

            // Hitung denda jika melewati tanggal selesai
            LocalDate tanggalSelesaiDate = LocalDate.parse(tanggalSelesai);
            LocalDate tanggalSekarang = LocalDate.now();
            long hariTerlambat = ChronoUnit.DAYS.between(tanggalSelesaiDate, tanggalSekarang);

            if (hariTerlambat > 0) {
                double dendaPerHari = 100000; // Contoh: denda Rp 100.000 per hari
                double totalDenda = Double.parseDouble(denda) + (hariTerlambat * dendaPerHari);
                denda = String.valueOf(totalDenda);
            }

            // Status transaksi yang diubah menjadi selesai
            String statusTransaksi = "selesai";

            // Format JSON sesuai dengan struktur yang diinginkan
            String json = String.format("{\"pelanggan\":{" +
                            "\"idPelanggan\":%s, " +
                            "\"nama\":\"%s\", " +
                            "\"alamat\":\"%s\", " +
                            "\"noTelepon\":\"%s\", " +
                            "\"email\":\"%s\"}, " +
                            "\"mobil\":{" +
                            "\"idMobil\":%s, " +
                            "\"merk\":\"%s\", " +
                            "\"model\":\"%s\", " +
                            "\"tahun\":%s, " +
                            "\"platNomor\":\"%s\", " +
                            "\"hargaSewaPerHari\":%s, " +
                            "\"status\":\"%s\"}, " +
                            "\"tanggalMulai\":\"%s\", " +
                            "\"tanggalSelesai\":\"%s\", " +
                            "\"totalHarga\":%s, " +
                            "\"denda\":%s, " +
                            "\"status\":\"%s\"}",
                    pelangganId, pelangganNama, pelangganAlamat, pelangganNoTelepon, pelangganEmail,
                    mobilId, mobilMerk, mobilModel, mobilTahun, mobilPlatNomor, mobilHargaSewaPerHari, mobilStatus,
                    tanggalMulai, tanggalSelesai, totalHargaString, denda, statusTransaksi);

            System.out.println("JSON yang dikirim: " + json);

            // URL untuk API transaksi, sesuaikan dengan ID transaksi yang ingin diupdate
            URL url = new URL(ApiUrl.getapiUrl() + "api/transaksi/" + transaksiId);  // Ganti selectedTransaksiId dengan ID transaksi yang sesuai
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");  // Mengubah metode menjadi PUT untuk update
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.getOutputStream().write(json.getBytes());

            // Cek response dari API
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                loadDataTransaksiFromAPI();  // Update data setelah berhasil mengupdate transaksi
                // clearFields(); // Bersihkan input field
                updateCar(mobilId, "tersedia");
                showAlert("Berhasil", "Transaksi berhasil diperbarui menjadi selesai!", Alert.AlertType.INFORMATION);  // Menampilkan pesan sukses
            } else {
                showAlert("Error", "Gagal memperbarui transaksi. Kode respons: " + responseCode, Alert.AlertType.ERROR);  // Menampilkan pesan error
            }
        } catch (Exception ex) {
            showAlert("Error", "Gagal memperbarui data transaksi: " + ex.getMessage(), Alert.AlertType.ERROR);
            ex.printStackTrace(); // Print stack trace di konsol untuk debugging
        }
    }

    private void clearFields() {
        PelangganField.setText("");
        MobilField.setText("");
        TanggalMulaiDatePicker.setValue(null);
        TanggalSelesaiDatePicker.setValue(null);
        TotalHargaField.setText("");
        DendaField.setText("");
        selectedTransaksiId = -1;
        SaveButton.setText("Save");
    }

    @FXML
    protected void SaveButtonClick() {
        if (SaveButton.getText().equals("Save")) {
            saveTransaksi();
        } else if (SaveButton.getText().equals("Edit")) {
            updateTransaksi();
        }
    }

    @FXML
    protected void CancelButtonClick() {
        clearFields();
    }

    @FXML
    protected void BackButtonClick() {
        Stage currentStage = (Stage) TransaksiLabel.getScene().getWindow();
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
    protected void PelangganTableClick() {
        if (PelangganTable.getSelectionModel().getSelectedItem() != null) {
            // Ambil data pelanggan dari baris yang dipilih
            PelangganData selectedPelanggan = PelangganTable.getSelectionModel().getSelectedItem();

            // Isi field dengan data dari pelanggan yang dipilih
            namaPelanggan = selectedPelanggan.getNama();
            alamatPelanggan = selectedPelanggan.getAlamat();
            noTelpPelanggan = selectedPelanggan.getNoTelepon();
            emailPelanggan = selectedPelanggan.getEmail();

            // Simpan ID pelanggan untuk keperluan edit atau delete
            idPelangganIndex = selectedPelanggan.getIdPelanggan();

            // isi field
            PelangganField.setText(namaPelanggan);
        }
    }

    @FXML
    protected void MobilTableClick() {
        if (MobilTable.getSelectionModel().getSelectedItem() != null) {
            // Ambil data mobil dari baris yang dipilih
            MobilData selectedMobil = MobilTable.getSelectionModel().getSelectedItem();

            // Cek status mobil
            if ("disewa".equalsIgnoreCase(selectedMobil.getStatus())) {
                // Jika status mobil adalah "disewa", tampilkan dialog dan kembalikan
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Peringatan");
                alert.setHeaderText(null);
                alert.setContentText("Mobil sudah disewa!");
                alert.showAndWait();
                return; // Keluar dari metode, tidak lanjut ke proses berikutnya
            }

            // Jika status mobil bukan "disewa", lanjutkan proses
            // Isi field dengan data dari mobil yang dipilih
            merkMobil = selectedMobil.getMerk();
            modelMobil = selectedMobil.getModel();
            tahunMobil = selectedMobil.getTahun();
            platMobil = selectedMobil.getPlatNomor();
            hargaSewaMobil = selectedMobil.getHargaSewa();
            statusMobil = selectedMobil.getStatus();

            // Simpan ID mobil untuk keperluan edit atau delete
            idMobilIndex = selectedMobil.getIdMobil();

            // Isi field
            MobilField.setText(platMobil);

            calculateTotalHarga();
        }
    }

    @FXML
    protected  void TransaksiTableClick() {
        if (TransaksiTable.getSelectionModel().getSelectedItem() != null) {
            // Ambil data pelanggan dari baris yang dipilih
            TransaksiData selectedTransaksi = TransaksiTable.getSelectionModel().getSelectedItem();

            // Isi field dengan data dari pelanggan yang dipilih
            PelangganField.setText(selectedTransaksi.getPelanggan().getNama());
            MobilField.setText(selectedTransaksi.getMobil().getPlatNomor());
            TanggalMulaiDatePicker.setValue(LocalDate.parse(selectedTransaksi.getTanggalMulai()));
            TanggalSelesaiDatePicker.setValue(LocalDate.parse(selectedTransaksi.getTanggalSelesai()));
            TotalHargaField.setText( selectedTransaksi.getTotalHarga());
            DendaField.setText( selectedTransaksi.getDenda());

            selectedTransaksiId = selectedTransaksi.getIdTransaksi();

            // Masukkan data ke variabel transaksi
            idM = selectedTransaksi.getMobil().getIdMobil();
            idP = selectedTransaksi.getPelanggan().getIdPelanggan();
            namaPelangganTransaksi = selectedTransaksi.getPelanggan().getNama();
            alamatPelangganTransaksi = selectedTransaksi.getPelanggan().getAlamat();
            noTelpPelangganTransaksi = selectedTransaksi.getPelanggan().getNoTelepon();
            emailPelangganTransaksi = selectedTransaksi.getPelanggan().getEmail();

            merkMobilTransaksi = selectedTransaksi.getMobil().getMerk();
            modelMobilTransaksi = selectedTransaksi.getMobil().getModel();
            tahunMobilTransaksi = String.valueOf(selectedTransaksi.getMobil().getTahun());
            platMobilTransaksi = selectedTransaksi.getMobil().getPlatNomor();
            hargaSewaMobilTransaksi = String.valueOf(selectedTransaksi.getMobil().getHargaSewa());
            statusMobilTransaksi = selectedTransaksi.getMobil().getStatus();

            SaveButton.setText("Edit");
        }
    }
}
