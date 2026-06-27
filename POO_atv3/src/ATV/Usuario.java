package ATV;

public abstract class Usuario {
    private int id;
    private String nome;
    private String email;

    public Usuario(int id, String nome, String email) {
        if (id < 0) {
            throw new IllegalArgumentException("ID de usuário não pode ser negativo.");
        }

        this.id = id;
        this.nome = normalizarTexto(nome);
        this.email = normalizarTexto(email);
    }

    public int getId() {
        return id;
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
        this.email = normalizarTexto(email);
    }

    public abstract int getPrazoEmprestimo();

    public abstract String getTipo();

    public String toCsv() {
        return getTipo() + ";" + id + ";" + nome + ";" + email;
    }

    private String normalizarTexto(String texto) {
        return texto == null ? "" : texto.trim();
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