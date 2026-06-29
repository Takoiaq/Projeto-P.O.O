package test;

import ATV.Bibliotecario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BibliotecarioTest {
    private Bibliotecario bibliotecario;

    @BeforeEach
    public void setUp() {
        bibliotecario = new Bibliotecario(30, " Robertty Costa ", " robertty@ufc.br ");
    }

    @Test
    public void testCriacaoBibliotecario() {
        assertEquals(30, bibliotecario.getId());
        assertEquals("Robertty Costa", bibliotecario.getNome());
        assertEquals("robertty@ufc.br", bibliotecario.getEmail());
    }

    @Test
    public void testPrazoEmprestimoBibliotecario() {
        assertEquals(35, bibliotecario.getPrazoEmprestimo());
    }

    @Test
    public void testToCsvBibliotecario() {
        String esperado = "BIBLIOTECARIO;30;Robertty Costa;robertty@ufc.br";
        assertEquals(esperado, bibliotecario.toCsv());
    }
}