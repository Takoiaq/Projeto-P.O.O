package test;

import ATV.Emprestimo;
import ATV.Livro;
import ATV.Usuario;
import ATV.Estudante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class EmprestimoTest {
    private Livro livro;
    private Usuario usuario;
    private Emprestimo emprestimo;
    private LocalDate dataEmp;
    private LocalDate dataPrev;

    @BeforeEach
    public void setUp() {
        livro = new Livro(1, "Dom Casmurro", "Machado de Assis", 1899, true);
        usuario = new Estudante(1, "João Souza", "joao@ufc.br");
        dataEmp = LocalDate.now();
        dataPrev = LocalDate.now().plusDays(7);
        emprestimo = new Emprestimo(livro, usuario, dataEmp, dataPrev);
    }

    @Test
    public void testCriacaoEmprestimo() {
        assertNotNull(emprestimo, "O empréstimo não deveria ser nulo.");
        assertEquals(livro, emprestimo.getLivro(), "O livro retornado deve ser o mesmo do construtor.");
        assertEquals(usuario, emprestimo.getUsuario(), "O usuário retornado deve ser o mesmo do construtor.");
        assertEquals(dataEmp, emprestimo.getDataEmp(), "A data de empréstimo deve ser igual à informada.");
        assertEquals(dataPrev, emprestimo.getDatadev(), "A data de devolução deve ser igual à informada.");
    }

    @Test
    public void testToCsv() {
        String esperado = livro.getCodigo() + ";" + usuario.getId() + ";" + dataEmp + ";" + dataPrev;
        assertEquals(esperado, emprestimo.toCsv(), "O método toCsv() deve gerar a linha formatada corretamente.");
    }
}