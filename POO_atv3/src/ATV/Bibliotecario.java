package ATV;

public class Professor extends Usuario {

    public Professor(int id, String nome, String email) {
        super(id, nome, email);
    }

    @Override
    public int getPrazoEmprestimo() {
        return 14;
    }

    @Override
    public String toCsv() {
        return "PROFESSOR;" + id + ";" + nome + ";" + email;
    }

    @Override
    public String toString() {
        return "Professor [ID: " + getId() + " | Nome: " + getNome() + " | Email: " + getEmail() + "]";
    }
}