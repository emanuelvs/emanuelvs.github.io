package br.ufba.schedulingsimulatorso.dto;

import java.util.Date;

public class GanttEventDTO {
    private String taskName;
    private int startTime;
    private int endTime;

    public GanttEventDTO(String taskName, int startTime, int endTime) {
        this.taskName = taskName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
