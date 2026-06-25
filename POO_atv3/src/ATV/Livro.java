package ATV;

public class Livro {
    private int codigo;
    private String titulo;
    private String autor;
    private int anop;
    private boolean disp;

    public Livro(int codigo, String titulo, String autor, int anop, boolean disp) {
        this.codigo = codigo;
        this.titulo = normalizarTexto(titulo);
        this.autor = normalizarTexto(autor);
        this.anop = anop;
        this.disp = disp;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
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

    public boolean isDisp() {
        return disp;
    }

    public void setDisp(boolean disp) {
        this.disp = disp;
    }

    private String normalizarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }

    public String toCsv() {
        return codigo + ";" + titulo + ";" + autor + ";" + anop + ";" + disp;
    }

    @Override
    public String toString() {
        return "Código: " + codigo +
                " | Título: " + titulo +
                " | Autor: " + autor +
                " | Ano: " + anop +
                " | Disponível: " + (disp ? "Sim" : "Não");
    }
}