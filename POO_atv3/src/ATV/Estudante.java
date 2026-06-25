package ATV;

public class Estudante extends Usuario {
    
    public Estudante(int id, String nome, String email) {
        super(id, nome, email);
    }

    @Override
    public int getPrazoEmprestimo() {
        return 7;
    }

    @Override
    public String toCsv() {
        return "ESTUDANTE;" + id + ";" + nome + ";" + email;
    }

    @Override
    public String toString() {
        return "Estudante [ID: " + getId() + " | Nome: " + getNome() + " | Email: " + getEmail() + "]";
    }
}