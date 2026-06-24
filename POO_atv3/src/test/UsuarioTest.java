package test;

import ATV.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {
    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        // Construtor real: id, nome, email
        usuario = new Usuario(10, "Ana Clara", "ana@ufc.br");
    }

    @Test
    public void testCriacaoUsuario() {
        assertEquals(10, usuario.getId());
        assertEquals("Ana Clara", usuario.getNome());
        assertEquals("ana@ufc.br", usuario.getEmail());
    }

    @Test
    public void testConvert() {
        String esperado = "10;Ana Clara;ana@ufc.br";
        assertEquals(esperado, usuario.convert(), "O método convert() deve gerar a string de persistência corretamente.");
    }
}