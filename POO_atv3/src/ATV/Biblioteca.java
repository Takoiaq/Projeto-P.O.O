package ATV;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ATV.Exception.ArquivoInvalidoException;
import ATV.Exception.LivroIndisponivelException;
import ATV.Exception.LivroNaoEncontradoException;
import ATV.Exception.UsuarioNaoEncontradoException;

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

    public void gravarDados() throws ArquivoInvalidoException {
        persistencia.gravarDados(livros, usuarios, emprestimos);
        System.out.println("Dados salvos nos arquivos com sucesso!");
    }

    public void carregarDados() throws ArquivoInvalidoException {
        Persistencia.DadosBiblioteca dados = persistencia.carregarDados();
        livros = dados.getLivros();
        usuarios = dados.getUsuarios();
        emprestimos = dados.getEmprestimos();
    }

    public void cadastrarLivro(Livro livro) {
        try {
            buscarLivroPorCodigo(livro.getCodigo());
            System.out.println("Erro: Já existe um livro cadastrado com este código.");
        } catch (LivroNaoEncontradoException e) {
            livros.add(livro);
            System.out.println("Livro cadastrado com sucesso!");
        }
    }

    public void cadastrarUsuario(Usuario usuario) {
        try {
            buscarUsuarioPorId(usuario.getId());
            System.out.println("Erro: Já existe um usuário cadastrado com este ID.");
        } catch (UsuarioNaoEncontradoException e) {
            usuarios.add(usuario);
            System.out.println("Usuário cadastrado com sucesso!");
        }
    }

    public Usuario buscarUsuarioPorId(int id) throws UsuarioNaoEncontradoException {
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) {
                return usuario;
            }
        }
        throw new UsuarioNaoEncontradoException("Usuário com ID " + id + " não existe no sistema.");
    }

    public void buscarUsuarioPorNome(String nome) {
        boolean encontrado = false;
        for (Usuario usuario : usuarios) {
            if (usuario.getNome().toLowerCase().contains(nome.toLowerCase())) {
                System.out.println(usuario);
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("Nenhum usuário encontrado com esse nome.");
        }
    }

    public Livro buscarLivroPorCodigo(int codigo) throws LivroNaoEncontradoException {
        for (Livro livro : livros) {
            if (livro.getCodigo() == codigo) {
                return livro;
            }
        }
        throw new LivroNaoEncontradoException("Livro com código " + codigo + " não existe no sistema.");
    }

    public void buscarLivroPorAutor(String autor) {
        boolean encontrado = false;
        for (Livro livro : livros) {
            if (livro.getAutor().toLowerCase().contains(autor.toLowerCase())) {
                System.out.println(livro);
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("Nenhum livro encontrado para este autor.");
        }
    }

    public void realizarEmprestimo(int codigoLivro, int idUsuario)
            throws LivroNaoEncontradoException, UsuarioNaoEncontradoException, LivroIndisponivelException {
        Livro livro = buscarLivroPorCodigo(codigoLivro);
        Usuario usuario = buscarUsuarioPorId(idUsuario);

        if (!livro.isDisp()) {
            throw new LivroIndisponivelException("O livro '" + livro.getTitulo() + "' já está emprestado.");
        }

        livro.setDisp(false);
        LocalDate dataEmprestimo = LocalDate.now();
        int diasPrazo = usuario.getPrazoEmprestimo();
        LocalDate dataPrevistaDevolucao = dataEmprestimo.plusDays(diasPrazo);

        emprestimos.add(new Emprestimo(livro, usuario, dataEmprestimo, dataPrevistaDevolucao));
        System.out.println("Empréstimo realizado com sucesso! Devolução até: " + dataPrevistaDevolucao);
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
            System.out.println("Aviso: O livro '" + livro.getTitulo() + "' ja consta como disponivel no sistema.");
            return;
        }

        emprestimoEncontrado.getLivro().setDisp(true);
        emprestimos.remove(emprestimoEncontrado);
        System.out.println("Devolucao do livro '" + livro.getTitulo() + "' realizada com sucesso!");
    }

    public void listarLivrosCadastrados() {
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }
        livros.forEach(System.out::println);
    }

    public void listarLivrosDisponiveis() {
        boolean possui = false;
        for (Livro livro : livros) {
            if (livro.isDisp()) {
                System.out.println(livro);
                possui = true;
            }
        }
        if (!possui) {
            System.out.println("Não há livros disponíveis no momento.");
        }
    }

    public void listarLivrosEmprestados() {
        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum livro emprestado atualmente.");
            return;
        }
        emprestimos.forEach(System.out::println);
    }

    public void listarLivrosEmprestadosPorUsuario(int idUsuario) {
        boolean possui = false;
        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getUsuario().getId() == idUsuario) {
                System.out.println(emprestimo.getLivro());
                possui = true;
            }
        }
        if (!possui) {
            System.out.println("Este usuário não possui empréstimos activos.");
        }
    }
}