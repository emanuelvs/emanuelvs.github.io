package br.ufba.schedulingsimulatorso;

import br.ufba.schedulingsimulatorso.dto.EventAction;
import br.ufba.schedulingsimulatorso.dto.EventPayload;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Paginacao {
    String algPaginacao;
    List<Pagina> fila = new ArrayList<>();

    public Paginacao(String algPaginacao){
        this.algPaginacao = algPaginacao;
    }

    public void substituicaoPaginasLRU(Processo processo, SimpMessagingTemplate messagingTemplate, Disco disco, Ram ram) {




            for (int i = 0; i < processo.getPaginas().length; i++) {
                Pagina pagina = processo.getPaginas()[i];

                if (!ram.hasPage(pagina)) {
                    if (ram.getFramesLivres().size() > 0) {
                        Integer iVazio = ram.getFrameLivre();
                        ram.adicionarPagina(iVazio, pagina);
                        disco.setAddrTabelaInvertida(processo.getId(), pagina.getNumPagina(), iVazio);

                        fila.add(pagina);
                    } else {
                        Pagina paginaRemovida = encontrarPaginaLRU(fila);
                        System.out.println("Substituição de Página (LRU): Removida Página " + paginaRemovida.getNumPagina() + " do Processo " + paginaRemovida.getIdProcesso());

                        int addrUltimaPagina = disco.getProcessos().get(paginaRemovida.getIdProcesso()).getTabelaInvertida()[paginaRemovida.getNumPagina()];

                        ram.adicionarPagina(addrUltimaPagina, pagina);
                        disco.setAddrTabelaInvertida(processo.getId(), pagina.getNumPagina(), addrUltimaPagina);

                        fila.add(pagina);
                        disco.addPagina(paginaRemovida);
                    }

                    disco.getPagina(pagina);
                    aplicarDelay();
                    messagingTemplate.convertAndSend("/escalonador/app", new EventPayload(EventAction.MEMORY, ram.getMemoria(), disco.getDiscoValues()));
                    System.out.println("Alocada Página " + pagina.getNumPagina() + " do Processo " + pagina.getIdProcesso() + " na Memória RAM");
                } else {
                    Pagina paginaExistente = encontrarPaginaExistente(fila, pagina);
                    if (paginaExistente != null) {
                        paginaExistente.incrementCountAcesso();
                    }
                }


            }

    }

    public static Pagina encontrarPaginaLRU(List<Pagina> memoriaRAM) {
        Pagina paginaLRU = memoriaRAM.get(0);
        for (Pagina pagina : memoriaRAM) {
            if (pagina.getCountAcesso() < paginaLRU.getCountAcesso()) {
                paginaLRU = pagina;
            }
        }
        return paginaLRU;
    }

    public static Pagina encontrarPaginaExistente(List<Pagina> memoriaRAM, Pagina pagina) {
        for (Pagina paginaExistente : memoriaRAM) {
            if (paginaExistente.equals(pagina)) {
                return paginaExistente;
            }
        }
        return null;
    }

    private void paginacaoFIFO(Processo processo, SimpMessagingTemplate messagingTemplate, Disco disco, Ram ram){
        for (int i = 0; i < processo.getPaginas().length; i++) {
            Pagina pagina = processo.getPaginas()[i];

            int pageFault = 0;

            if(!ram.hasPage(pagina)){
                if(ram.getFramesLivres().size() > 0){
                    Integer iVazio = ram.getFrameLivre();
                    ram.adicionarPagina(iVazio, pagina);
                    disco.setAddrTabelaInvertida(processo.getId(), pagina.getNumPagina(), iVazio);

                    fila.add(pagina);
                }else {
                    Pagina ultimaPagina = fila.remove(0);
                    int addrUltimaPagina = disco.getProcessos().get(ultimaPagina.getIdProcesso()).getTabelaInvertida()[ultimaPagina.getNumPagina()];

                    ram.adicionarPagina(addrUltimaPagina, pagina);
                    disco.setAddrTabelaInvertida(processo.getId(), pagina.getNumPagina(), addrUltimaPagina);

                    fila.add(pagina);
                    disco.addPagina(ultimaPagina);
                }

                disco.getPagina(pagina);
                aplicarDelay();
                messagingTemplate.convertAndSend("/escalonador/app", new EventPayload(EventAction.MEMORY, ram.getMemoria(), disco.getDiscoValues()));

            }
        }
    }

    public void paginar(Processo p, SimpMessagingTemplate messagingTemplate, Disco d, Ram r) {
        if(algPaginacao.equals(Algoritmos.FIFO)){
            paginacaoFIFO(p, messagingTemplate, d, r);
        }else if(algPaginacao.equals(Algoritmos.LRU)) {
            substituicaoPaginasLRU(p, messagingTemplate, d, r);
        }
    }

    private void aplicarDelay() {
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
