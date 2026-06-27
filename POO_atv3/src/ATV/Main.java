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

    private JTextField txLogin, txCodLivro, txTitulo, txAutor, txAno, txQtd, txIdUsuario;
    private JPasswordField txSenha;

    private JButton btLogin, btSair;
    private JButton btCadastrarLivro, btBuscarLivro, btListarTodos, btListarUsuarios;
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
        setSize(800, 600);
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

        JPanel painelSis = new JPanel();
        painelSis.setLayout(new GridLayout(6, 2, 10, 10));
        painelSis.setBackground(Color.WHITE);

        TitledBorder border = BorderFactory.createTitledBorder("Sistema de Biblioteca");
        border.setTitleFont(new Font("Arial", Font.BOLD, 16));
        border.setTitleColor(Color.LIGHT_GRAY);
        painelSis.setBorder(border);

        txCodLivro  = new JTextField();
        txTitulo    = new JTextField();
        txAutor     = new JTextField();
        txAno       = new JTextField();
        txQtd       = new JTextField();
        txIdUsuario = new JTextField();

        painelSis.add(NTxt("Código do Livro:"));
        painelSis.add(txCodLivro);

        painelSis.add(NTxt("Título do Livro:"));
        painelSis.add(txTitulo);

        painelSis.add(NTxt("Autor:"));
        painelSis.add(txAutor);

        painelSis.add(NTxt("Ano de Publicação:"));
        painelSis.add(txAno);

        painelSis.add(NTxt("Quantidade de Livros:"));
        painelSis.add(txQtd);

        painelSis.add(NTxt("ID do Usuário:"));
        painelSis.add(txIdUsuario);

        add(painelSis, BorderLayout.NORTH);

        Console = new JTextArea();
        Console.setEditable(false);
        Console.setFont(new Font("Monospaced", Font.PLAIN, 12));
        Console.setBackground(Color.BLACK);
        Console.setForeground(Color.GREEN);
        redirecionarConsole();

        add(new JScrollPane(Console), BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        botoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btCadastrarLivro = new JButton("Cadastrar Livro");
        btBuscarLivro    = new JButton("Buscar Livro");
        btListarTodos    = new JButton("Listar Livros");
        btListarUsuarios = new JButton("Listar Usuários");
        btEmprestimo     = new JButton("Realizar Empréstimo");
        btDevolucao      = new JButton("Realizar Devolução");
        btLogout         = new JButton("Terminar Sessão");

        btCadastrarLivro.addActionListener(this);
        btBuscarLivro.addActionListener(this);
        btListarTodos.addActionListener(this);
        btListarUsuarios.addActionListener(this);
        btEmprestimo.addActionListener(this);
        btDevolucao.addActionListener(this);
        btLogout.addActionListener(this);

        if (usuarioLogado instanceof Bibliotecario) {
            botoes.setLayout(new GridLayout(3, 3, 10, 10));

            botoes.add(btBuscarLivro);
            botoes.add(btListarTodos);
            botoes.add(btListarUsuarios);
            botoes.add(btCadastrarLivro);
            botoes.add(btEmprestimo);
            botoes.add(btDevolucao);
            botoes.add(btLogout);
        }
        else {
            txCodLivro.setEditable(true);
            txTitulo.setEditable(true);
            txAutor.setEditable(true);

            txAno.setEditable(false);
            txQtd.setEditable(false);
            txIdUsuario.setEditable(false);

            botoes.setLayout(new GridLayout(1, 3, 10, 10));

            botoes.add(btBuscarLivro);
            botoes.add(btListarTodos);
            botoes.add(btLogout);
        }

        add(botoes, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JLabel NTxt(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
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
                    null,
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
                    null,
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
                    null,
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
                    null,
                    "Login inválido. Use admin/admin ou digite um ID numérico de usuário.",
                    "Erro de Login",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Erro ao fazer login: " + ex.getMessage(),
                    "Erro de Login",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cadastrarLivro() {
        Console.setText("");

        try {
            int codigo = Integer.parseInt(txCodLivro.getText().trim());
            String titulo = txTitulo.getText().trim();
            String autor = txAutor.getText().trim();
            int ano = Integer.parseInt(txAno.getText().trim());
            int qtd = Integer.parseInt(txQtd.getText().trim());

            Livro livro = new Livro(codigo, titulo, autor, ano, qtd);
            biblioteca.cadastrarLivro(livro);

            JOptionPane.showMessageDialog(
                    null,
                    "Livro registrado com sucesso",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limparCampos();
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Inválido! Preencha Código, Ano e Quantidade com números inteiros.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void buscarLivro() {
        Console.setText("");

        String codigoTexto = txCodLivro.getText().trim();
        String tituloTexto = txTitulo.getText().trim();
        String autorTexto  = txAutor.getText().trim();

        if (codigoTexto.isBlank() && tituloTexto.isBlank() && autorTexto.isBlank()) {
            JOptionPane.showMessageDialog(
                    null,
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

            limparCamposBusca();
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "O código do livro precisa ser um número inteiro.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (LivroNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    "Livro não encontrado",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
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
                    null,
                    "Erro ao listar usuários: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void realizarEmprestimo() {
        try {
            int codLivro = Integer.parseInt(txCodLivro.getText().trim());
            int idUsuario = Integer.parseInt(txIdUsuario.getText().trim());

            biblioteca.realizarEmprestimo(codLivro, idUsuario);

            JOptionPane.showMessageDialog(
                    null,
                    "Empréstimo realizado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limparCampos();
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Preencha os campos 'Código' e 'ID Usuário' com números.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (LivroIndisponivelException | LivroNaoEncontradoException | UsuarioNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    "Erro de Regra de Negócio",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void realizarDevolucao() {
        try {
            int codLivro = Integer.parseInt(txCodLivro.getText().trim());

            biblioteca.realizarDevolucao(codLivro);

            JOptionPane.showMessageDialog(
                    null,
                    "Devolução registrada com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limparCampos();
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Preencha o Código do Livro com número.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
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
                    null,
                    "Não foi possível salvar os dados: " + ex.getMessage(),
                    "Erro ao Salvar",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limparCamposBusca() {
        txCodLivro.setText("");
        txTitulo.setText("");
        txAutor.setText("");
    }

    private void limparCampos() {
        txCodLivro.setText("");
        txTitulo.setText("");
        txAutor.setText("");
        txAno.setText("");
        txQtd.setText("");
        txIdUsuario.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}