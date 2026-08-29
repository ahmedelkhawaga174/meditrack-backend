package com.meditrack.meditrack_backend.dto;
import com.meditrack.meditrack_backend.enums.AppointmentStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public class PatientResponse {
    private Long appointmentId;
    private String doctorName;
    private String specialization;
    private String DepartmentName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private AppointmentStatus status;
    private String notes;


    public PatientResponse(){

    }

    public PatientResponse(
            Long appointmentId,
            String doctorName,
            String specialization,
            String departmentName,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            AppointmentStatus status,
            String notes
    ){
        this.appointmentId = appointmentId;
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.DepartmentName = departmentName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.notes = notes;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }
}
