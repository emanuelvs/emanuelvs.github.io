package br.ufba.schedulingsimulatorso.rest;

// Importações necessárias para a simulação
import br.ufba.schedulingsimulatorso.DataSO;
import br.ufba.schedulingsimulatorso.SistemaOperacional;
import br.ufba.schedulingsimulatorso.dto.GanttEventDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// ... (restante do código)

@RestController
@RequestMapping("/api/gantt")
public class GanttResources {

    SistemaOperacional sistemaOperacional;
    DataSO cacheDataSO = new DataSO();

    private final SimpMessagingTemplate messagingTemplate;

    public GanttResources(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/escalonar")
    public ResponseEntity<String> escalonarProcessos(@RequestBody DataSO requisicao) {

        System.out.println("Número de Processos: " + requisicao.getnProcessos());
        System.out.println("Algoritmo de Escalonamento: " + requisicao.getAlgoritmoEscalonamento());
        System.out.println("Algoritmo de Paginação: " + requisicao.getAlgoritmoPaginacao());
        System.out.println("Processos: " + requisicao.getProcessos());

        sistemaOperacional = new SistemaOperacional(requisicao.getProcessos(), requisicao.getAlgoritmoEscalonamento(), requisicao.getAlgoritmoPaginacao());

        sistemaOperacional.executarProcessos(messagingTemplate);

        return new ResponseEntity<>("Processos estão sendo executados!", HttpStatus.OK);
    }


}
