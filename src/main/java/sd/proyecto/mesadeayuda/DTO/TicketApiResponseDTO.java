package sd.proyecto.mesadeayuda.DTO;

public class TicketApiResponseDTO {

    private Long id;

    private String titulo;

    private String descripcion;

    private Integer prioridad;

    private String responsable;

    private Long usuarioId;

    private String usuarioNombre;

    public TicketApiResponseDTO() {
    }

    public TicketApiResponseDTO(
            Long id,
            String titulo,
            String descripcion,
            Integer prioridad,
            String responsable,
            Long usuarioId,
            String usuarioNombre) {

        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.responsable = responsable;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(
            Long id) {

        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(
            String titulo) {

        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(
            String descripcion) {

        this.descripcion = descripcion;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(
            Integer prioridad) {

        this.prioridad = prioridad;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(
            String responsable) {

        this.responsable = responsable;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(
            Long usuarioId) {

        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(
            String usuarioNombre) {

        this.usuarioNombre = usuarioNombre;
    }
}