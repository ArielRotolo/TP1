import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.Objects;

@Getter
@Setter
public class Materia {
    private String nombre;
    private Set<Materia> correlativas;

    Materia(String materia, Set<Materia> correlativas) {
        this.nombre = Objects.requireNonNull(materia, "Debe proporcionar un nombre para la materia");
        this.correlativas = Objects.requireNonNull(correlativas, "Debe proporcionar un Set");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Materia otraMateria = (Materia) o;
        return Objects.equals(nombre, otraMateria.nombre); // Asumo que 2 materias son las mismas si comparten el mismo nombre
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre); // Calculo el hash únicamente con el nombre de la materia
    }
}
