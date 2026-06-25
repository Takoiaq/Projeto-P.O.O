package ATV;

public abstract class Usuario implements DadosEntidade {
    protected int id;
    protected String nome;
    protected String email;

    public Usuario(int id, String nome, String email) {
        if (id < 0) {
            throw new IllegalArgumentException("ID de usuário não pode ser negativo.");
        }

        this.id = id;
        this.nome = normalizarTexto(nome);
        this.email = normalizarEmail(email);
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID de usuário não pode ser negativo.");
        }

        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = normalizarTexto(nome);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = normalizarEmail(email);
    }

    protected String normalizarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }

    private String normalizarEmail(String email) {
        String emailTratado = normalizarTexto(email);

        if (!emailValido(emailTratado)) {
            throw new IllegalArgumentException(
                    "Email inválido. Use um formato válido, exemplo: nome@email.com"
            );
        }

        return emailTratado;
    }

    private boolean emailValido(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }

        if (email.contains(" ")) {
            return false;
        }

        return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public abstract int getPrazoEmprestimo();

    public abstract String toCsv();

    @Override
    public String toString() {
        return "ID: " + id + " | Nome: " + nome + " | Email: " + email;
    }
}