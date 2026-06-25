package ATV;

public class Bibliotecario extends Usuario {

    public Bibliotecario(int id, String nome, String email) {
        super(id, nome, email);
    }

    @Override
    public int getPrazoEmprestimo() {
        return 14;
    }

    @Override
    public String toCsv() {
        return "BIBLIOTECARIO;" + id + ";" + nome + ";" + email;
    }

    @Override
    public String toString() {
        return "Bibliotecário [ID: " + getId() + " | Nome: " + getNome() + " | Email: " + getEmail() + "]";
    }
}