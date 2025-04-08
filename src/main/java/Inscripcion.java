import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.Objects;

@Setter
@Getter
public class Inscripcion {
    private Alumno alumno; // Alumno que se inscribe
    private Set<Materia> materiasInscriptas; // Set de materias al que el alumno se inscribe

    Inscripcion(Alumno alumno, Set<Materia> materiasInscriptas) {
        this.alumno = Objects.requireNonNull(alumno, "Debe proporcionar un alumno para la inscripción");
        this.materiasInscriptas = Objects.requireNonNull(materiasInscriptas, "Debe proporcionar un Set no nulo");
        if (materiasInscriptas.isEmpty())
            throw new IllegalArgumentException("Debe proporcionar al menos una materia para inscribirse");
    }

    public boolean aprobada() {
        for (Materia materia : materiasInscriptas) {
            if (alumno.tieneAprobada(materia)) {
                System.out.print("El alumno " + alumno.toString() + " ya tiene aprobada " + materia.getNombre());
                return false; // El alumno se quiere inscribir a una materia que ya tiene aprobada
            }
            if (!cumpleCorrelativas(materia, alumno.getMateriasAprobadas())) {
                System.out.print("El alumno " + alumno.toString() + " no cumple correlativas para " + materia.getNombre());
                return false; // No cumple con las correlativas
            }
        }
        return true; // Inscripción aprobada
    }

    private boolean cumpleCorrelativas(Materia materia, Set<Materia> materiasAprobadas) {
        Set<Materia> correlativas = materia.getCorrelativas();
        return correlativas.isEmpty() || materiasAprobadas.containsAll(correlativas); // La materia no tiene correlativas o todas están aprobadas
    }

    public String resultado() {
        return this.aprobada() ? "El alumno " + alumno.toString() + " ha sido inscrito en todas las materias [Inscripción Aprobada]\n" : " [Inscripción Rechazada]\n";
    }
}
