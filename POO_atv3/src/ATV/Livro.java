package ATV;

public class Livro {
    private int codigo;
    private String titulo;
    private String autor;
    private int anop;
    private int quantidade;

    public Livro(int codigo, String titulo, String autor, int anop, int quantidade) {
        if (codigo < 0) {
            throw new IllegalArgumentException("Código do livro não pode ser negativo.");
        }

        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade do livro não pode ser negativa.");
        }

        this.codigo = codigo;
        this.titulo = normalizarTexto(titulo);
        this.autor = normalizarTexto(autor);
        this.anop = anop;
        this.quantidade = quantidade;
    }

    public Livro(int codigo, String titulo, String autor, int anop, boolean disp) {
        this(codigo, titulo, autor, anop, disp ? 1 : 0);
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        if (codigo < 0) {
            throw new IllegalArgumentException("Código do livro não pode ser negativo.");
        }

        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = normalizarTexto(titulo);
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = normalizarTexto(autor);
    }

    public int getAnop() {
        return anop;
    }

    public void setAnop(int anop) {
        this.anop = anop;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade do livro não pode ser negativa.");
        }

        this.quantidade = quantidade;
    }

    public boolean isDisp() {
        return quantidade > 0;
    }

    public void emprestarUnidade() {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Não há unidades disponíveis deste livro.");
        }

        quantidade--;
    }

    public void devolverUnidade() {
        quantidade++;
    }

    public void setDisp(boolean disp) {
        if (!disp) {
            this.quantidade = 0;
        } else if (this.quantidade == 0) {
            this.quantidade = 1;
        }
    }

    private String normalizarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }

    public String toCsv() {
        return codigo + ";" + titulo + ";" + autor + ";" + anop + ";" + quantidade;
    }

    @Override
    public String toString() {
        return "Código: " + codigo +
                " | Título: " + titulo +
                " | Autor: " + autor +
                " | Ano: " + anop +
                " | Quantidade disponível: " + quantidade;
    }
}