package br.ufba.schedulingsimulatorso;

import br.ufba.schedulingsimulatorso.dto.EventAction;
import br.ufba.schedulingsimulatorso.dto.EventPayload;
import br.ufba.schedulingsimulatorso.dto.GanttEventDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;

public class Escalonador {
    public void escalonamentoFIFO(List<Processo> processosDto, SimpMessagingTemplate messagingTemplate, Disco disco, Ram ram, Paginacao paginacao) {
        List<Processo> processos = new ArrayList<>(processosDto);
        processos.sort(Comparator.comparingInt(Processo::gettChegada));

        int tempoTotal = 0;
        int turnaroundTotal = 0;


        for (Processo processo : processos) {
            int espera = Math.max(0, tempoTotal - processo.gettChegada());
            int turnaround = espera + processo.gettExecucao();
            turnaroundTotal += turnaround;
            int inicioExecucao = tempoTotal;


            tempoTotal += processo.gettExecucao() + processo.getSobrecarga();
            //aplicarDelay();
            paginacao.paginar(processo, messagingTemplate, disco, ram);

            int fimLiberacao = tempoTotal;
            GanttEventDTO liberacao = new GanttEventDTO("P" + processo.getId(), inicioExecucao, fimLiberacao);
            messagingTemplate.convertAndSend("/escalonador/app", new EventPayload(EventAction.GANTT, liberacao));

        }

        double turnaroundMedio = (double) turnaroundTotal / processos.size();
        messagingTemplate.convertAndSend("/escalonador/app", new EventPayload(EventAction.TURNAROUND, turnaroundMedio));
        System.out.println("Turnaround Médio (FIFO): " + turnaroundMedio);
    }


    public void escalonamentoSJF(List<Processo> processos, SimpMessagingTemplate messagingTemplate, Disco disco, Ram ram, Paginacao paginacao) {
        List<Processo> processosRestantes = new ArrayList<>(processos);
        Collections.sort(processosRestantes, Comparator.comparingInt(Processo::gettChegada));

        int tempoTotal = 0;
        int turnaroundTotal = 0;

        while (!processosRestantes.isEmpty()) {
            Processo menorDuracao = Collections.min(processosRestantes, Comparator.comparingInt(Processo::gettExecucao));
            processosRestantes.remove(menorDuracao);

            int espera = Math.max(0, tempoTotal - menorDuracao.gettChegada());
            int turnaround = espera + menorDuracao.gettExecucao();
            turnaroundTotal += turnaround;

            int inicioExecucao = tempoTotal;
            tempoTotal += menorDuracao.gettExecucao() + menorDuracao.getSobrecarga();

            int fimExecucao = tempoTotal;
            paginacao.paginar(menorDuracao, messagingTemplate, disco, ram);

            GanttEventDTO liberacao = new GanttEventDTO("P" + menorDuracao.getId(), inicioExecucao, fimExecucao);
            messagingTemplate.convertAndSend("/escalonador/app", new EventPayload(EventAction.GANTT, liberacao));

        }

        double turnaroundMedio = (double) turnaroundTotal / processos.size();
        messagingTemplate.convertAndSend("/escalonador/app", new EventPayload(EventAction.TURNAROUND, turnaroundMedio));
        System.out.println("Turnaround Médio (SJF): " + turnaroundMedio);
    }


    public void escalonamentoRoundRobin(List<Processo> processos, SimpMessagingTemplate messagingTemplate, Disco disco, Ram ram, Paginacao paginacao) {
        List<Processo> processosRestantes = new ArrayList<>(processos);

        int tempoTotal = 0;
        int turnaroundTotal = 0;

        while (!processosRestantes.isEmpty()) {
            Iterator<Processo> iterator = processosRestantes.iterator();
            while (iterator.hasNext()) {
                Processo processo = iterator.next();
                int quantumAtual = Math.min(processo.getQuantum(), processo.gettExecucao());
                int espera = Math.max(0, tempoTotal - processo.gettChegada());
                int turnaround = espera + quantumAtual;
                turnaroundTotal += turnaround;

                int inicioExecucao = tempoTotal;
                tempoTotal += quantumAtual;


                processo.settExecucao((processo.gettExecucao() + processo.getSobrecarga()) - quantumAtual);
                processo.setSobrecarga(0);
                if (processo.gettExecucao() <= 0) {
                    tempoTotal += processo.getSobrecarga();
                    processosRestantes.remove(processo);
                }


                int fimExecucao = tempoTotal;

                paginacao.paginar(processo, messagingTemplate, disco, ram);


                GanttEventDTO execucao = new GanttEventDTO("P" + processo.getId(), inicioExecucao, fimExecucao);
                messagingTemplate.convertAndSend("/escalonador/app", new EventPayload(EventAction.GANTT, execucao));

            }
        }

        double turnaroundMedio = (double) turnaroundTotal / processos.size();
        messagingTemplate.convertAndSend("/escalonador/app", new EventPayload(EventAction.TURNAROUND, turnaroundMedio));
        System.out.println("Turnaround Médio (Round Robin): " + turnaroundMedio);
    }


    public void escalonamentoEDF(List<Processo> processosDto, SimpMessagingTemplate messagingTemplate, Disco disco, Ram ram, Paginacao paginacao) {
        List<Processo> processos = new ArrayList<>(processosDto);
        processos.sort(Comparator.comparingInt(Processo::getDeadline));
        //Collections.sort(processos, Comparator.comparingInt(Processo::getDeadline));

        int tempoTotal = 0;
        int turnaroundTotal = 0;

        for (Processo processo : processos) {
            int espera = Math.max(0, tempoTotal - processo.gettChegada());
            int turnaround = espera + processo.gettExecucao();
            turnaroundTotal += turnaround;

            int inicioExecucao = tempoTotal;
            tempoTotal += processo.gettExecucao() + processo.getSobrecarga();
            int fimExecucao = tempoTotal;
            paginacao.paginar(processo, messagingTemplate, disco, ram);
            GanttEventDTO execucao = new GanttEventDTO("P" + processo.getId(), inicioExecucao, fimExecucao);
            messagingTemplate.convertAndSend("/escalonador/app", new EventPayload(EventAction.GANTT, execucao));
            //aplicarDelay();
        }

        double turnaroundMedio = (double) turnaroundTotal / processos.size();
        messagingTemplate.convertAndSend("/escalonador/app", new EventPayload(EventAction.TURNAROUND, turnaroundMedio));
        System.out.println("Turnaround Médio (EDF): " + turnaroundMedio);
    }

    private void aplicarDelay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
