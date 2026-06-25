package test;

import ATV.Professor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProfessorTest {
    private Professor professor;

    @BeforeEach
    public void setUp() {
        professor = new Professor(30, "Robertty Costa", "robertty@ufc.br");
    }

    @Test
    public void testCriacaoProfessor() {
        assertEquals(30, professor.getId());
        assertEquals("Robertty Costa", professor.getNome());
        assertEquals("robertty@ufc.br", professor.getEmail());
    }

    @Test
    public void testPrazoEmprestimoProfessor() {
        assertEquals(14, professor.getPrazoEmprestimo());
    }

    @Test
    public void testToCsvProfessor() {
        String esperado = "PROFESSOR;30;Robertty Costa;robertty@ufc.br";
        assertEquals(esperado, professor.toCsv());
    }
}