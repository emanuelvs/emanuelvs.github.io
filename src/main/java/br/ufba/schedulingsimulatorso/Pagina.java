package br.ufba.schedulingsimulatorso;

public class Pagina {
    private int idProcesso;
    private int numPagina;
    private int countAcesso;

    public Pagina(int idProcesso, int numPagina) {
        this.idProcesso = idProcesso;
        this.numPagina = numPagina;
        this.countAcesso = 0;
    }

    public int getIdProcesso() {
        return idProcesso;
    }

    public int getNumPagina() {
        return numPagina;
    }

    public int getCountAcesso() {
        return countAcesso;
    }

    public void incrementCountAcesso() {
        countAcesso++;
    }
}
