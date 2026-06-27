package ATV;

public abstract class Usuario implements Interface {
    private int id;
    private String nome;
    private String email;

    public Usuario(int id, String nome, String email) {
        if (id < 0) {
            throw new IllegalArgumentException("ID não pode ser negativo.");
        }

        this.id = id;
        this.nome = normalizarNome(nome);
        this.email = normalizarEmail(email);
    }

    private String normalizarNome(String nome) {
        if (nome == null || nome.trim().isBlank()) {
            throw new IllegalArgumentException("Nome não pode estar vazio.");
        }

        return nome.trim();
    }

    private String normalizarEmail(String email) {
        if (email == null || email.trim().isBlank()) {
            throw new IllegalArgumentException("Email não pode estar vazio.");
        }

        email = email.trim();

        if (email.contains(" ")) {
            throw new IllegalArgumentException("Email não pode conter espaços.");
        }

        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Email inválido. Use um formato válido, exemplo: nome@email.com");
        }

        return email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID não pode ser negativo.");
        }

        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = normalizarNome(nome);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = normalizarEmail(email);
    }

    public abstract int getPrazoEmprestimo();

    public abstract String getTipo();

    @Override
    public String toCsv() {
        return getTipo() + ";" + id + ";" + nome + ";" + email;
    }

    @Override
    public String toString() {
        return "Tipo: " + getTipo() +
                " | ID: " + id +
                " | Nome: " + nome +
                " | Email: " + email +
                " | Prazo: " + getPrazoEmprestimo() + " dias";
    }
}
