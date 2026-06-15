import java.time.LocalDate;

public class Emprestimo {

    private Livro livro;
    private Usuario usuario;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;

public Emprestimo(Livro livro, Usuario usuario, LocalDate dataEmprestimo, LocalDate dataDevolucao) {
    this.livro = livro; 
    this.usuario = usuario;
    this.dataEmprestimo = dataEmprestimo;
    this.dataDevolucao = dataDevolucao;
}

public Livro getLivro() {
    return livro;
}

public void setLivro(Livro livro) {
    this.livro = livro;
}

public Usuario getUsuario() {
    return usuario;
}

public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
}

public LocalDate getDataEmprestimo() {
    return dataEmprestimo;
}

public void setDataEmprestimo(LocalDate dataEmprestimo) {
    this.dataEmprestimo = dataEmprestimo;
}

public LocalDate getDataDevolucao() {
    return dataDevolucao;
}

public void setDataDevolucao(LocalDate dataDevolucao) {
    this.dataDevolucao = dataDevolucao;
}
}
