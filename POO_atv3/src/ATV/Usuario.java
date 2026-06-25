package ATV;

public class Usuario {
    private int id; 
    private String nome; 
    private String email;

    public Usuario(int id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public int getId() { 
        return id; 
    }
    
    public String getNome() { 
        return nome; 
    }
    
    public String getEmail() { 
        return email; 
    }

    public String toCsv() {
        return id + ";" + nome + ";" + email;
    } 

    @Override
    public String toString() {
        return "ID: " + id + " | Nome: " + nome + " | Email: " + email;
    }
}