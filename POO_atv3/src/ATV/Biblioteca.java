package ATV;

import ATV.Exception.ArquivoInvalidoException;
import ATV.Exception.LivroIndisponivelException;
import ATV.Exception.LivroNaoEncontradoException;
import ATV.Exception.UsuarioNaoEncontradoException;

import java.util.ArrayList;
import java.util.List;

public class Biblioteca {
    private List<Livro> livros;
    private List<Usuario> usuarios;
    private List<Emprestimo> emprestimos;
    private Persistencia persistencia;

    public Biblioteca() {
        this(new Persistencia());
    }

    public Biblioteca(Persistencia persistencia) {
        this.livros = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.emprestimos = new ArrayList<>();
        this.persistencia = persistencia;
    }

    public void cadastrarLivro(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo.");
        }

        for (Livro l : livros) {
            if (l.getCodigo() == livro.getCodigo()) {
                System.out.println("Código de livro já existe.");
                return;
            }
        }

        livros.add(livro);
        System.out.println("Livro cadastrado: " + livro);
    }

    public void cadastrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }

        for (Usuario u : usuarios) {
            if (u.getId() == usuario.getId()) {
                System.out.println("ID de usuário já existe.");
                return;
            }
        }

        usuarios.add(usuario);
        System.out.println("Usuário cadastrado: " + usuario);
    }

    public Livro buscarLivroPorCodigo(int codigo) throws LivroNaoEncontradoException {
        for (Livro livro : livros) {
            if (livro.getCodigo() == codigo) {
                return livro;
            }
        }

        throw new LivroNaoEncontradoException("Livro não encontrado com código: " + codigo);
    }

    public Livro buscarLivroPorCódigo(int codigo) throws LivroNaoEncontradoException {
        return buscarLivroPorCodigo(codigo);
    }

    public Usuario buscarUsuarioPorId(int id) throws UsuarioNaoEncontradoException {
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) {
                return usuario;
            }
        }

        throw new UsuarioNaoEncontradoException("Usuário não encontrado com ID: " + id);
    }

    public Usuario buscarUsuarioPorID(int id) throws UsuarioNaoEncontradoException {
        return buscarUsuarioPorId(id);
    }

    public Usuario buscarUsuario(int id) throws UsuarioNaoEncontradoException {
        return buscarUsuarioPorId(id);
    }

    public void buscarLivroPorTitulo(String titulo) {
        String busca = titulo == null ? "" : titulo.trim().toLowerCase();

        if (busca.isBlank()) {
            System.out.println("Digite um título para buscar.");
            return;
        }

        boolean encontrado = false;

        for (Livro livro : livros) {
            if (livro.getTitulo().toLowerCase().contains(busca)) {
                System.out.println(livro);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("Nenhum livro encontrado com esse título.");
        }
    }

    public void buscarLivroPorAutor(String autor) {
        String busca = autor == null ? "" : autor.trim().toLowerCase();

        if (busca.isBlank()) {
            System.out.println("Digite um autor para buscar.");
            return;
        }

        boolean encontrado = false;

        for (Livro livro : livros) {
            if (livro.getAutor().toLowerCase().contains(busca)) {
                System.out.println(livro);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("Nenhum livro encontrado para este autor.");
        }
    }

    public void listarLivrosCadastrados() {
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        System.out.println("=== LIVROS CADASTRADOS ===");

        for (Livro livro : livros) {
            System.out.println(livro);
        }
    }

    public void listarTodos() {
        listarLivrosCadastrados();
    }

    public void listarLivros() {
        listarLivrosCadastrados();
    }

    public void realizarEmprestimo(int codigoLivro, int idUsuario)
            throws LivroNaoEncontradoException, UsuarioNaoEncontradoException, LivroIndisponivelException {

        Livro livro = buscarLivroPorCodigo(codigoLivro);
        Usuario usuario = buscarUsuarioPorId(idUsuario);

        if (!livro.isDisp()) {
            throw new LivroIndisponivelException("Livro indisponível para empréstimo.");
        }

        livro.emprestarUnidade();

        Emprestimo emprestimo = new Emprestimo(livro, usuario);
        emprestimos.add(emprestimo);

        System.out.println("Empréstimo realizado: " + emprestimo);
    }

    public void realizarDevolucao(int codigoLivro) throws LivroNaoEncontradoException {
        Livro livro = buscarLivroPorCodigo(codigoLivro);

        Emprestimo emprestimoEncontrado = null;

        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getLivro().getCodigo() == codigoLivro) {
                emprestimoEncontrado = emprestimo;
                break;
            }
        }

        if (emprestimoEncontrado == null) {
            throw new IllegalArgumentException("Não existe empréstimo aberto para este livro.");
        }

        emprestimos.remove(emprestimoEncontrado);
        livro.devolverUnidade();

        System.out.println("Devolução realizada do livro: " + livro.getTitulo());
    }

    public void carregarDados() throws ArquivoInvalidoException {
        Persistencia.DadosBiblioteca dados = persistencia.carregarDados();

        this.livros = dados.getLivros();
        this.usuarios = dados.getUsuarios();
        this.emprestimos = dados.getEmprestimos();
    }

    public void gravarDados() throws ArquivoInvalidoException {
        persistencia.gravarDados(livros, usuarios, emprestimos);
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }
}