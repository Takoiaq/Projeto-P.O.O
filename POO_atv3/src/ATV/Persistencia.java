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
    private final File arquivoLivros;
    private final File arquivoUsuarios;
    private final File arquivoEmprestimos;

    public Persistencia() {
        this("livros.txt", "usuarios.txt", "emprestimos.txt");
    }

    public Persistencia(String arquivoLivros, String arquivoUsuarios, String arquivoEmprestimos) {
        this.arquivoLivros = new File(arquivoLivros);
        this.arquivoUsuarios = new File(arquivoUsuarios);
        this.arquivoEmprestimos = new File(arquivoEmprestimos);
    }

    public void gravarDados(List<Livro> livros, List<Usuario> usuarios, List<Emprestimo> emprestimos)
            throws ArquivoInvalidoException {
        try {
            criarPastaSeNecessario(arquivoLivros);
            criarPastaSeNecessario(arquivoUsuarios);
            criarPastaSeNecessario(arquivoEmprestimos);

            gravarLivros(livros);
            gravarUsuarios(usuarios);
            gravarEmprestimos(emprestimos);
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

    private void criarPastaSeNecessario(File arquivo) throws IOException {
        File pasta = arquivo.getParentFile();
        if (pasta != null && !pasta.exists() && !pasta.mkdirs()) {
            throw new IOException("Não foi possível criar a pasta: " + pasta.getAbsolutePath());
        }
    }

    private void gravarLivros(List<Livro> livros) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoLivros))) {
            for (Livro livro : livros) {
                bw.write(livro.convert());
                bw.newLine();
            }
        }
    }

    private void gravarUsuarios(List<Usuario> usuarios) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoUsuarios))) {
            for (Usuario usuario : usuarios) {
                bw.write(usuario.convert());
                bw.newLine();
            }
        }
    }

    private void gravarEmprestimos(List<Emprestimo> emprestimos) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoEmprestimos))) {
            for (Emprestimo emprestimo : emprestimos) {
                bw.write(emprestimo.toCsv());
                bw.newLine();
            }
        }
    }

    private List<Livro> carregarLivros() throws IOException, ArquivoInvalidoException {
        List<Livro> livros = new ArrayList<>();
        if (!arquivoLivros.exists()) {
            return livros;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoLivros))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) {
                    continue;
                }

                String[] campos = linha.split(";", -1);
                validarQuantidadeCampos(campos, 5, "livros.txt");

                int codigo = Integer.parseInt(campos[0]);
                String titulo = campos[1];
                String autor = campos[2];
                int anoPublicacao = Integer.parseInt(campos[3]);
                boolean disponivel = lerBoolean(campos[4]);

                livros.add(new Livro(codigo, titulo, autor, anoPublicacao, disponivel));
            }
        }
        return livros;
    }

    private List<Usuario> carregarUsuarios() throws IOException, ArquivoInvalidoException {
        List<Usuario> usuarios = new ArrayList<>();
        if (!arquivoUsuarios.exists()) {
            return usuarios;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoUsuarios))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) {
                    continue;
                }

                String[] campos = linha.split(";", -1);
                validarQuantidadeCampos(campos, 3, "usuarios.txt");

                int id = Integer.parseInt(campos[0]);
                String nome = campos[1];
                String email = campos[2];

                usuarios.add(new Usuario(id, nome, email));
            }
        }
        return usuarios;
    }

    private List<Emprestimo> carregarEmprestimos(List<Livro> livros, List<Usuario> usuarios)
            throws IOException, ArquivoInvalidoException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        if (!arquivoEmprestimos.exists()) {
            return emprestimos;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoEmprestimos))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) {
                    continue;
                }

                String[] campos = linha.split(";", -1);
                validarQuantidadeCampos(campos, 4, "emprestimos.txt");

                int codigoLivro = Integer.parseInt(campos[0]);
                int idUsuario = Integer.parseInt(campos[1]);
                LocalDate dataEmprestimo = LocalDate.parse(campos[2]);
                LocalDate dataPrevistaDevolucao = LocalDate.parse(campos[3]);

                Livro livro = procurarLivro(livros, codigoLivro);
                Usuario usuario = procurarUsuario(usuarios, idUsuario);

                if (livro == null || usuario == null) {
                    throw new ArquivoInvalidoException(
                            "Arquivo de empréstimos possui livro ou usuário que não existe no cadastro.");
                }

                emprestimos.add(new Emprestimo(livro, usuario, dataEmprestimo, dataPrevistaDevolucao));
            }
        }
        return emprestimos;
    }

    private void validarQuantidadeCampos(String[] campos, int esperado, String arquivo) throws ArquivoInvalidoException {
        if (campos.length != esperado) {
            throw new ArquivoInvalidoException("Linha inválida em " + arquivo + ".");
        }
    }

    private boolean lerBoolean(String valor) throws ArquivoInvalidoException {
        if ("true".equalsIgnoreCase(valor)) {
            return true;
        }
        if ("false".equalsIgnoreCase(valor)) {
            return false;
        }
        throw new ArquivoInvalidoException("Valor booleano inválido: " + valor);
    }

    private Livro procurarLivro(List<Livro> livros, int codigo) {
        for (Livro livro : livros) {
            if (livro.getCodigo() == codigo) {
                return livro;
            }
        }
        return null;
    }

    private Usuario procurarUsuario(List<Usuario> usuarios, int id) {
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
