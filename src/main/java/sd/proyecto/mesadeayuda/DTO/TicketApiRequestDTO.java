package sd.proyecto.mesadeayuda.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class TicketApiRequestDTO {

        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150, message = "El título no puede superar 150 caracteres")
        private String titulo;

        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 1000, message = "La descripción no puede superar 1000 caracteres")
        private String descripcion;

        @NotNull(message = "La prioridad es obligatoria")
        @Min(value = 1, message = "La prioridad debe estar entre 1 y 3")
        @Max(value = 3, message = "La prioridad debe estar entre 1 y 3")
        private Integer prioridad;

        @NotBlank(message = "El responsable es obligatorio")
        @Size(max = 150, message = "El responsable no puede superar 150 caracteres")
        private String responsable;

        @NotNull(message = "El usuario es obligatorio")
        @Positive(message = "El ID del usuario debe ser positivo")
        private Long usuarioId;

        public TicketApiRequestDTO() {
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
}