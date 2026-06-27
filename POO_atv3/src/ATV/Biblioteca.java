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
                System.out.println("Codigo de livro já existe.");
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

        throw new LivroNaoEncontradoException("Livro não encontrado com codigo: " + codigo);
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

    public void buscarUsuarioPorNome(String nome) {
        String busca = nome == null ? "" : nome.trim().toLowerCase();

        if (busca.isBlank()) {
            System.out.println("Digite um nome para buscar.");
            return;
        }

        boolean encontrado = false;

        for (Usuario usuario : usuarios) {
            if (usuario.getNome().toLowerCase().contains(busca)) {
                System.out.println(usuario);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("Nenhum usuário encontrado com esse nome.");
        }
    }

    public void buscarLivroPorTitulo(String titulo) {
        String busca = titulo == null ? "" : titulo.trim().toLowerCase();

        if (busca.isBlank()) {
            System.out.println("Digite um titulo para buscar.");
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
            System.out.println("Nenhum livro encontrado com esse titulo.");
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

    public void listarUsuariosCadastrados() {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        System.out.println("=== USUÁRIOS CADASTRADOS ===");

        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }
    }

    public void listarTodos() {
        listarLivrosCadastrados();
    }

    public void listarLivros() {
        listarLivrosCadastrados();
    }

    public void listarLivrosEmprestados() {
        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum livro emprestado.");
            return;
        }

        System.out.println("=== LIVROS EMPRESTADOS ===");

        for (Emprestimo emprestimo : emprestimos) {
            System.out.println(emprestimo);
        }
    }

    public void listarLivrosEmprestadosPorUsuario(int idUsuario) {
        boolean encontrado = false;

        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getUsuario().getId() == idUsuario) {
                System.out.println(emprestimo);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("Nenhum empréstimo encontrado para este usuário.");
        }
    }

    public void listarUsuariosComEmprestimos() {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        System.out.println("=== USUÁRIOS E EMPRÉSTIMOS ===");

        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
            listarLivrosEmprestadosPorUsuario(usuario.getId());
        }
    }

    public void realizarEmprestimo(int codigoLivro, int idUsuario)
            throws LivroNaoEncontradoException, UsuarioNaoEncontradoException, LivroIndisponivelException {

        Livro livro = buscarLivroPorCodigo(codigoLivro);
        Usuario usuario = buscarUsuarioPorId(idUsuario);

        if (buscarEmprestimoAtivo(codigoLivro, idUsuario) != null) {
            throw new LivroIndisponivelException("Este usuário já possui empréstimo ativo deste livro.");
        }

        if (!livro.isDisp()) {
            throw new LivroIndisponivelException("Livro indisponível para empréstimo.");
        }

        livro.emprestarUnidade();

        Emprestimo emprestimo = new Emprestimo(livro, usuario);
        emprestimos.add(emprestimo);

        System.out.println("Empréstimo realizado: " + emprestimo);
    }

    public void realizarDevolucao(int codigoLivro, int idUsuario)
            throws LivroNaoEncontradoException, UsuarioNaoEncontradoException {

        if (codigoLivro < 0) {
            throw new LivroNaoEncontradoException("Codigo do livro não pode ser negativo.");
        }

        if (idUsuario < 0) {
            throw new UsuarioNaoEncontradoException("ID de usuário não pode ser negativo.");
        }

        Livro livro = buscarLivroPorCodigo(codigoLivro);
        Usuario usuario = buscarUsuarioPorId(idUsuario);
        Emprestimo emprestimoEncontrado = buscarEmprestimoAtivo(codigoLivro, idUsuario);

        if (emprestimoEncontrado == null) {
            System.out.println(
                    "Aviso: o usuário '" + usuario.getNome() +
                    "' não possui empréstimo ativo do livro '" + livro.getTitulo() + "'."
            );
            return;
        }

        emprestimos.remove(emprestimoEncontrado);
        livro.devolverUnidade();

        System.out.println("Devolução realizada com sucesso!");
        System.out.println("Livro: " + livro.getTitulo());
        System.out.println("Usuário: " + usuario.getNome());
        System.out.println("Quantidade atual do livro: " + livro.getQuantidade());
    }

    public void realizarDevolucao(int codigoLivro) throws LivroNaoEncontradoException {
        Livro livro = buscarLivroPorCodigo(codigoLivro);

        Emprestimo emprestimoEncontrado = null;
        int quantidadeEmprestimosDoLivro = 0;

        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getLivro().getCodigo() == codigoLivro) {
                emprestimoEncontrado = emprestimo;
                quantidadeEmprestimosDoLivro++;
            }
        }

        if (quantidadeEmprestimosDoLivro == 0) {
            System.out.println("Aviso: O livro '" + livro.getTitulo() + "' não possui empréstimo ativo no sistema.");
            return;
        }

        if (quantidadeEmprestimosDoLivro > 1) {
            System.out.println("[Erro] Existe mais de um empréstimo ativo deste livro.");
            System.out.println("Informe também o ID do usuário para realizar a devolução correta.");
            return;
        }

        emprestimos.remove(emprestimoEncontrado);
        livro.devolverUnidade();

        System.out.println("Devolução realizada do livro: " + livro.getTitulo());
    }

    private Emprestimo buscarEmprestimoAtivo(int codigoLivro, int idUsuario) {
        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getLivro().getCodigo() == codigoLivro &&
                    emprestimo.getUsuario().getId() == idUsuario) {
                return emprestimo;
            }
        }

        return null;
    }

    public void adicionarQuantidadeLivro(int codigoLivro, int quantidade)
            throws LivroNaoEncontradoException {

        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa.");
        }

        Livro livro = buscarLivroPorCodigo(codigoLivro);
        livro.setQuantidade(livro.getQuantidade() + quantidade);

        System.out.println("Quantidade adicionada com sucesso.");
        System.out.println(livro);
    }

    public void removerQuantidadeLivro(int codigoLivro, int quantidade)
            throws LivroNaoEncontradoException {

        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa.");
        }

        Livro livro = buscarLivroPorCodigo(codigoLivro);

        if (quantidade > livro.getQuantidade()) {
            System.out.println("Não é possível remover quantidade maior que a disponível.");
            return;
        }

        livro.setQuantidade(livro.getQuantidade() - quantidade);

        System.out.println("Quantidade removida com sucesso.");
        System.out.println(livro);
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