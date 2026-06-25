package test;

import ATV.Livro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LivroTest {
    private Livro livro;

    @BeforeEach
    public void setUp() {
        // Usa o construtor da classe: codigo, titulo, autor, anop, disp
        livro = new Livro(1, "Dom Casmurro", "Machado de Assis", 1899, true);
    }

    @Test
    public void testCriacaoLivro() {
        assertEquals(1, livro.getCodigo());
        assertEquals("Dom Casmurro", livro.getTitulo());
        assertEquals("Machado de Assis", livro.getAutor());
        assertEquals(1899, livro.getAnop());
        assertTrue(livro.isDisp(), "O livro deve iniciar como disponível.");
    }

    @Test
    public void testAlterarDisponibilidade() {
        livro.setDisp(false);
        assertFalse(livro.isDisp(), "O status de disponibilidade deve mudar para falso.");
    }

    @Test
    public void testConvert() {
        String esperado = "1;Dom Casmurro;Machado de Assis;1899;true";
        assertEquals(esperado, livro.convert(), "O método convert() deve gerar a string de persistência corretamente.");
    }
}