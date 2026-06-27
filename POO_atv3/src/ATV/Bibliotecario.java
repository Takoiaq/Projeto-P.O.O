package ATV;

public class Bibliotecario extends Usuario {

    public Bibliotecario(int id, String nome, String email) {
        super(id, nome, email);
    }

    @Override
    public int getPrazoEmprestimo() {
        return 35;
    }

    @Override
    public String getTipo() {
        return "BIBLIOTECARIO";
    }

    @Override
    public String toCsv() {
        return getTipo() + ";" + getId() + ";" + getNome() + ";" + getEmail();
    }

    @Override
    public String toString() {
        return "Bibliotecario [ID: " + getId() +
                " | Nome: " + getNome() +
                " | Email: " + getEmail() +
                " | Prazo: " + getPrazoEmprestimo() + " dias]";
    }
}