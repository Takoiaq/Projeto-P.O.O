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
        if (livro == null) {
            System.out.println("[Erro] Livro inválido.");
            return;
        }

        if (livro.getCodigo() < 0) {
            System.out.println("[Erro] Código do livro não pode ser negativo.");
            return;
        }

        if (livro.getQuantidade() < 0) {
            System.out.println("[Erro] Quantidade do livro não pode ser negativa.");
            return;
        }

        try {
            buscarLivroPorCodigo(livro.getCodigo());
            System.out.println("[Erro] Este código de livro já está sendo usado / já existe no sistema.");
        } catch (LivroNaoEncontradoException e) {
            livros.add(livro);
            System.out.println("Livro cadastrado com sucesso!");
        }
    }

    public void cadastrarUsuario(Usuario usuario) {
        if (usuario == null) {
            System.out.println("[Erro] Usuário inválido.");
            return;
        }

        if (usuario.getId() < 0) {
            System.out.println("[Erro] ID de usuário não pode ser negativo.");
            return;
        }

        try {
            buscarUsuarioPorId(usuario.getId());
            System.out.println("[Erro] Este ID de usuário já está sendo usado / já existe no sistema.");
        } catch (UsuarioNaoEncontradoException e) {
            usuarios.add(usuario);
            System.out.println("Usuário cadastrado com sucesso!");
        }
    }

    public Usuario buscarUsuarioPorId(int id) throws UsuarioNaoEncontradoException {
        if (id < 0) {
            throw new UsuarioNaoEncontradoException("ID de usuário não pode ser negativo.");
        }

        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) {
                return usuario;
            }
        }

        throw new UsuarioNaoEncontradoException("Usuário com ID " + id + " não existe no sistema.");
    }

    public void buscarUsuarioPorNome(String nome) {
        String nomeBusca = nome == null ? "" : nome.trim();
        boolean encontrado = false;

        for (Usuario usuario : usuarios) {
            if (usuario.getNome().toLowerCase().contains(nomeBusca.toLowerCase())) {
                System.out.println(usuario);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("Nenhum usuário encontrado com esse nome.");
        }
    }

    public Livro buscarLivroPorCodigo(int codigo) throws LivroNaoEncontradoException {
        if (codigo < 0) {
            throw new LivroNaoEncontradoException("Código do livro não pode ser negativo.");
        }

        for (Livro livro : livros) {
            if (livro.getCodigo() == codigo) {
                return livro;
            }
        }

        throw new LivroNaoEncontradoException("Livro com código " + codigo + " não existe no sistema.");
    }

    public void buscarLivroPorAutor(String autor) {
        String autorBusca = autor == null ? "" : autor.trim();
        boolean encontrado = false;

        for (Livro livro : livros) {
            if (livro.getAutor().toLowerCase().contains(autorBusca.toLowerCase())) {
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

        if (codigoLivro < 0) {
            throw new LivroNaoEncontradoException("Código do livro não pode ser negativo.");
        }

        if (idUsuario < 0) {
            throw new UsuarioNaoEncontradoException("ID de usuário não pode ser negativo.");
        }

        Livro livro = buscarLivroPorCodigo(codigoLivro);
        Usuario usuario = buscarUsuarioPorId(idUsuario);

        if (!livro.isDisp()) {
            throw new LivroIndisponivelException(
                    "O livro '" + livro.getTitulo() + "' não possui unidades disponíveis."
            );
        }

        livro.emprestarUnidade();

        LocalDate dataEmprestimo = LocalDate.now();
        int diasPrazo = usuario.getPrazoEmprestimo();
        LocalDate dataPrevistaDevolucao = dataEmprestimo.plusDays(diasPrazo);

        emprestimos.add(new Emprestimo(livro, usuario, dataEmprestimo, dataPrevistaDevolucao));

        System.out.println("Empréstimo realizado com sucesso! Devolução até: " + dataPrevistaDevolucao);
        System.out.println("Quantidade restante do livro: " + livro.getQuantidade());
    }

    public void realizarDevolucao(int codigoLivro) throws LivroNaoEncontradoException {
        if (codigoLivro < 0) {
            throw new LivroNaoEncontradoException("Código do livro não pode ser negativo.");
        }

        Livro livro = buscarLivroPorCodigo(codigoLivro);
        Emprestimo emprestimoEncontrado = null;

        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getLivro().getCodigo() == codigoLivro) {
                emprestimoEncontrado = emprestimo;
                break;
            }
        }

        if (emprestimoEncontrado == null) {
            System.out.println("Aviso: O livro '" + livro.getTitulo() + "' não possui empréstimo ativo no sistema.");
            return;
        }

        emprestimoEncontrado.getLivro().devolverUnidade();
        emprestimos.remove(emprestimoEncontrado);

        System.out.println("Devolução do livro '" + livro.getTitulo() + "' realizada com sucesso!");
        System.out.println("Quantidade atual do livro: " + livro.getQuantidade());
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
        if (idUsuario < 0) {
            System.out.println("[Erro] ID de usuário não pode ser negativo.");
            return;
        }

        boolean possui = false;

        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getUsuario().getId() == idUsuario) {
                System.out.println(emprestimo.getLivro());
                possui = true;
            }
        }

        if (!possui) {
            System.out.println("Este usuário não possui empréstimos ativos.");
        }
    }

    public void listarUsuariosComEmprestimos() {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        System.out.println("\n===== USUÁRIOS CADASTRADOS E EMPRÉSTIMOS =====");

        for (Usuario usuario : usuarios) {
            String tipoUsuario;

            if (usuario instanceof Bibliotecario) {
                tipoUsuario = "Bibliotecário";
            } else if (usuario instanceof Estudante) {
                tipoUsuario = "Estudante";
            } else {
                tipoUsuario = "Usuário";
            }

            System.out.println("\n----------------------------------------");
            System.out.println("Tipo: " + tipoUsuario);
            System.out.println("ID: " + usuario.getId());
            System.out.println("Nome: " + usuario.getNome());
            System.out.println("Email: " + usuario.getEmail());
            System.out.println("Prazo de empréstimo: " + usuario.getPrazoEmprestimo() + " dias");

            boolean possuiEmprestimo = false;

            for (Emprestimo emprestimo : emprestimos) {
                if (emprestimo.getUsuario().getId() == usuario.getId()) {
                    Livro livro = emprestimo.getLivro();

                    if (!possuiEmprestimo) {
                        System.out.println("Empréstimos:");
                    }

                    System.out.println("- Livro: " + livro.getTitulo());
                    System.out.println("  Código: " + livro.getCodigo());
                    System.out.println("  Autor: " + livro.getAutor());
                    System.out.println("  Quantidade atual disponível: " + livro.getQuantidade());
                    System.out.println("  Data do empréstimo: " + emprestimo.getDataEmp());
                    System.out.println("  Devolução prevista: " + emprestimo.getDatadev());

                    possuiEmprestimo = true;
                }
            }

            if (!possuiEmprestimo) {
                System.out.println("Empréstimos: nenhum empréstimo ativo.");
            }
        }

        System.out.println("----------------------------------------");
    }
}