package ATV;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import java.util.ArrayList;
import java.util.List;

import ATV.Exception.ArquivoInvalidoException;

public class Persistencia {
    private final File arqLivros;
    private final File arqUsuarios;
    private final File arqEmprestimos;

    public Persistencia() {
        this("livros.txt", "usuarios.txt", "emprestimos.txt");
    }

    public Persistencia(String arqLivros, String arqUsuarios, String arqEmprestimos) {
        this.arqLivros = new File(arqLivros);
        this.arqUsuarios = new File(arqUsuarios);
        this.arqEmprestimos = new File(arqEmprestimos);
    }

    public void gravarDados(List<Livro> livros, List<Usuario> usuarios, List<Emprestimo> emprestimos)
            throws ArquivoInvalidoException {

        try {
            verificarDiretorio(arqLivros);
            verificarDiretorio(arqUsuarios);
            verificarDiretorio(arqEmprestimos);

            salvarLivros(livros);
            salvarUsuarios(usuarios);
            salvarEmprestimos(emprestimos);

        } catch (IOException e) {
            throw new ArquivoInvalidoException("Erro ao gravar os arquivos: " + e.getMessage());
        }
    }

    public DadosBiblioteca carregarDados() throws ArquivoInvalidoException {
        try {
            List<Livro> livros = carregarLivros();
            List<Usuario> usuarios = carregarUsuarios();
            List<Emprestimo> emprestimos = carregarEmprestimos(livros, usuarios);

            return new DadosBiblioteca(livros, usuarios, emprestimos);

        } catch (IOException | NumberFormatException | DateTimeParseException | IndexOutOfBoundsException e) {
            throw new ArquivoInvalidoException("Erro ao ler os arquivos. Verifique se os dados estão no formato correto.");
        }
    }

    private void verificarDiretorio(File arquivo) throws IOException {
        File pasta = arquivo.getParentFile();

        if (pasta != null && !pasta.exists() && !pasta.mkdirs()) {
            throw new IOException("Não foi possível criar a pasta: " + pasta.getAbsolutePath());
        }
    }

    private void salvarLivros(List<Livro> livros) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arqLivros))) {
            for (Livro livro : livros) {
                bw.write(livro.toCsv());
                bw.newLine();
            }
        }
    }

    private void salvarUsuarios(List<Usuario> usuarios) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arqUsuarios))) {
            for (Usuario usuario : usuarios) {
                bw.write(usuario.toCsv());
                bw.newLine();
            }
        }
    }

    private void salvarEmprestimos(List<Emprestimo> emprestimos) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arqEmprestimos))) {
            for (Emprestimo emprestimo : emprestimos) {
                bw.write(emprestimo.toCsv());
                bw.newLine();
            }
        }
    }

    private List<Livro> carregarLivros() throws IOException, ArquivoInvalidoException {
        List<Livro> lista = new ArrayList<>();

        if (!arqLivros.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arqLivros))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) {
                    continue;
                }

                String[] tokens = linha.split(";", -1);
                validarTokens(tokens, 5, "livros.txt");

                int codigo = Integer.parseInt(tokens[0].trim());
                String titulo = tokens[1].trim();
                String autor = tokens[2].trim();
                int anoPublicacao = Integer.parseInt(tokens[3].trim());
                boolean disponivel = parseStringToBool(tokens[4].trim());

                lista.add(new Livro(codigo, titulo, autor, anoPublicacao, disponivel));
            }
        }

        return lista;
    }

    private List<Usuario> carregarUsuarios() throws IOException, ArquivoInvalidoException {
        List<Usuario> lista = new ArrayList<>();

        if (!arqUsuarios.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arqUsuarios))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) {
                    continue;
                }

                String[] tokens = linha.split(";", -1);
                validarTokens(tokens, 4, "usuarios.txt");

                String tipo = tokens[0].trim().toUpperCase();
                int id = Integer.parseInt(tokens[1].trim());
                String nome = tokens[2].trim();
                String email = tokens[3].trim();

                if (tipo.equals("ESTUDANTE")) {
                    lista.add(new Estudante(id, nome, email));
                } else if (tipo.equals("BIBLIOTECARIO")) {
                    lista.add(new Bibliotecario(id, nome, email));
                } else {
                    throw new ArquivoInvalidoException("Tipo de usuário inválido em usuarios.txt: " + tipo);
                }
            }
        }

        return lista;
    }

    private List<Emprestimo> carregarEmprestimos(List<Livro> livros, List<Usuario> usuarios)
            throws IOException, ArquivoInvalidoException {

        List<Emprestimo> lista = new ArrayList<>();

        if (!arqEmprestimos.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arqEmprestimos))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) {
                    continue;
                }

                String[] tokens = linha.split(";", -1);
                validarTokens(tokens, 4, "emprestimos.txt");

                int codigoLivro = Integer.parseInt(tokens[0].trim());
                int idUsuario = Integer.parseInt(tokens[1].trim());
                LocalDate dataEmprestimo = LocalDate.parse(tokens[2].trim());
                LocalDate dataPrevistaDevolucao = LocalDate.parse(tokens[3].trim());

                Livro livro = acharLivro(livros, codigoLivro);
                Usuario usuario = acharUsuario(usuarios, idUsuario);

                if (livro == null || usuario == null) {
                    throw new ArquivoInvalidoException(
                            "Arquivo de empréstimos possui livro ou usuário que não existe no cadastro.");
                }

                lista.add(new Emprestimo(livro, usuario, dataEmprestimo, dataPrevistaDevolucao));
            }
        }

        return lista;
    }

    private void validarTokens(String[] tokens, int esperado, String arquivo) throws ArquivoInvalidoException {
        if (tokens.length != esperado) {
            throw new ArquivoInvalidoException("Linha inválida em " + arquivo + ".");
        }
    }

    private boolean parseStringToBool(String valor) throws ArquivoInvalidoException {
        valor = valor.trim();

        if ("true".equalsIgnoreCase(valor)) {
            return true;
        }

        if ("false".equalsIgnoreCase(valor)) {
            return false;
        }

        throw new ArquivoInvalidoException("Valor booleano inválido: " + valor);
    }

    private Livro acharLivro(List<Livro> livros, int codigo) {
        for (Livro livro : livros) {
            if (livro.getCodigo() == codigo) {
                return livro;
            }
        }

        return null;
    }

    private Usuario acharUsuario(List<Usuario> usuarios, int id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) {
                return usuario;
            }
        }

        return null;
    }

    public static class DadosBiblioteca {
        private final List<Livro> livros;
        private final List<Usuario> usuarios;
        private final List<Emprestimo> emprestimos;

        public DadosBiblioteca(List<Livro> livros, List<Usuario> usuarios, List<Emprestimo> emprestimos) {
            this.livros = livros;
            this.usuarios = usuarios;
            this.emprestimos = emprestimos;
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
}