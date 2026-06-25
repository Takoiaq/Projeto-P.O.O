package ATV;

public abstract class Usuario implements DadosEntidade {
    protected int id;
    protected String nome;
    protected String email;

    public Usuario(int id, String nome, String email) {
        this.id = id;
        this.nome = normalizarTexto(nome);
        this.email = normalizarTexto(email);
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        this.email = normalizarTexto(email);
    }

    protected String normalizarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }

    public abstract int getPrazoEmprestimo();

    public abstract String toCsv();

    @Override
    public String toString() {
        return "ID: " + id + " | Nome: " + nome + " | Email: " + email;
    }
}