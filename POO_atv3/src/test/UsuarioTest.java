package test;

import ATV.Usuario;
import ATV.Estudante;
import ATV.Bibliotecario;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    @Test
    public void testEstudanteComHerancaEPolimorfismo() {
        Usuario usuarioEstudante = new Estudante(10, "Ana Clara", "ana@ufc.br");

        assertEquals(10, usuarioEstudante.getId());
        assertEquals("Ana Clara", usuarioEstudante.getNome());
        assertEquals("ana@ufc.br", usuarioEstudante.getEmail());
        assertEquals(7, usuarioEstudante.getPrazoEmprestimo());
        assertTrue(usuarioEstudante instanceof Estudante);
    }

    @Test
    public void testBibliotecarioComHerancaEPolimorfismo() {
        Usuario usuarioBibliotecario = new Bibliotecario(30, "Robertty Costa", "robertty@ufc.br");

        assertEquals(30, usuarioBibliotecario.getId());
        assertEquals("Robertty Costa", usuarioBibliotecario.getNome());
        assertEquals("robertty@ufc.br", usuarioBibliotecario.getEmail());
        assertEquals(14, usuarioBibliotecario.getPrazoEmprestimo());
        assertTrue(usuarioBibliotecario instanceof Bibliotecario);
    }

    @Test
    public void testRemoverEspacosNoInicioEFimDosDados() {
        Usuario usuarioEstudante = new Estudante(10, " Ana Clara ", " ana@ufc.br ");

        assertEquals("Ana Clara", usuarioEstudante.getNome());
        assertEquals("ana@ufc.br", usuarioEstudante.getEmail());
    }

    @Test
    public void testToCsvEstudante() {
        Usuario usuarioEstudante = new Estudante(10, "Ana Clara", "ana@ufc.br");

        String esperado = "ESTUDANTE;10;Ana Clara;ana@ufc.br";

        assertEquals(esperado, usuarioEstudante.toCsv());
    }

    @Test
    public void testToCsvBibliotecario() {
        Usuario usuarioBibliotecario = new Bibliotecario(30, "Robertty Costa", "robertty@ufc.br");

        String esperado = "BIBLIOTECARIO;30;Robertty Costa;robertty@ufc.br";

        assertEquals(esperado, usuarioBibliotecario.toCsv());
    }

    @Test
    public void testToStringEstudanteContemDadosPrincipais() {
        Usuario usuarioEstudante = new Estudante(10, "Ana Clara", "ana@ufc.br");

        String texto = usuarioEstudante.toString();

        assertTrue(texto.contains("Estudante"));
        assertTrue(texto.contains("10"));
        assertTrue(texto.contains("Ana Clara"));
        assertTrue(texto.contains("ana@ufc.br"));
    }

    @Test
    public void testToStringBibliotecarioContemDadosPrincipais() {
        Usuario usuarioBibliotecario = new Bibliotecario(30, "Robertty Costa", "robertty@ufc.br");

        String texto = usuarioBibliotecario.toString();

        assertTrue(texto.contains("Bibliotecário"));
        assertTrue(texto.contains("30"));
        assertTrue(texto.contains("Robertty Costa"));
        assertTrue(texto.contains("robertty@ufc.br"));
    }
}