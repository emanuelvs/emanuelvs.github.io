package br.ufba.schedulingsimulatorso;

import java.util.ArrayList;
import java.util.List;

public class Processo{

    private int id;
    private int tChegada;
    private int tExecucao;
    private int deadline;
    private int prioridade;
    private int quantum;
    private int sobrecarga;

    private Pagina[] paginas;

    private int[] tabelaInvertida;

    int qntdPaginas;

    public Processo(int id,int tChegada, int tExecucao, int deadline, int prioridade, int quantum, int sobrecarga, int qntdPaginas) {
        this.id = id;
        this.tChegada = tChegada;
        this.tExecucao = tExecucao;
        this.deadline = deadline;
        this.prioridade = prioridade;
        this.quantum = quantum;
        this.sobrecarga = sobrecarga;
        this.qntdPaginas = qntdPaginas;
        this.paginas = new Pagina[qntdPaginas];
        for (int i = 0; i < qntdPaginas; i++) {
            this.paginas[i] = new Pagina(id, i);
        }
        this.tabelaInvertida = new int[qntdPaginas];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int gettChegada() {
        return tChegada;
    }

    public void settChegada(int tChegada) {
        this.tChegada = tChegada;
    }

    public int gettExecucao() {
        return tExecucao;
    }

    public void settExecucao(int tExecucao) {
        this.tExecucao = tExecucao;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public int getSobrecarga() {
        return sobrecarga;
    }

    public void setSobrecarga(int sobrecarga) {
        this.sobrecarga = sobrecarga;
    }

    public Pagina[] getPaginas() {
        return paginas;
    }

    public void setPaginas(Pagina[] paginas) {
        this.paginas = paginas;
    }

    public int getQntdPaginas() {
        return qntdPaginas;
    }

    public void setQntdPaginas(int qntdPaginas) {
        this.qntdPaginas = qntdPaginas;
    }

    public int[] getTabelaInvertida() {
        return tabelaInvertida;
    }

    public void addAddrTabelaIvertida(int pagina, int addr) {
        this.tabelaInvertida[pagina] = addr;
    }

    public void setTabelaInvertida(int[] tabelaInvertida) {
        this.tabelaInvertida = tabelaInvertida;
    }
}
