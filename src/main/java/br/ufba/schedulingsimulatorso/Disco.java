package br.ufba.schedulingsimulatorso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Disco {

    private final int TAMANHO_PAGINA = 4 * 1024;
    private final int TAMANHO_DISCO = 400 * 1024;

    private final int CAPACIDADE_DISCO = TAMANHO_DISCO/TAMANHO_PAGINA;
    private List<Processo> processos;
    private Map<Integer, Pagina[]> memoria = new HashMap<Integer, Pagina[]>();

    //private static Disco instance;
    public Disco(List<Processo> processos) {
        this.processos = processos;
        this.montarDisco(processos);
    };

    public void montarDisco(List<Processo> prc) {
        for (Processo p:
             prc) {
            addProcesso(p);
        }
    }

    public void addPagina(Pagina pagina){
        Pagina[] pagDto = memoria.get(pagina.getIdProcesso());
        pagDto[pagina.getNumPagina()] = pagina;
        this.memoria.replace(pagina.getIdProcesso(), pagDto);
    }

    public void addProcesso(Processo processo){
        if(this.memoria.size() <= 10) {
            Pagina[] pags = new Pagina[processo.qntdPaginas];
            for (int i = 0; i < processo.qntdPaginas; i++) {
                Pagina pag = new Pagina(processo.getId(), i);
                pags[i] = pag;
            }

            this.memoria.put(processo.getId(), pags);
        }
    }

    public Pagina getPagina(Pagina page) {
        Pagina[] pagsDto = memoria.get(page.getIdProcesso());
        Pagina pag = pagsDto[page.getNumPagina()];
        pagsDto[page.getNumPagina()] = null;
        this.memoria.replace(page.getIdProcesso(), pagsDto);
        return pag;
    }

    public Map<Integer, Pagina[]> getMemoria() {
        return memoria;
    }

    public void setMemoria(Map<Integer, Pagina[]> memoria) {
        this.memoria = memoria;
    }

    public List<Processo> getProcessos() {
        return this.processos;
    }

    public int getCAPACIDADE_DISCO() {
        return CAPACIDADE_DISCO;
    }

    public void setAddrTabelaInvertida(int pid, int nPagina, int addr) {
        this.processos.get(pid).addAddrTabelaIvertida(nPagina, addr);
    }

    public Pagina[] getDiscoValues() {
        Pagina[] paginaList = new Pagina[getCAPACIDADE_DISCO()];
        int index = 0;
        for (Pagina[] pgs:
                memoria.values()) {

            for (Pagina p : pgs) {
                paginaList[index] = p;
                index++;
            }
        }
        return paginaList;
    }
}
