package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ATV.Biblioteca;
import ATV.Estudante;
import ATV.Livro;
import ATV.Persistencia;
import ATV.Bibliotecario;
import ATV.Usuario;

import ATV.Exception.ArquivoInvalidoException;
import ATV.Exception.LivroIndisponivelException;
import ATV.Exception.LivroNaoEncontradoException;
import ATV.Exception.UsuarioNaoEncontradoException;

public class BibliotecaTest {

    @TempDir
    Path pastaTemporaria;

    private Biblioteca biblioteca;
    private Path arquivoLivros;
    private Path arquivoUsuarios;
    private Path arquivoEmprestimos;

    @BeforeEach
    void setUp() {
        arquivoLivros = pastaTemporaria.resolve("livros.txt");
        arquivoUsuarios = pastaTemporaria.resolve("usuarios.txt");
        arquivoEmprestimos = pastaTemporaria.resolve("emprestimos.txt");

        Persistencia persistencia = new Persistencia(
                arquivoLivros.toString(),
                arquivoUsuarios.toString(),
                arquivoEmprestimos.toString()
        );

        biblioteca = new Biblioteca(persistencia);
    }

    @Test
    void deveCadastrarLivro() throws LivroNaoEncontradoException {
        Livro livro = new Livro(1, "Livro Teste", "Autor Teste", 2026, 3);

        biblioteca.cadastrarLivro(livro);

        Livro livroEncontrado = biblioteca.buscarLivroPorCodigo(1);

        assertNotNull(livroEncontrado);
        assertEquals("Livro Teste", livroEncontrado.getTitulo());
        assertEquals("Autor Teste", livroEncontrado.getAutor());
        assertEquals(2026, livroEncontrado.getAnop());
        assertEquals(3, livroEncontrado.getQuantidade());
        assertTrue(livroEncontrado.isDisp());
    }

    @Test
    void deveCadastrarUsuario() throws UsuarioNaoEncontradoException {
        Usuario usuario = new Estudante(10, "Usuario Teste", "teste@email.com");

        biblioteca.cadastrarUsuario(usuario);

        Usuario usuarioEncontrado = biblioteca.buscarUsuarioPorId(10);

        assertNotNull(usuarioEncontrado);
        assertEquals("Usuario Teste", usuarioEncontrado.getNome());
        assertEquals("teste@email.com", usuarioEncontrado.getEmail());
    }

    @Test
    void deveValidarPrazoDiferenciadoPolimorfismo() {
        Usuario estudante = new Estudante(1, "Aluno", "aluno@email.com");
        Usuario bibliotecario = new Bibliotecario(2, "Bibliotecário", "bibliotecario@email.com");

        assertEquals(7, estudante.getPrazoEmprestimo());
        assertEquals(14, bibliotecario.getPrazoEmprestimo());
    }

    @Test
    void deveRealizarEmprestimo() throws Exception {
        Livro livro = new Livro(2, "Java Avançado", "Expert", 2026, 1);
        Usuario usuario = new Estudante(20, "Developer", "dev@email.com");

        biblioteca.cadastrarLivro(livro);
        biblioteca.cadastrarUsuario(usuario);

        biblioteca.realizarEmprestimo(2, 20);

        Livro livroEncontrado = biblioteca.buscarLivroPorCodigo(2);

        assertFalse(livroEncontrado.isDisp());
        assertEquals(0, livroEncontrado.getQuantidade());
    }

    @Test
    void deveRealizarDevolucao() throws Exception {
        Livro livro = new Livro(3, "Estruturas de Dados", "Professor", 2025, 1);
        Usuario usuario = new Estudante(30, "Aluno", "aluno@email.com");

        biblioteca.cadastrarLivro(livro);
        biblioteca.cadastrarUsuario(usuario);
        biblioteca.realizarEmprestimo(3, 30);

        biblioteca.realizarDevolucao(3, 30);

        Livro livroEncontrado = biblioteca.buscarLivroPorCodigo(3);

        assertTrue(livroEncontrado.isDisp());
        assertEquals(1, livroEncontrado.getQuantidade());
    }

    @Test
    void deveEscreverArquivos() throws Exception {
        Livro livro = new Livro(55, "Persistência em Java", "Uncle Bob", 2020, 1);
        Usuario usuario = new Estudante(66, "Felipe", "felipe@email.com");

        biblioteca.cadastrarLivro(livro);
        biblioteca.cadastrarUsuario(usuario);
        biblioteca.realizarEmprestimo(55, 66);

        biblioteca.gravarDados();

        assertTrue(Files.exists(arquivoLivros));
        assertTrue(Files.exists(arquivoUsuarios));
        assertTrue(Files.exists(arquivoEmprestimos));

        assertTrue(Files.readString(arquivoLivros).contains("55;Persistência em Java;Uncle Bob;2020;0"));
        assertTrue(Files.readString(arquivoUsuarios).contains("ESTUDANTE;66;Felipe;felipe@email.com"));
        assertTrue(Files.readString(arquivoEmprestimos).contains("55;66;"));
    }

    @Test
    void deveLerArquivos() throws Exception {
        Files.writeString(arquivoLivros, "77;Livro Carregado;Autor Arquivo;2019;0\n");
        Files.writeString(arquivoUsuarios, "PROFESSOR;88;Usuario Arquivo;arquivo@email.com\n");
        Files.writeString(arquivoEmprestimos, "77;88;2026-06-01;2026-06-15\n");

        biblioteca.carregarDados();

        Livro livroCarregado = biblioteca.buscarLivroPorCodigo(77);
        Usuario usuarioCarregado = biblioteca.buscarUsuarioPorId(88);

        assertEquals("Livro Carregado", livroCarregado.getTitulo());
        assertEquals("Usuario Arquivo", usuarioCarregado.getNome());
        assertTrue(usuarioCarregado instanceof Bibliotecario);
        assertEquals(14, usuarioCarregado.getPrazoEmprestimo());
        assertFalse(livroCarregado.isDisp());
        assertEquals(0, livroCarregado.getQuantidade());

        biblioteca.realizarDevolucao(77, 88);

        assertTrue(livroCarregado.isDisp());
        assertEquals(1, livroCarregado.getQuantidade());
    }

    @Test
    void deveLerArquivoAntigoComTrueFalse() throws Exception {
        Files.writeString(
                arquivoLivros,
                "90;Livro Antigo Disponivel;Autor A;2020;true\n" +
                "91;Livro Antigo Indisponivel;Autor B;2020;false\n"
        );

        biblioteca.carregarDados();

        Livro livroTrue = biblioteca.buscarLivroPorCodigo(90);
        Livro livroFalse = biblioteca.buscarLivroPorCodigo(91);

        assertEquals(1, livroTrue.getQuantidade());
        assertTrue(livroTrue.isDisp());

        assertEquals(0, livroFalse.getQuantidade());
        assertFalse(livroFalse.isDisp());
    }

    @Test
    void deveLancarExcecaoQuandoLivroNaoForEncontrado() {
        assertThrows(LivroNaoEncontradoException.class, () -> {
            biblioteca.buscarLivroPorCodigo(9999);
        });
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForEncontrado() {
        assertThrows(UsuarioNaoEncontradoException.class, () -> {
            biblioteca.buscarUsuarioPorId(8888);
        });
    }

    @Test
    void deveLancarExcecaoQuandoDevolverLivroNaoEncontrado() {
        assertThrows(LivroNaoEncontradoException.class, () -> {
            biblioteca.realizarDevolucao(9999, 1);
        });
    }

    @Test
    void deveLancarExcecaoQuandoLivroEstiverIndisponivel() throws Exception {
        Livro livro = new Livro(4, "Dom Casmurro", "Machado de Assis", 1899, 1);
        Usuario usuario1 = new Estudante(1, "Machado", "machado@email.com");
        Usuario usuario2 = new Bibliotecario(2, "Alencar", "alencar@email.com");

        biblioteca.cadastrarLivro(livro);
        biblioteca.cadastrarUsuario(usuario1);
        biblioteca.cadastrarUsuario(usuario2);

        biblioteca.realizarEmprestimo(4, 1);

        assertThrows(LivroIndisponivelException.class, () -> {
            biblioteca.realizarEmprestimo(4, 2);
        });
    }

    @Test
    void deveLancarExcecaoQuandoArquivoForInvalido() throws IOException {
        Files.writeString(arquivoLivros, "TEXTO_CORROMPIDO;Titulo;Autor;AnoInvalido;1\n");

        assertThrows(ArquivoInvalidoException.class, () -> {
            biblioteca.carregarDados();
        });
    }

    @Test
    void deveAvisarQuandoIdDeUsuarioJaExistir() throws UsuarioNaoEncontradoException {
        Usuario usuarioOriginal = new Estudante(1, "Roberty", "roberty@email.com");
        Usuario usuarioDuplicado = new Bibliotecario(1, "Outro Nome", "outro@email.com");

        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        PrintStream saidaOriginal = System.out;

        try {
            System.setOut(new PrintStream(saida));

            biblioteca.cadastrarUsuario(usuarioOriginal);
            biblioteca.cadastrarUsuario(usuarioDuplicado);

        } finally {
            System.setOut(saidaOriginal);
        }

        String mensagem = saida.toString();

        assertTrue(mensagem.contains("ID") && mensagem.contains("existe"));
        assertEquals("Roberty", biblioteca.buscarUsuarioPorId(1).getNome());
    }

    @Test
    void deveAvisarQuandoCodigoDeLivroJaExistir() throws LivroNaoEncontradoException {
        Livro livroOriginal = new Livro(1, "Livro Original", "Autor A", 2020, 2);
        Livro livroDuplicado = new Livro(1, "Livro Duplicado", "Autor B", 2021, 5);

        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        PrintStream saidaOriginal = System.out;

        try {
            System.setOut(new PrintStream(saida));

            biblioteca.cadastrarLivro(livroOriginal);
            biblioteca.cadastrarLivro(livroDuplicado);

        } finally {
            System.setOut(saidaOriginal);
        }

        String mensagem = saida.toString();

        assertTrue(mensagem.contains("codigo") || mensagem.contains("codigo de livro") || mensagem.contains("existe"));
        assertEquals("Livro Original", biblioteca.buscarLivroPorCodigo(1).getTitulo());
        assertEquals(2, biblioteca.buscarLivroPorCodigo(1).getQuantidade());
    }

    @Test
    void deveRemoverEspacosNoInicioEFimDosTextos() {
        Usuario usuario = new Estudante(1, " Roberty ", " roberty@email.com ");
        Livro livro = new Livro(2, " Livro Java ", " Autor Teste ", 2026, 3);

        assertEquals("Roberty", usuario.getNome());
        assertEquals("roberty@email.com", usuario.getEmail());
        assertEquals("Livro Java", livro.getTitulo());
        assertEquals("Autor Teste", livro.getAutor());
    }

    @Test
    void deveCarregarArquivosMesmoComEspacosExtras() throws Exception {
        Files.writeString(arquivoLivros, " 77 ; Livro Carregado ; Autor Arquivo ; 2019 ; 0 \n");
        Files.writeString(arquivoUsuarios, " PROFESSOR ; 88 ; Usuario Arquivo ; arquivo@email.com \n");
        Files.writeString(arquivoEmprestimos, " 77 ; 88 ; 2026-06-01 ; 2026-06-15 \n");

        biblioteca.carregarDados();

        Livro livroCarregado = biblioteca.buscarLivroPorCodigo(77);
        Usuario usuarioCarregado = biblioteca.buscarUsuarioPorId(88);

        assertEquals("Livro Carregado", livroCarregado.getTitulo());
        assertEquals("Usuario Arquivo", usuarioCarregado.getNome());
        assertTrue(usuarioCarregado instanceof Bibliotecario);
        assertFalse(livroCarregado.isDisp());
        assertEquals(0, livroCarregado.getQuantidade());
    }

    @Test
    void deveAdicionarQuantidadeLivro() throws Exception {
        Livro livro = new Livro(10, "Clean Code", "Robert Martin", 2008, 2);

        biblioteca.cadastrarLivro(livro);
        biblioteca.adicionarQuantidadeLivro(10, 3);

        Livro livroEncontrado = biblioteca.buscarLivroPorCodigo(10);

        assertEquals(5, livroEncontrado.getQuantidade());
        assertTrue(livroEncontrado.isDisp());
    }

    @Test
    void deveRemoverQuantidadeLivro() throws Exception {
        Livro livro = new Livro(11, "Java Básico", "Autor Teste", 2020, 5);

        biblioteca.cadastrarLivro(livro);
        biblioteca.removerQuantidadeLivro(11, 2);

        Livro livroEncontrado = biblioteca.buscarLivroPorCodigo(11);

        assertEquals(3, livroEncontrado.getQuantidade());
        assertTrue(livroEncontrado.isDisp());
    }

    @Test
    void naoDeveRemoverQuantidadeMaiorQueDisponivel() throws Exception {
        Livro livro = new Livro(12, "Banco de Dados", "Autor Teste", 2021, 2);

        biblioteca.cadastrarLivro(livro);
        biblioteca.removerQuantidadeLivro(12, 5);

        Livro livroEncontrado = biblioteca.buscarLivroPorCodigo(12);

        assertEquals(2, livroEncontrado.getQuantidade());
        assertTrue(livroEncontrado.isDisp());
    }

    @Test
    void deveSalvarECarregarQuantidadeCorretamente() throws Exception {
        Livro livro = new Livro(13, "Persistência", "Autor Arquivo", 2024, 6);

        biblioteca.cadastrarLivro(livro);
        biblioteca.gravarDados();

        Biblioteca outraBiblioteca = new Biblioteca(new Persistencia(
                arquivoLivros.toString(),
                arquivoUsuarios.toString(),
                arquivoEmprestimos.toString()
        ));

        outraBiblioteca.carregarDados();

        Livro livroCarregado = outraBiblioteca.buscarLivroPorCodigo(13);

        assertEquals(6, livroCarregado.getQuantidade());
        assertTrue(livroCarregado.isDisp());
        assertTrue(Files.readString(arquivoLivros).contains("13;Persistência;Autor Arquivo;2024;6"));
    }

    @Test
    void deveImpedirMesmoUsuarioPegarMesmoLivroDuasVezes() throws Exception {
        Livro livro = new Livro(14, "Redes", "Autor Redes", 2022, 2);
        Usuario usuario = new Estudante(140, "Aluno Redes", "redes@email.com");

        biblioteca.cadastrarLivro(livro);
        biblioteca.cadastrarUsuario(usuario);

        biblioteca.realizarEmprestimo(14, 140);

        assertThrows(LivroIndisponivelException.class, () -> {
            biblioteca.realizarEmprestimo(14, 140);
        });

        assertEquals(1, biblioteca.buscarLivroPorCodigo(14).getQuantidade());
    }

    @Test
    void deveDevolverLivroDoUsuarioCorretoQuandoMesmoLivroTemMaisDeUmEmprestimo() throws Exception {
        Livro livro = new Livro(15, "POO", "Autor POO", 2023, 2);
        Usuario usuario1 = new Estudante(151, "Aluno Um", "aluno1@email.com");
        Usuario usuario2 = new Estudante(152, "Aluno Dois", "aluno2@email.com");

        biblioteca.cadastrarLivro(livro);
        biblioteca.cadastrarUsuario(usuario1);
        biblioteca.cadastrarUsuario(usuario2);

        biblioteca.realizarEmprestimo(15, 151);
        biblioteca.realizarEmprestimo(15, 152);

        assertEquals(0, biblioteca.buscarLivroPorCodigo(15).getQuantidade());

        biblioteca.realizarDevolucao(15, 152);

        assertEquals(1, biblioteca.buscarLivroPorCodigo(15).getQuantidade());

        assertThrows(LivroIndisponivelException.class, () -> {
            biblioteca.realizarEmprestimo(15, 151);
        });

        biblioteca.realizarEmprestimo(15, 152);

        assertEquals(0, biblioteca.buscarLivroPorCodigo(15).getQuantidade());
    }

    @Test
    void metodoAntigoNaoDeveDevolverQuandoExistemVariosEmprestimosDoMesmoLivro() throws Exception {
        Livro livro = new Livro(16, "Algoritmos", "Autor Algoritmos", 2020, 2);
        Usuario usuario1 = new Estudante(161, "Aluno A", "alunoa@email.com");
        Usuario usuario2 = new Estudante(162, "Aluno B", "alunob@email.com");

        biblioteca.cadastrarLivro(livro);
        biblioteca.cadastrarUsuario(usuario1);
        biblioteca.cadastrarUsuario(usuario2);

        biblioteca.realizarEmprestimo(16, 161);
        biblioteca.realizarEmprestimo(16, 162);

        biblioteca.realizarDevolucao(16);

        assertEquals(0, biblioteca.buscarLivroPorCodigo(16).getQuantidade());
    }
}