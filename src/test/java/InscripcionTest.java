import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InscripcionTest {

    // Materias
    private Materia algoritmos;
    private Materia paradigmas;
    private Materia diseno;
    private Materia fisica1;
    private Materia analisis1;
    private Materia analisis2;

    // Alumnos
    private Alumno alumnoJuan; // Aprobó Algoritmos
    private Alumno alumnoMaria; // Aprobó Algoritmos, Paradigmas, Analisis1, Fisica1
    private Alumno alumnoPedro; // No aprobó nada

    @BeforeEach // Todos los test se inician con este setUp
    void setUp() {

        // Materias sin correlativas (Set vacío)
        algoritmos = new Materia("Algoritmos y Estructuras de Datos", new HashSet<>());
        analisis1 = new Materia("Análisis Matemático 1", new HashSet<>());
        fisica1 = new Materia("Física 1", new HashSet<>());

        // Materias con correlativas
        paradigmas = new Materia("Paradigmas de Programación", Set.of(algoritmos)); // Requiere Algoritmos
        diseno = new Materia("Diseño de Sistemas", Set.of(paradigmas)); // Requiere Paradigmas
        analisis2 = new Materia("Análisis Matemático 2", Set.of(analisis1, fisica1)); // Requiere Analisis1 y Fisica1

        // Materia aprobada por Juan
        Set<Materia> aprobadasJuan = new HashSet<>();
        aprobadasJuan.add(algoritmos);
        alumnoJuan = new Alumno("Juan", "Perez", 12345, aprobadasJuan);

        // Materias aprobadas por Maria
        Set<Materia> aprobadasMaria = new HashSet<>();
        aprobadasMaria.add(algoritmos);
        aprobadasMaria.add(paradigmas);
        aprobadasMaria.add(analisis1);
        aprobadasMaria.add(fisica1);
        alumnoMaria = new Alumno("Maria", "Lopez", 67890, aprobadasMaria);

        // Pedro no aprobó nada
        alumnoPedro = new Alumno("Pedro", "Gomez", 11223, new HashSet<>());
    }

    // Tests Exitosos [aprobada() debería retornar true]
    @Test
    @DisplayName("Inscripción APROBADA: Materia sin correlativas (Alumno Nuevo)")
    void inscripcionAprobadaMateriaSinCorrelativas() {
        // Algoritmos no requiere correlativas
        Inscripcion inscripcion = new Inscripcion(alumnoPedro, Set.of(algoritmos));
        assertTrue(inscripcion.aprobada(), "Pedro debería poder inscribirse a Algoritmos");
    }

    @Test
    @DisplayName("Inscripción APROBADA: Cumple correlativa simple")
    void inscripcionAprobadaCumpleCorrelativaSimple() {
        // Juan tiene Algoritmos y se inscribe a Paradigmas que requiere Algoritmos
        Inscripcion inscripcion = new Inscripcion(alumnoJuan, Set.of(paradigmas));
        assertTrue(inscripcion.aprobada(), "Juan debería poder inscribirse a Paradigmas");
    }

    @Test
    @DisplayName("Inscripción APROBADA: Cumple correlativas múltiples")
    void inscripcionAprobadaCumpleCorrelativasMultiples() {
        // Maria tiene Analisis1 y Fisica1, se inscribe a Analisis2 que requiere ambas
        Inscripcion inscripcion = new Inscripcion(alumnoMaria, Set.of(analisis2));
        assertTrue(inscripcion.aprobada(), "Maria debería poder inscribirse a Analisis 2");
    }

    @Test
    @DisplayName("Inscripción APROBADA: Múltiples materias, todas cumplen")
    void inscripcionAprobadaMultiplesMateriasTodasCumplen() {
        // Maria tiene Algoritmos, Paradigmas, Analisis1, Fisica1 se inscribe a
        // Diseño requiere Paradigmas (cumple) y Analisis2 requiere Analisis1 y Fisica1 (cumple)
        Inscripcion inscripcion = new Inscripcion(alumnoMaria, Set.of(diseno, analisis2));
        assertTrue(inscripcion.aprobada(), "Maria debería poder inscribirse a Diseño y Analisis2");
        assertEquals("El alumno Maria Lopez ha sido inscrito en todas las materias [Inscripción Aprobada]\n", inscripcion.resultado());
    }

    @Test
    @DisplayName("Inscripción APROBADA: Múltiples materias, algunas sin correlativas")
    void inscripcionAprobadaMultiplesMateriasAlgunasSinCorrelativas() {
        // Juan tiene Algoritmos se inscribe a Paradigmas que requiere Algoritmos (cumple) y Fisica1 (cumple)
        Inscripcion inscripcion = new Inscripcion(alumnoJuan, Set.of(paradigmas, fisica1));
        assertTrue(inscripcion.aprobada(), "Juan debería poder inscribirse a Paradigmas y Fisica1");
    }

    // Tests de Rechazo [aprobada() debe retornar false]
    @Test
    @DisplayName("Inscripción RECHAZADA: No cumple correlativa simple")
    void inscripcionRechazadaNoCumpleCorrelativaSimple() {
        // Pedro intenta inscribirse a Paradigmas requiere Algoritmos (no cumple)
        Inscripcion inscripcion = new Inscripcion(alumnoPedro, Set.of(paradigmas));
        assertFalse(inscripcion.aprobada(), "Pedro NO debería poder inscribirse a Paradigmas");
        // Verifico que el resultado tenga Inscripción Rechazada por las dudas
        assertTrue(inscripcion.resultado().endsWith("[Inscripción Rechazada]\n"));
    }

    @Test
    @DisplayName("Inscripción RECHAZADA: No cumple una de varias correlativas")
    void inscripcionRechazadaNoCumpleUnaCorrelativaMultiple() {
        // Alumno solo con Analisis1 intenta Analisis2 que requiere Analisis1 y Fisica1
        Alumno alumnoSoloAnalisis1 = new Alumno("Ana", "Lopez", 55555, Set.of(analisis1));
        Inscripcion inscripcion = new Inscripcion(alumnoSoloAnalisis1, Set.of(analisis2));
        assertFalse(inscripcion.aprobada(), "Ana NO debería poder inscribirse a Analisis 2 (falta Fisica1)");
        assertTrue(inscripcion.resultado().endsWith("[Inscripción Rechazada]\n"));
    }

    @Test
    @DisplayName("Inscripción RECHAZADA: Materia ya aprobada (inscripción única)")
    void inscripcionRechazadaMateriaYaAprobadaUnica() {
        // Juan que tiene Algoritmos intenta inscribirse a Algoritmos
        Inscripcion inscripcion = new Inscripcion(alumnoJuan, Set.of(algoritmos));
        assertFalse(inscripcion.aprobada(), "Juan NO debería poder inscribirse a Algoritmos de nuevo");
        assertTrue(inscripcion.resultado().endsWith("[Inscripción Rechazada]\n"));
    }

    @Test
    @DisplayName("Inscripción RECHAZADA: Múltiples materias, una ya aprobada")
    void inscripcionRechazadaMultiplesMateriasUnaYaAprobada() {
        // Juan tiene Algoritmos intenta inscribirse a Paradigmas (cumple) y Algoritmos (ya aprobada)
        Inscripcion inscripcion = new Inscripcion(alumnoJuan, Set.of(paradigmas, algoritmos));
        assertFalse(inscripcion.aprobada(), "Inscripción de Juan debe fallar porque Algoritmos ya está aprobada");
        assertTrue(inscripcion.resultado().endsWith("[Inscripción Rechazada]\n"));
    }

    @Test
    @DisplayName("Inscripción RECHAZADA: Múltiples materias, una no cumple correlativas")
    void inscripcionRechazadaMultiplesMateriasUnaFallaCorrelativas() {
        // Juan tiene Algoritmos intenta inscribirse a Paradigmas (cumple) y Diseño que requiere Paradigmas (no cumple)
        Inscripcion inscripcion = new Inscripcion(alumnoJuan, Set.of(paradigmas, diseno));
        assertFalse(inscripcion.aprobada(), "Inscripción de Juan debe fallar porque Diseño no cumple correlativas");
        assertTrue(inscripcion.resultado().endsWith("[Inscripción Rechazada]\n"));
    }

    @Test
    @DisplayName("Inscripción RECHAZADA: Múltiples materias, una falla correlativas y otra está aprobada")
    void inscripcionRechazadaMultiplesMateriasFallaCorrelativaYAprobada() {
        // Maria que tiene Algoritmos, Paradigmas, Analisis1, Fisica1 intenta inscribirse a
        // Diseño que requiere Paradigmas (cumple)
        // Analisis2 que requiere Analisis1, Fisica1 (cumple)
        // Paradigmas (Ya aprobada)
        Inscripcion inscripcion = new Inscripcion(alumnoMaria, Set.of(diseno, analisis2, paradigmas));
        assertFalse(inscripcion.aprobada(), "Inscripción de Maria debe fallar porque Paradigmas ya está aprobada");
        assertTrue(inscripcion.resultado().endsWith("[Inscripción Rechazada]\n"));

        // Juan que tiene Algoritmos intenta:
        // Paradigmas que requiere Algoritmos (cumple)
        // Algoritmos (ya aprobada)
        // Diseño que requiere Paradigmas (no cumple)
        Inscripcion inscripcion2 = new Inscripcion(alumnoJuan, Set.of(paradigmas, algoritmos, diseno));
        assertFalse(inscripcion2.aprobada(), "Inscripción de Juan debe fallar (por Algoritmos y/o Diseño)");
        assertTrue(inscripcion2.resultado().endsWith("[Inscripción Rechazada]\n"));
    }
}
