package ATV;

import java.time.LocalDate;

public class Emprestimo implements Interface {
    private Livro livro;
    private Usuario usuario;
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;

    public Emprestimo(Livro livro, Usuario usuario, LocalDate dataEmprestimo, LocalDate dataPrevistaDevolucao) {
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo.");
        }

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }

        if (dataEmprestimo == null) {
            throw new IllegalArgumentException("Data de empréstimo não pode ser nula.");
        }

        if (dataPrevistaDevolucao == null) {
            throw new IllegalArgumentException("Data prevista de devolução não pode ser nula.");
        }

        this.livro = livro;
        this.usuario = usuario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public Emprestimo(Livro livro, Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }

        this.livro = livro;
        this.usuario = usuario;
        this.dataEmprestimo = LocalDate.now();
        this.dataPrevistaDevolucao = LocalDate.now().plusDays(usuario.getPrazoEmprestimo());
    }

    public Livro getLivro() {
        return livro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public LocalDate getDataEmp() {
        return dataEmprestimo;
    }

    public LocalDate getDataDev() {
        return dataPrevistaDevolucao;
    }

    public LocalDate getDatadev() {
        return dataPrevistaDevolucao;
    }

    @Override
    public String toCsv() {
        return livro.getCodigo() + ";" +
                usuario.getId() + ";" +
                dataEmprestimo + ";" +
                dataPrevistaDevolucao;
    }

    @Override
    public String toString() {
        return "Empréstimo [Livro: " + livro.getTitulo() +
                " | Usuário: " + usuario.getNome() +
                " | Data empréstimo: " + dataEmprestimo +
                " | Devolução prevista: " + dataPrevistaDevolucao + "]";
    }
}