package ATV;

import ATV.Exception.ArquivoInvalidoException;
import ATV.Exception.LivroIndisponivelException;
import ATV.Exception.LivroNaoEncontradoException;
import ATV.Exception.UsuarioNaoEncontradoException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Main extends JFrame implements ActionListener {
    private Biblioteca biblioteca;
    private Usuario usuarioLogado = null;

    private JTextArea Console;

    private JTextField txLogin;
    private JPasswordField txSenha;

    private JButton btLogin, btSair;
    private JButton btCadastrarLivro, btCadastrarUsuario, btBuscarLivro, btListarTodos, btListarUsuarios;
    private JButton btEmprestimo, btDevolucao, btLogout;

    public Main() {
        try {
            biblioteca = new Biblioteca();
            biblioteca.carregarDados();
        }
        catch (ArquivoInvalidoException e) {
            System.out.println("Aviso: " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println("ERRO CRÍTICO");
            e.printStackTrace();
        }

        setTitle("Sistema de Gestão de Biblioteca");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TelaLog();
        setVisible(true);
    }

    private void TelaLog() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JPanel painelLogin = new JPanel();
        painelLogin.setLayout(new GridLayout(3, 2, 10, 10));
        painelLogin.setBackground(Color.WHITE);
        painelLogin.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));

        JLabel lblLogin = new JLabel("Login / ID do Usuário:");
        lblLogin.setFont(new Font("Arial", Font.BOLD, 14));
        txLogin = new JTextField();

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.BOLD, 14));
        txSenha = new JPasswordField();

        btLogin = new JButton("Login");
        btSair = new JButton("Sair");

        btLogin.addActionListener(this);
        btSair.addActionListener(this);

        painelLogin.add(lblLogin);
        painelLogin.add(txLogin);
        painelLogin.add(lblSenha);
        painelLogin.add(txSenha);
        painelLogin.add(btLogin);
        painelLogin.add(btSair);

        add(painelLogin, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void TelaSistema() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(Color.WHITE);

        TitledBorder border = BorderFactory.createTitledBorder("Sistema de Biblioteca");
        border.setTitleFont(new Font("Arial", Font.BOLD, 16));
        border.setTitleColor(Color.GRAY);
        painelTopo.setBorder(border);

        JLabel lblUsuario = new JLabel("Usuário logado: " + usuarioLogado.getNome());
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 15));
        lblUsuario.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblInfo = new JLabel("Escolha uma opção abaixo para abrir a tela da função.");
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblInfo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        painelTopo.add(lblUsuario, BorderLayout.NORTH);
        painelTopo.add(lblInfo, BorderLayout.CENTER);

        add(painelTopo, BorderLayout.NORTH);

        Console = new JTextArea();
        Console.setEditable(false);
        Console.setFont(new Font("Monospaced", Font.PLAIN, 12));
        Console.setBackground(Color.BLACK);
        Console.setForeground(Color.GREEN);
        redirecionarConsole();

        add(new JScrollPane(Console), BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        botoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btCadastrarLivro   = new JButton("Cadastrar Livro");
        btCadastrarUsuario = new JButton("Cadastrar Usuário");
        btBuscarLivro      = new JButton("Buscar Livro");
        btListarTodos      = new JButton("Listar Livros");
        btListarUsuarios   = new JButton("Listar Usuários");
        btEmprestimo       = new JButton("Realizar Empréstimo");
        btDevolucao        = new JButton("Realizar Devolução");
        btLogout           = new JButton("Terminar Sessão");

        btCadastrarLivro.addActionListener(this);
        btCadastrarUsuario.addActionListener(this);
        btBuscarLivro.addActionListener(this);
        btListarTodos.addActionListener(this);
        btListarUsuarios.addActionListener(this);
        btEmprestimo.addActionListener(this);
        btDevolucao.addActionListener(this);
        btLogout.addActionListener(this);

        if (usuarioLogado instanceof Bibliotecario) {
            botoes.setLayout(new GridLayout(3, 3, 10, 10));

            botoes.add(btCadastrarLivro);
            botoes.add(btCadastrarUsuario);
            botoes.add(btBuscarLivro);
            botoes.add(btListarTodos);
            botoes.add(btListarUsuarios);
            botoes.add(btEmprestimo);
            botoes.add(btDevolucao);
            botoes.add(btLogout);
        }
        else {
            botoes.setLayout(new GridLayout(1, 3, 10, 10));

            botoes.add(btBuscarLivro);
            botoes.add(btListarTodos);
            botoes.add(btLogout);
        }

        add(botoes, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void redirecionarConsole() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                if (Console != null) {
                    Console.append(String.valueOf((char) b));
                    Console.setCaretPosition(Console.getDocument().getLength());
                }
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btSair) {
            int resposta = JOptionPane.showConfirmDialog(
                    this,
                    "Irá sair mesmo?",
                    "Sair",
                    JOptionPane.YES_NO_OPTION
            );

            if (resposta == JOptionPane.YES_OPTION) {
                salvarDados();
                System.exit(0);
            }
        }
        else if (e.getSource() == btLogin) {
            fazerLogin();
        }
        else if (e.getSource() == btLogout) {
            salvarDados();
            usuarioLogado = null;
            TelaLog();
        }
        else if (e.getSource() == btCadastrarLivro) {
            cadastrarLivro();
        }
        else if (e.getSource() == btCadastrarUsuario) {
            cadastrarUsuario();
        }
        else if (e.getSource() == btBuscarLivro) {
            buscarLivro();
        }
        else if (e.getSource() == btListarTodos) {
            listarLivros();
        }
        else if (e.getSource() == btListarUsuarios) {
            listarUsuarios();
        }
        else if (e.getSource() == btEmprestimo) {
            realizarEmprestimo();
        }
        else if (e.getSource() == btDevolucao) {
            realizarDevolucao();
        }
    }

    private void fazerLogin() {
        String login = txLogin.getText().trim();
        String senha = new String(txSenha.getPassword()).trim();

        if (login.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Digite o login ou o ID do usuário.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (login.equalsIgnoreCase("admin") && senha.equals("admin")) {
            usuarioLogado = new Bibliotecario(1, "Administrador", "admin@email.com");
            TelaSistema();
            System.out.println("Login de administrador realizado com sucesso.");
            return;
        }

        if (!senha.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Login inválido.\nPara administrador use: admin / admin\nPara usuário comum, informe apenas o ID e deixe a senha vazia.",
                    "Erro de Login",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            int idUsuario = Integer.parseInt(login);
            Usuario usuario = biblioteca.buscarUsuarioPorId(idUsuario);

            usuarioLogado = usuario;
            TelaSistema();

            System.out.println("Login realizado com sucesso: " + usuarioLogado);
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Login inválido. Use admin/admin ou digite um ID numérico de usuário.",
                    "Erro de Login",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao fazer login: " + ex.getMessage(),
                    "Erro de Login",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cadastrarLivro() {
        Console.setText("");

        JTextField campoCodigo = new JTextField();
        JTextField campoTitulo = new JTextField();
        JTextField campoAutor = new JTextField();
        JTextField campoAno = new JTextField();
        JTextField campoQuantidade = new JTextField();

        JPanel painel = new JPanel(new GridLayout(5, 2, 10, 10));

        painel.add(new JLabel("Código do Livro:"));
        painel.add(campoCodigo);

        painel.add(new JLabel("Título:"));
        painel.add(campoTitulo);

        painel.add(new JLabel("Autor:"));
        painel.add(campoAutor);

        painel.add(new JLabel("Ano de Publicação:"));
        painel.add(campoAno);

        painel.add(new JLabel("Quantidade:"));
        painel.add(campoQuantidade);

        int resposta = JOptionPane.showConfirmDialog(
                this,
                painel,
                "Cadastrar Livro",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resposta != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            int codigo = Integer.parseInt(campoCodigo.getText().trim());
            String titulo = campoTitulo.getText().trim();
            String autor = campoAutor.getText().trim();
            int ano = Integer.parseInt(campoAno.getText().trim());
            int qtd = Integer.parseInt(campoQuantidade.getText().trim());

            if (titulo.isBlank() || autor.isBlank()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Preencha o título e o autor do livro.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            Livro livro = new Livro(codigo, titulo, autor, ano, qtd);
            biblioteca.cadastrarLivro(livro);
            salvarDados();

            JOptionPane.showMessageDialog(
                    this,
                    "Livro cadastrado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            System.out.println("=== Livro cadastrado ===");
            System.out.println(livro);
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Código, ano e quantidade precisam ser números inteiros.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao cadastrar livro: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cadastrarUsuario() {
        Console.setText("");

        JTextField campoId = new JTextField();
        JTextField campoNome = new JTextField();
        JTextField campoEmail = new JTextField();

        JComboBox<String> campoTipo = new JComboBox<>(new String[] {
                "ESTUDANTE",
                "BIBLIOTECARIO"
        });

        JPanel painel = new JPanel(new GridLayout(4, 2, 10, 10));

        painel.add(new JLabel("ID do Usuário:"));
        painel.add(campoId);

        painel.add(new JLabel("Nome:"));
        painel.add(campoNome);

        painel.add(new JLabel("Email:"));
        painel.add(campoEmail);

        painel.add(new JLabel("Tipo:"));
        painel.add(campoTipo);

        int resposta = JOptionPane.showConfirmDialog(
                this,
                painel,
                "Cadastrar Usuário",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resposta != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            int id = Integer.parseInt(campoId.getText().trim());
            String nome = campoNome.getText().trim();
            String email = campoEmail.getText().trim();
            String tipo = (String) campoTipo.getSelectedItem();

            if (nome.isBlank() || email.isBlank()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Preencha nome e email do usuário.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            Usuario usuario;

            if ("BIBLIOTECARIO".equals(tipo)) {
                usuario = new Bibliotecario(id, nome, email);
            } else {
                usuario = new Estudante(id, nome, email);
            }

            biblioteca.cadastrarUsuario(usuario);
            salvarDados();

            JOptionPane.showMessageDialog(
                    this,
                    "Usuário cadastrado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            System.out.println("=== Usuário cadastrado ===");
            System.out.println(usuario);
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "O ID do usuário precisa ser um número inteiro.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao cadastrar usuário: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void buscarLivro() {
        Console.setText("");

        JTextField campoCodigo = new JTextField();
        JTextField campoTitulo = new JTextField();
        JTextField campoAutor = new JTextField();

        JPanel painel = new JPanel(new GridLayout(3, 2, 10, 10));

        painel.add(new JLabel("Código do Livro:"));
        painel.add(campoCodigo);

        painel.add(new JLabel("Título do Livro:"));
        painel.add(campoTitulo);

        painel.add(new JLabel("Autor do Livro:"));
        painel.add(campoAutor);

        int resposta = JOptionPane.showConfirmDialog(
                this,
                painel,
                "Buscar Livro",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resposta != JOptionPane.OK_OPTION) {
            return;
        }

        String codigoTexto = campoCodigo.getText().trim();
        String tituloTexto = campoTitulo.getText().trim();
        String autorTexto  = campoAutor.getText().trim();

        if (codigoTexto.isBlank() && tituloTexto.isBlank() && autorTexto.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Preencha pelo menos um campo para buscar: Código, Título ou Autor.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            if (!codigoTexto.isBlank()) {
                int codLivro = Integer.parseInt(codigoTexto);
                Livro livro = biblioteca.buscarLivroPorCodigo(codLivro);

                System.out.println("=== Resultado por código ===");
                System.out.println(livro);
            }

            if (!tituloTexto.isBlank()) {
                System.out.println("=== Resultado por título ===");
                biblioteca.buscarLivroPorTitulo(tituloTexto);
            }

            if (!autorTexto.isBlank()) {
                System.out.println("=== Resultado por autor ===");
                biblioteca.buscarLivroPorAutor(autorTexto);
            }
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "O código do livro precisa ser um número inteiro.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (LivroNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Livro não encontrado",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void listarLivros() {
        Console.setText("");
        biblioteca.listarLivrosCadastrados();
    }

    private void listarUsuarios() {
        Console.setText("");

        try {
            biblioteca.listarUsuariosCadastrados();
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao listar usuários: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void realizarEmprestimo() {
        Console.setText("");

        JTextField campoCodLivro = new JTextField();
        JTextField campoIdUsuario = new JTextField();

        JPanel painel = new JPanel(new GridLayout(2, 2, 10, 10));

        painel.add(new JLabel("Código do Livro:"));
        painel.add(campoCodLivro);

        painel.add(new JLabel("ID do Usuário:"));
        painel.add(campoIdUsuario);

        int resposta = JOptionPane.showConfirmDialog(
                this,
                painel,
                "Realizar Empréstimo",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resposta != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            int codLivro = Integer.parseInt(campoCodLivro.getText().trim());
            int idUsuario = Integer.parseInt(campoIdUsuario.getText().trim());

            biblioteca.realizarEmprestimo(codLivro, idUsuario);
            salvarDados();

            JOptionPane.showMessageDialog(
                    this,
                    "Empréstimo realizado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            System.out.println("=== Empréstimo realizado ===");
            System.out.println("Código do livro: " + codLivro);
            System.out.println("ID do usuário: " + idUsuario);
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Código do livro e ID do usuário precisam ser números.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (LivroIndisponivelException | LivroNaoEncontradoException | UsuarioNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro de Regra de Negócio",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void realizarDevolucao() {
        Console.setText("");

        JTextField campoCodLivro = new JTextField();

        JPanel painel = new JPanel(new GridLayout(1, 2, 10, 10));

        painel.add(new JLabel("Código do Livro:"));
        painel.add(campoCodLivro);

        int resposta = JOptionPane.showConfirmDialog(
                this,
                painel,
                "Realizar Devolução",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resposta != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            int codLivro = Integer.parseInt(campoCodLivro.getText().trim());

            biblioteca.realizarDevolucao(codLivro);
            salvarDados();

            JOptionPane.showMessageDialog(
                    this,
                    "Devolução registrada com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            System.out.println("=== Devolução realizada ===");
            System.out.println("Código do livro: " + codLivro);
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "O código do livro precisa ser um número.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void salvarDados() {
        try {
            biblioteca.gravarDados();
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível salvar os dados: " + ex.getMessage(),
                    "Erro ao Salvar",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}