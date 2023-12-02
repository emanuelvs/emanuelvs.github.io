package br.ufba.schedulingsimulatorso.dto;

public class ProcessoDTO {
    int tChegada;
    int tExecucao;
    int deadline;
    int prioridade;
    int quantum;
    int sobrecarga;
    int qntdPaginas;

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

    public int getQntdPaginas() {
        return qntdPaginas;
    }

    public void setQntdPaginas(int qntdPaginas) {
        this.qntdPaginas = qntdPaginas;
    }
}
