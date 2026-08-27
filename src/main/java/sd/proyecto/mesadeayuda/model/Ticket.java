package sd.proyecto.mesadeayuda.model;

public class Ticket {

    private Long id;
    private String titulo;
    private String descripcion;
    private String prioridad;
    private String responsable;

    public Ticket(Long id, String titulo, String descripcion, String prioridad, String responsable) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.responsable = responsable;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public String getResponsable() {
        return responsable;
    }
}
