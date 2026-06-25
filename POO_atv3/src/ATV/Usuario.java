package ATV;

public abstract class Usuario implements DadosEntidade {
    protected int id; 
    protected String nome; 
    protected String email;

    public Usuario(int id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
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
        this.nome = nome;
    }
    
    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // O polimorfismo puro: as subclasses Estudante e Professor são obrigadas a implementar as regras abaixo
    public abstract int getPrazoEmprestimo();

    public abstract String toCsv();

    @Override
    public String toString() {
        return "ID: " + id + " | Nome: " + nome + " | Email: " + email;
    }
}