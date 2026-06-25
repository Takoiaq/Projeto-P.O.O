package test;

import ATV.Usuario;
import ATV.Estudante;
import ATV.Professor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {
    private Usuario usuarioComum;
    private Usuario estudante;
    private Usuario professor;

    @BeforeEach
    public void setUp() {
        usuarioComum = new Usuario(10, "Ana Clara", "ana@ufc.br");
        estudante = new Estudante(20, "Luis Felipe", "luis@ufc.br");
        professor = new Professor(30, "Robertty", "robertty@ufc.br");
    }

    @Test
    public void testCriacaoUsuario() {
        assertEquals(10, usuarioComum.getId());
        assertEquals("Ana Clara", usuarioComum.getNome());
        assertEquals("ana@ufc.br", usuarioComum.getEmail());
    }

    @Test
    public void testPrazosEmprestimoPolimorfismo() {
        assertEquals(7, usuarioComum.getPrazoEmprestimo());
        assertEquals(7, estudante.getPrazoEmprestimo());
        assertEquals(14, professor.getPrazoEmprestimo());
    }

    @Test
    public void testToCsv() {
        assertEquals("USUARIO;10;Ana Clara;ana@ufc.br", usuarioComum.toCsv());
        assertEquals("ESTUDANTE;20;Luis Felipe;luis@ufc.br", estudante.toCsv());
        assertEquals("PROFESSOR;30;Robertty;robertty@ufc.br", professor.toCsv());
    }
}