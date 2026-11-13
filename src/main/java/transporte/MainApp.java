package transporte;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class MainApp extends Application {

    private final VehiculoDAO dao = new VehiculoDAO();
    private final ObservableList<Vehiculo> datos = FXCollections.observableArrayList();
    private TableView<Vehiculo> tabla;

    private final TextField txtPlaca = new TextField();
    private final TextField txtMarca = new TextField();
    private final TextField txtModelo = new TextField();
    private final TextField txtCapacidad = new TextField();
    private final TextField txtConductor = new TextField();
    private final DatePicker dpMantenimiento = new DatePicker();
    private final ComboBox<String> cbEstado =
        new ComboBox<>(FXCollections.observableArrayList("Activo", "En mantenimiento", "Inactivo"));

    private Vehiculo seleccionado;

    @Override
    public void start(Stage stage) {
        cbEstado.setValue("Activo");

        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(15));
        form.addRow(0, new Label("Placa:"), txtPlaca, new Label("Marca:"), txtMarca);
        form.addRow(1, new Label("Modelo:"), txtModelo, new Label("Capacidad:"), txtCapacidad);
        form.addRow(2, new Label("Conductor:"), txtConductor, new Label("F. mantenimiento:"), dpMantenimiento);
        form.addRow(3, new Label("Estado:"), cbEstado);

        Button btnGuardar = new Button("Registrar");
        Button btnActualizar = new Button("Actualizar");
        Button btnEliminar = new Button("Eliminar");
        Button btnLimpiar = new Button("Limpiar");
        HBox botones = new HBox(10, btnGuardar, btnActualizar, btnEliminar, btnLimpiar);
        botones.setPadding(new Insets(0, 15, 10, 15));

        tabla = new TableView<>(datos);
        agregarColumna("ID", "idVehiculo");
        agregarColumna("Placa", "placa");
        agregarColumna("Marca", "marca");
        agregarColumna("Modelo", "modelo");
        agregarColumna("Capacidad", "capacidad");
        agregarColumna("Conductor", "conductor");
        agregarColumna("Mantenimiento", "fechaMantenimiento");
        agregarColumna("Estado", "estado");
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, old, v) -> {
            if (v != null) cargarFormulario(v);
        });

        btnGuardar.setOnAction(e -> registrar());
        btnActualizar.setOnAction(e -> actualizar());
        btnEliminar.setOnAction(e -> eliminar());
        btnLimpiar.setOnAction(e -> limpiar());

        VBox root = new VBox(form, botones, tabla);
        VBox.setVgrow(tabla, Priority.ALWAYS);

        refrescar();
        stage.setTitle("Gestión de Flota - Empresa de Transporte");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }

    private void agregarColumna(String titulo, String propiedad) {
        TableColumn<Vehiculo, Object> col = new TableColumn<>(titulo);
        col.setCellValueFactory(new PropertyValueFactory<>(propiedad));
        tabla.getColumns().add(col);
    }

    private void refrescar() {
        try {
            datos.setAll(dao.listar());
        } catch (Exception ex) {
            alerta(Alert.AlertType.ERROR, "No se pudo conectar/consultar: " + ex.getMessage());
        }
    }

    private Vehiculo leerFormulario() {
        String placa = txtPlaca.getText().trim().toUpperCase();
        String marca = txtMarca.getText().trim();
        String modelo = txtModelo.getText().trim();
        String conductor = txtConductor.getText().trim();

        if (placa.isEmpty() || marca.isEmpty() || modelo.isEmpty() || conductor.isEmpty()
                || dpMantenimiento.getValue() == null) {
            alerta(Alert.AlertType.WARNING, "Complete todos los campos.");
            return null;
        }
        if (!placa.matches("[A-Z0-9]{3}-[A-Z0-9]{3}")) {
            alerta(Alert.AlertType.WARNING, "La placa debe tener el formato ABC-123.");
            return null;
        }
        int capacidad;
        try {
            capacidad = Integer.parseInt(txtCapacidad.getText().trim());
            if (capacidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            alerta(Alert.AlertType.WARNING, "La capacidad debe ser un número entero mayor a 0.");
            return null;
        }
        LocalDate fecha = dpMantenimiento.getValue();
        return new Vehiculo(0, placa, marca, modelo, capacidad, conductor, fecha, cbEstado.getValue());
    }

    private void registrar() {
        Vehiculo v = leerFormulario();
        if (v == null) return;
        try {
            dao.insertar(v);
            refrescar(); limpiar();
            alerta(Alert.AlertType.INFORMATION, "Vehículo registrado.");
        } catch (Exception ex) {
            alerta(Alert.AlertType.ERROR, "Error al registrar (¿placa repetida?): " + ex.getMessage());
        }
    }

    private void actualizar() {
        if (seleccionado == null) {
            alerta(Alert.AlertType.WARNING, "Seleccione un registro de la tabla.");
            return;
        }
        Vehiculo v = leerFormulario();
        if (v == null) return;
        v.setIdVehiculo(seleccionado.getIdVehiculo());
        try {
            dao.actualizar(v);
            refrescar(); limpiar();
            alerta(Alert.AlertType.INFORMATION, "Vehículo actualizado.");
        } catch (Exception ex) {
            alerta(Alert.AlertType.ERROR, "Error al actualizar: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionado == null) {
            alerta(Alert.AlertType.WARNING, "Seleccione un registro de la tabla.");
            return;
        }
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION,
            "¿Eliminar el vehículo " + seleccionado.getPlaca() + "?", ButtonType.YES, ButtonType.NO);
        if (conf.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) return;
        try {
            dao.eliminar(seleccionado.getIdVehiculo());
            refrescar(); limpiar();
        } catch (Exception ex) {
            alerta(Alert.AlertType.ERROR, "Error al eliminar: " + ex.getMessage());
        }
    }

    private void cargarFormulario(Vehiculo v) {
        seleccionado = v;
        txtPlaca.setText(v.getPlaca());
        txtMarca.setText(v.getMarca());
        txtModelo.setText(v.getModelo());
        txtCapacidad.setText(String.valueOf(v.getCapacidad()));
        txtConductor.setText(v.getConductor());
        dpMantenimiento.setValue(v.getFechaMantenimiento());
        cbEstado.setValue(v.getEstado());
    }

    private void limpiar() {
        seleccionado = null;
        txtPlaca.clear(); txtMarca.clear(); txtModelo.clear();
        txtCapacidad.clear(); txtConductor.clear();
        dpMantenimiento.setValue(null);
        cbEstado.setValue("Activo");
        tabla.getSelectionModel().clearSelection();
    }

    private void alerta(Alert.AlertType tipo, String msg) {
        new Alert(tipo, msg).showAndWait();
    }

    public static void main(String[] args) { launch(args); }
}
