package br.ufba.schedulingsimulatorso.dto;

import br.ufba.schedulingsimulatorso.Pagina;

import java.util.List;

public class EventPayload {
    EventAction action;

    GanttEventDTO ganttEventDTO;
    Pagina[] ram;
    Pagina[] disco;

    double turnaroundMedio;

    public EventPayload(EventAction action, GanttEventDTO ganttEventDTO, Pagina[] ram, Pagina[] disco) {
        this.action = action;
        this.ganttEventDTO = ganttEventDTO;
        this.ram = ram;
        this.disco = disco;
    }

    public EventPayload(EventAction action, GanttEventDTO ganttEventDTO) {
        this.action = action;
        this.ganttEventDTO = ganttEventDTO;
    }

    public EventPayload(EventAction action, Pagina[] ram, Pagina[] disco) {
        this.action = action;
        this.ram = ram;
        this.disco = disco;
    }

    public EventPayload(EventAction action, double turnaroundMedio) {
        this.action = action;
        this.turnaroundMedio = turnaroundMedio;
    }

    public EventAction getAction() {
        return action;
    }

    public void setAction(EventAction action) {
        this.action = action;
    }

    public GanttEventDTO getGanttEventDTO() {
        return ganttEventDTO;
    }

    public void setGanttEventDTO(GanttEventDTO ganttEventDTO) {
        this.ganttEventDTO = ganttEventDTO;
    }

    public Pagina[] getRam() {
        return ram;
    }

    public void setRam(Pagina[] ram) {
        this.ram = ram;
    }

    public Pagina[] getDisco() {
        return disco;
    }

    public void setDisco(Pagina[] disco) {
        this.disco = disco;
    }

    public double getTurnaroundMedio() {
        return turnaroundMedio;
    }

    public void setTurnaroundMedio(double turnaroundMedio) {
        this.turnaroundMedio = turnaroundMedio;
    }
}
