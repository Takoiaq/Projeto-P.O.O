package ATV;

public class Usuario implements DadosEntidade {
    private int id; 
    private String nome; 
    private String email;

    public Usuario(int id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    @Override
    public int getId() { 
        return id; 
    }
    
    public String getNome() { 
        return nome; 
    }
    
    public String getEmail() { 
        return email; 
    }

    public int getPrazoEmprestimo() {
        return 7;
    }

    public String toCsv() {
        String tipo = "USUARIO";
        if (this instanceof Estudante) {
            tipo = "ESTUDANTE";
        } else if (this instanceof Professor) {
            tipo = "PROFESSOR";
        }
        return tipo + ";" + id + ";" + nome + ";" + email;
    } 

    @Override
    public String toString() {
        return "ID: " + id + " | Nome: " + nome + " | Email: " + email;
    }
}