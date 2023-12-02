package br.ufba.schedulingsimulatorso;

import br.ufba.schedulingsimulatorso.dto.ProcessoDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Queue;

public class SistemaOperacional {

    Queue<Processo> filaProcessos;
    Processo[] processos;
    private String algoEscalonador;
    private String algoPaginacao;

    Disco disco;
    Ram ram;
    Escalonador escalonador = new Escalonador();

    Paginacao paginacao;
    public static void main(String[] args) {

    }

    public SistemaOperacional(List<ProcessoDTO> prc, String algoEscalonador, String algoPaginacao) {
        this.criarProcessos(prc);
        this.algoEscalonador = algoEscalonador;
        this.algoPaginacao = algoPaginacao;
        this.paginacao = new Paginacao(algoPaginacao);
        this.disco = new Disco(List.of(processos));

        this.ram = new Ram();
    }

    public void criarProcessos(List<ProcessoDTO> processosDto){
        this.processos = new Processo[processosDto.size()];
        for (int i = 0; i < processosDto.size(); i++) {
            ProcessoDTO processoDto = processosDto.get(i);
            this.processos[i] = new Processo(
                    i,
                    processoDto.gettChegada(),
                    processoDto.gettExecucao(),
                    processoDto.getDeadline(),
                    processoDto.getPrioridade(),
                    processoDto.getQuantum(),
                    processoDto.getSobrecarga(),
                    processoDto.getQntdPaginas()
            );
        }


    }

    public void executarProcessos(SimpMessagingTemplate messagingTemplate) {
        if(this.algoEscalonador.equals(Algoritmos.FIFO)){
            escalonador.escalonamentoFIFO(disco.getProcessos(), messagingTemplate, disco, ram, paginacao);
        } else if (this.algoEscalonador.equals(Algoritmos.SJF)) {
            escalonador.escalonamentoSJF(disco.getProcessos(), messagingTemplate, disco, ram, paginacao);
        } else if (this.algoEscalonador.equals(Algoritmos.RoundRobin)) {
            escalonador.escalonamentoRoundRobin(disco.getProcessos(), messagingTemplate, disco, ram, paginacao);
        } else if (this.algoEscalonador.equals(Algoritmos.EDF)) {
            escalonador.escalonamentoEDF(disco.getProcessos(), messagingTemplate, disco, ram, paginacao);
        }

    }


    public void setEscalonador(String algoEscalonador) {
        this.algoEscalonador = algoEscalonador;
    }
    public void setPaginacao(String algoPaginacao) {
        this.algoPaginacao = algoPaginacao;
    }
}
