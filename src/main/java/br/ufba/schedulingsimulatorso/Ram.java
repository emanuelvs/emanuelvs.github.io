package br.ufba.schedulingsimulatorso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Ram {

    int TAMANHO_MEMORIA_RAM = 200 * 1024;
    int TAMANHO_PAGINA = 4 * 1024;
    int CAPACIDADE_RAM = TAMANHO_MEMORIA_RAM/TAMANHO_PAGINA;



    public Ram() {
        montarTabelaFramesLivres();
    }



    private Pagina[] ramM = new Pagina[CAPACIDADE_RAM];
    private List<Integer> tabela_frames_livres = new ArrayList<>();


    public Pagina adicionarPagina(int addr, Pagina p) {
        Pagina paginaRemovida = this.ramM[addr];
        this.ramM[addr] = p;
        return paginaRemovida;
    }

    public void montarTabelaFramesLivres() {
        for (int i = 0; i < CAPACIDADE_RAM; i++) {
            this.tabela_frames_livres.add(i);
        }
    }

    public boolean hasPage(Pagina pagina) {
       return Arrays.stream(ramM).anyMatch(p -> p != null && (p.getIdProcesso() == pagina.getIdProcesso() && p.getNumPagina() == pagina.getNumPagina()));
    }

    public List<Integer> getFramesLivres() {
        return tabela_frames_livres;
    }

    public Integer getFrameLivre() {
        return removerFrameLivre();
    }

    public int removerFrameLivre(){
        return this.tabela_frames_livres.remove(0);
    }

    public Pagina[] getMemoria() {

        return ramM;

    }




}
