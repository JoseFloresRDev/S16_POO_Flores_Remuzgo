package transporte;

import java.time.LocalDate;

public class Vehiculo {
    private int idVehiculo;
    private String placa, marca, modelo, conductor, estado;
    private int capacidad;
    private LocalDate fechaMantenimiento;

    public Vehiculo() {}

    public Vehiculo(int id, String placa, String marca, String modelo, int capacidad,
                    String conductor, LocalDate fecha, String estado) {
        this.idVehiculo = id; this.placa = placa; this.marca = marca; this.modelo = modelo;
        this.capacidad = capacidad; this.conductor = conductor;
        this.fechaMantenimiento = fecha; this.estado = estado;
    }

    public int getIdVehiculo() { return idVehiculo; }
    public void setIdVehiculo(int v) { idVehiculo = v; }
    public String getPlaca() { return placa; }
    public void setPlaca(String v) { placa = v; }
    public String getMarca() { return marca; }
    public void setMarca(String v) { marca = v; }
    public String getModelo() { return modelo; }
    public void setModelo(String v) { modelo = v; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int v) { capacidad = v; }
    public String getConductor() { return conductor; }
    public void setConductor(String v) { conductor = v; }
    public LocalDate getFechaMantenimiento() { return fechaMantenimiento; }
    public void setFechaMantenimiento(LocalDate v) { fechaMantenimiento = v; }
    public String getEstado() { return estado; }
    public void setEstado(String v) { estado = v; }
}
