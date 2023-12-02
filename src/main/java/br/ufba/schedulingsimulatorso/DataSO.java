package br.ufba.schedulingsimulatorso;

import br.ufba.schedulingsimulatorso.dto.ProcessoDTO;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

public class DataSO {
    int nProcessos;
    String algoritmoEscalonamento;
    String algoritmoPaginacao;

    List<ProcessoDTO> processos;

    public int getnProcessos() {
        return nProcessos;
    }

    public void setnProcessos(int nProcessos) {
        this.nProcessos = nProcessos;
    }

    public String getAlgoritmoEscalonamento() {
        return algoritmoEscalonamento;
    }

    public void setAlgoritmoEscalonamento(String algoritmoEscalonamento) {
        this.algoritmoEscalonamento = algoritmoEscalonamento;
    }

    public String getAlgoritmoPaginacao() {
        return algoritmoPaginacao;
    }

    public void setAlgoritmoPaginacao(String algoritmoPaginacao) {
        this.algoritmoPaginacao = algoritmoPaginacao;
    }

    public List<ProcessoDTO> getProcessos() {
        return processos;
    }

    public void setProcessos(List<ProcessoDTO> processos) {
        this.processos = processos;
    }


}
