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
        Livro livro = new Livro(1, "Livro Teste", "Autor Teste", 2026, true);

        biblioteca.cadastrarLivro(livro);

        Livro livroEncontrado = biblioteca.buscarLivroPorCodigo(1);

        assertNotNull(livroEncontrado);
        assertEquals("Livro Teste", livroEncontrado.getTitulo());
        assertEquals("Autor Teste", livroEncontrado.getAutor());
        assertEquals(2026, livroEncontrado.getAnop());
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
        Usuario professor = new Bibliotecario(2, "Docente", "docente@email.com");

        assertEquals(7, estudante.getPrazoEmprestimo());
        assertEquals(14, professor.getPrazoEmprestimo());
    }

    @Test
    void deveRealizarEmprestimo() throws Exception {
        Livro livro = new Livro(2, "Java Avançado", "Expert", 2026, true);
        Usuario usuario = new Estudante(20, "Developer", "dev@email.com");

        biblioteca.cadastrarLivro(livro);
        biblioteca.cadastrarUsuario(usuario);

        biblioteca.realizarEmprestimo(2, 20);

        assertFalse(biblioteca.buscarLivroPorCodigo(2).isDisp());
    }

    @Test
    void deveRealizarDevolucao() throws Exception {
        Livro livro = new Livro(3, "Estruturas de Dados", "Professor", 2025, true);
        Usuario usuario = new Estudante(30, "Aluno", "aluno@email.com");

        biblioteca.cadastrarLivro(livro);
        biblioteca.cadastrarUsuario(usuario);
        biblioteca.realizarEmprestimo(3, 30);

        biblioteca.realizarDevolucao(3);

        assertTrue(biblioteca.buscarLivroPorCodigo(3).isDisp());
    }

    @Test
    void deveEscreverArquivos() throws Exception {
        Livro livro = new Livro(55, "Persistência em Java", "Uncle Bob", 2020, true);
        Usuario usuario = new Estudante(66, "Felipe", "felipe@email.com");

        biblioteca.cadastrarLivro(livro);
        biblioteca.cadastrarUsuario(usuario);
        biblioteca.realizarEmprestimo(55, 66);

        biblioteca.gravarDados();

        assertTrue(Files.exists(arquivoLivros));
        assertTrue(Files.exists(arquivoUsuarios));
        assertTrue(Files.exists(arquivoEmprestimos));

        assertTrue(Files.readString(arquivoLivros).contains("55;Persistência em Java;Uncle Bob;2020;false"));
        assertTrue(Files.readString(arquivoUsuarios).contains("ESTUDANTE;66;Felipe;felipe@email.com"));
        assertTrue(Files.readString(arquivoEmprestimos).contains("55;66;"));
    }

    @Test
    void deveLerArquivos() throws Exception {
        Files.writeString(arquivoLivros, "77;Livro Carregado;Autor Arquivo;2019;false\n");
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

        biblioteca.realizarDevolucao(77);

        assertTrue(livroCarregado.isDisp());
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
            biblioteca.realizarDevolucao(9999);
        });
    }

    @Test
    void deveLancarExcecaoQuandoLivroEstiverIndisponivel() throws Exception {
        Livro livro = new Livro(4, "Dom Casmurro", "Machado de Assis", 1899, true);
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
        Files.writeString(arquivoLivros, "TEXTO_CORROMPIDO;Titulo;Autor;AnoInvalido;true\n");

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
    void deveRemoverEspacosNoInicioEFimDosTextos() {
        Usuario usuario = new Estudante(1, " Roberty ", " roberty@email.com ");
        Livro livro = new Livro(2, " Livro Java ", " Autor Teste ", 2026, true);

        assertEquals("Roberty", usuario.getNome());
        assertEquals("roberty@email.com", usuario.getEmail());
        assertEquals("Livro Java", livro.getTitulo());
        assertEquals("Autor Teste", livro.getAutor());
    }

    @Test
    void deveCarregarArquivosMesmoComEspacosExtras() throws Exception {
        Files.writeString(arquivoLivros, " 77 ; Livro Carregado ; Autor Arquivo ; 2019 ; false \n");
        Files.writeString(arquivoUsuarios, " PROFESSOR ; 88 ; Usuario Arquivo ; arquivo@email.com \n");
        Files.writeString(arquivoEmprestimos, " 77 ; 88 ; 2026-06-01 ; 2026-06-15 \n");

        biblioteca.carregarDados();

        Livro livroCarregado = biblioteca.buscarLivroPorCodigo(77);
        Usuario usuarioCarregado = biblioteca.buscarUsuarioPorId(88);

        assertEquals("Livro Carregado", livroCarregado.getTitulo());
        assertEquals("Usuario Arquivo", usuarioCarregado.getNome());
        assertTrue(usuarioCarregado instanceof Bibliotecario);
        assertFalse(livroCarregado.isDisp());
    }
}