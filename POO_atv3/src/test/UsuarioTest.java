package test;

import ATV.Usuario;
import ATV.Estudante;
import ATV.Bibliotecario;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    @Test
    public void testPolimorfismoEAtributosComHeranca() {
        Usuario usuarioEstudante = new Estudante(10, "Ana Clara", "ana@ufc.br");

        assertEquals(10, usuarioEstudante.getId());
        assertEquals("Ana Clara", usuarioEstudante.getNome());
        assertEquals("ana@ufc.br", usuarioEstudante.getEmail());
        assertEquals(7, usuarioEstudante.getPrazoEmprestimo());
    }

    @Test
    public void testAlteracaoDeDadosCadastrais() {
        Usuario usuarioBibliotecario = new Bibliotecario(30, "Robertty Costa", "robertty@ufc.br");

        usuarioBibliotecario.setNome("Robertty C. Silva");
        usuarioBibliotecario.setEmail("robertty.costa@ufc.br");

        assertEquals("Robertty C. Silva", usuarioBibliotecario.getNome());
        assertEquals("robertty.costa@ufc.br", usuarioBibliotecario.getEmail());
    }

    @Test
    public void testToStringFormatado() {
        Usuario usuarioEstudante = new Estudante(10, "Ana Clara", "ana@ufc.br");
        String esperado = "ID: 10 | Nome: Ana Clara | Email: ana@ufc.br";

        assertEquals(esperado, usuarioEstudante.toString());
    }
}