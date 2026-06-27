package test;

import ATV.Livro;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LivroTest {
    private Livro livro;

    @BeforeEach
    public void setUp() {
        livro = new Livro(1, "Dom Casmurro", "Machado de Assis", 1899, 3);
    }

    @Test
    public void testCriacaoLivroComQuantidade() {
        assertEquals(1, livro.getCodigo());
        assertEquals("Dom Casmurro", livro.getTitulo());
        assertEquals("Machado de Assis", livro.getAutor());
        assertEquals(1899, livro.getAnop());
        assertEquals(3, livro.getQuantidade());
        assertTrue(livro.isDisp(), "O livro deve iniciar como disponível quando quantidade for maior que zero.");
    }

    @Test
    public void testCriacaoLivroComBooleanTrue() {
        Livro livroBoolean = new Livro(2, "Livro Antigo", "Autor Teste", 2020, true);

        assertEquals(1, livroBoolean.getQuantidade());
        assertTrue(livroBoolean.isDisp());
    }

    @Test
    public void testCriacaoLivroComBooleanFalse() {
        Livro livroBoolean = new Livro(3, "Livro Antigo 2", "Autor Teste", 2020, false);

        assertEquals(0, livroBoolean.getQuantidade());
        assertFalse(livroBoolean.isDisp());
    }

    @Test
    public void testEmprestarUnidade() {
        livro.emprestarUnidade();

        assertEquals(2, livro.getQuantidade());
        assertTrue(livro.isDisp());
    }

    @Test
    public void testEmprestarAteFicarIndisponivel() {
        Livro livroUnico = new Livro(4, "Livro Único", "Autor", 2020, 1);

        livroUnico.emprestarUnidade();

        assertEquals(0, livroUnico.getQuantidade());
        assertFalse(livroUnico.isDisp());
    }

    @Test
    public void testNaoEmprestarQuandoQuantidadeForZero() {
        Livro livroSemEstoque = new Livro(5, "Livro Sem Estoque", "Autor", 2020, 0);

        livroSemEstoque.emprestarUnidade();

        assertEquals(0, livroSemEstoque.getQuantidade());
        assertFalse(livroSemEstoque.isDisp());
    }

    @Test
    public void testDevolverUnidade() {
        Livro livroSemEstoque = new Livro(6, "Livro Devolvido", "Autor", 2020, 0);

        livroSemEstoque.devolverUnidade();

        assertEquals(1, livroSemEstoque.getQuantidade());
        assertTrue(livroSemEstoque.isDisp());
    }

    @Test
    public void testSetQuantidade() {
        livro.setQuantidade(10);

        assertEquals(10, livro.getQuantidade());
        assertTrue(livro.isDisp());
    }

    @Test
    public void testSetQuantidadeZeroDeixaIndisponivel() {
        livro.setQuantidade(0);

        assertEquals(0, livro.getQuantidade());
        assertFalse(livro.isDisp());
    }

    @Test
    public void testNaoPermitirQuantidadeNegativaNoConstrutor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Livro(7, "Livro Inválido", "Autor", 2020, -1);
        });
    }

    @Test
    public void testNaoPermitirQuantidadeNegativaNoSetQuantidade() {
        assertThrows(IllegalArgumentException.class, () -> {
            livro.setQuantidade(-5);
        });
    }

    @Test
    public void testRemoverEspacosNoTituloEAutor() {
        Livro livroComEspacos = new Livro(8, " Dom Casmurro ", " Machado de Assis ", 1899, 2);

        assertEquals("Dom Casmurro", livroComEspacos.getTitulo());
        assertEquals("Machado de Assis", livroComEspacos.getAutor());
    }

    @Test
    public void testToCsvComQuantidade() {
        String esperado = "1;Dom Casmurro;Machado de Assis;1899;3";

        assertEquals(esperado, livro.toCsv(), "O método toCsv() deve salvar a quantidade, não true/false.");
    }

    @Test
    public void testSetDispFalseCompatibilidade() {
        livro.setDisp(false);

        assertFalse(livro.isDisp());
        assertEquals(0, livro.getQuantidade());
    }

    @Test
    public void testSetDispTrueCompatibilidade() {
        Livro livroSemEstoque = new Livro(9, "Livro Teste", "Autor", 2020, 0);

        livroSemEstoque.setDisp(true);

        assertTrue(livroSemEstoque.isDisp());
        assertEquals(1, livroSemEstoque.getQuantidade());
    }
}