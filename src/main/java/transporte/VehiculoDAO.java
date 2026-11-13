package transporte;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    public List<Vehiculo> listar() throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM vehiculo ORDER BY id_vehiculo";
        try (Connection c = Conexion.getConexion();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Vehiculo(
                    rs.getInt("id_vehiculo"), rs.getString("placa"),
                    rs.getString("marca"), rs.getString("modelo"),
                    rs.getInt("capacidad"), rs.getString("conductor"),
                    rs.getDate("fecha_mantenimiento").toLocalDate(),
                    rs.getString("estado")));
            }
        }
        return lista;
    }

    public void insertar(Vehiculo v) throws SQLException {
        String sql = "INSERT INTO vehiculo (placa, marca, modelo, capacidad, conductor, fecha_mantenimiento, estado) VALUES (?,?,?,?,?,?,?)";
        try (Connection c = Conexion.getConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setInt(4, v.getCapacidad());
            ps.setString(5, v.getConductor());
            ps.setDate(6, Date.valueOf(v.getFechaMantenimiento()));
            ps.setString(7, v.getEstado());
            ps.executeUpdate();
        }
    }

    public void actualizar(Vehiculo v) throws SQLException {
        String sql = "UPDATE vehiculo SET placa=?, marca=?, modelo=?, capacidad=?, conductor=?, fecha_mantenimiento=?, estado=? WHERE id_vehiculo=?";
        try (Connection c = Conexion.getConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setInt(4, v.getCapacidad());
            ps.setString(5, v.getConductor());
            ps.setDate(6, Date.valueOf(v.getFechaMantenimiento()));
            ps.setString(7, v.getEstado());
            ps.setInt(8, v.getIdVehiculo());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        try (Connection c = Conexion.getConexion();
             PreparedStatement ps = c.prepareStatement("DELETE FROM vehiculo WHERE id_vehiculo=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
