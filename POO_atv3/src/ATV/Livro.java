package ATV;

public class Livro {
    private int codigo;         
    private String titulo;      
    private String autor;       
    private int anop;           
    private boolean disp;       

    
    public String convert() {
        return codigo + ";" + titulo + ";" + autor + ";" + anop + ";" + disp; // 
    }

    public Livro(int codigo, String titulo, String autor, int anop, boolean disp) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.anop = anop;
        this.disp = disp;
    }

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public int getAnop() { return anop; }
    public void setAnop(int anop) { this.anop = anop; }
    public boolean isDisp() { return disp; }
    public void setDisp(boolean disp) { this.disp = disp; }

    @Override
    public String toString() {
        return "Código: " + codigo + " | Título: " + titulo + " | Autor: " + autor + " | Ano: " + anop + " | Disponível: " + (disp ? "Sim" : "Não");
    }
}