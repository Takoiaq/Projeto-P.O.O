// Emprestimo.java
package ATV;

import java.time.LocalDate;

public class Emprestimo {
    private Livro livro;
    private Usuario usuario;
    private LocalDate dataEmp;
    private LocalDate dataPrev;

    public Emprestimo(Livro livro, Usuario usuario, LocalDate dataEmp, LocalDate dataPrev) {
        this.livro = livro;
        this.usuario = usuario;
        this.dataEmp = dataEmp;
        this.dataPrev = dataPrev;
    }

    public Livro getLivro() { return livro; }
    public Usuario getUsuario() { return usuario; }
    public LocalDate getDataEmp() { return dataEmp; }
    public LocalDate getDatadev() { return dataPrev; } // apareceu deu bom

    // O método toCsv é o salvamento de emprestimos no arquivo CSV
    public String toCsv() {
        return livro.getCodigo() + ";" + usuario.getId() + ";" + dataEmp + ";" + dataPrev;
    }

    @Override
    public String toString() {
        return "Livro: " + livro.getTitulo() + " | Usuário: " + usuario.getNome() + " | Devolução Prevista: " + dataPrev;
    }
}