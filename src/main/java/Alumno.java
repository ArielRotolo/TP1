import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.Objects;

@Setter
@Getter
public class Alumno {
    private String nombre;
    private String apellido;
    private int legajo;
    private Set<Materia> materiasAprobadas;

    Alumno(String nombre, String apellido, int legajo, Set<Materia> materiasAprobadas) {
        this.nombre = Objects.requireNonNull(nombre, "Debe proporcionar un nombre para el alumno");
        this.apellido = Objects.requireNonNull(apellido, "Debe proporcionar un apellido para el alumno");
        this.materiasAprobadas = Objects.requireNonNull(materiasAprobadas, "Debe proporcionar un Set");
        if (legajo <= 0 || legajo > 9999999)
            throw new IllegalArgumentException("Legajo invalido! Debe ser un numero entre 1 y 9.999.999");
        this.legajo = legajo;
    }

    public boolean tieneAprobada(Materia materia) {
        return this.materiasAprobadas.contains(materia);
    }

    @Override
    public String toString() {
        return this.nombre + " " + this.apellido;
    }
}
