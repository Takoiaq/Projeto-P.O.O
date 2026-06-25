package test;

import ATV.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {
    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        usuario = new Usuario(10, "Ana Clara", "ana@ufc.br");
    }

    @Test
    public void testCriacaoUsuario() {
        assertEquals(10, usuario.getId());
        assertEquals("Ana Clara", usuario.getNome());
        assertEquals("ana@ufc.br", usuario.getEmail());
    }

    @Test
    public void testToCsv() {
        String esperado = "10;Ana Clara;ana@ufc.br";
        assertEquals(esperado, usuario.toCsv(), "O método toCsv() deve gerar a string de persistência corretamente.");
    }
}