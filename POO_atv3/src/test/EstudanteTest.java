package test;

import ATV.Estudante;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EstudanteTest {
    private Estudante estudante;

    @BeforeEach
    public void setUp() {
        estudante = new Estudante(10, " Ana Clara ", " ana@ufc.br ");
    }

    @Test
    public void testCriacaoEstudante() {
        assertEquals(10, estudante.getId());
        assertEquals("Ana Clara", estudante.getNome());
        assertEquals("ana@ufc.br", estudante.getEmail());
    }

    @Test
    public void testPrazoEmprestimoEstudante() {
        assertEquals(7, estudante.getPrazoEmprestimo());
    }

    @Test
    public void testToCsvEstudante() {
        assertEquals("ESTUDANTE;10;Ana Clara;ana@ufc.br", estudante.toCsv());
    }
}