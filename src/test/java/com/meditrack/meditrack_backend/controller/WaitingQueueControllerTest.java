package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.entity.AvailabilitySlot;
import com.meditrack.meditrack_backend.entity.Doctor;
import com.meditrack.meditrack_backend.entity.Patient;
import com.meditrack.meditrack_backend.entity.WaitingQueue;
import com.meditrack.meditrack_backend.enums.QueueStatus;
import com.meditrack.meditrack_backend.service.WaitingQueueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WaitingQueueController.class)
@AutoConfigureMockMvc(addFilters = false)
class WaitingQueueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WaitingQueueService waitingQueueService;

    private WaitingQueue createQueue() {

        Patient patient = new Patient();
        patient.setId(2L);

        Doctor doctor = new Doctor();
        doctor.setId(1L);

        AvailabilitySlot slot = new AvailabilitySlot();
        slot.setId(4L);
        slot.setDate(LocalDate.of(2026, 9, 4));

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setSlot(slot);

        WaitingQueue queue = new WaitingQueue();
        queue.setId(1L);
        queue.setAppointment(appointment);
        queue.setQueuePosition(1);
        queue.setStatus(QueueStatus.WAITING);
        queue.setCheckedInAt(
                LocalDateTime.of(
                        2026,
                        9,
                        4,
                        19,
                        0
                )
        );

        return queue;
    }

    @Test
    void shouldAddAppointmentToQueue() throws Exception {

        WaitingQueue queue = createQueue();

        when(waitingQueueService.addToQueue(1L))
                .thenReturn(queue);

        mockMvc.perform(
                        post("/api/waiting-queue/1")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.appointmentId").value(1))
                .andExpect(jsonPath("$.patientId").value(2))
                .andExpect(jsonPath("$.doctorId").value(1))
                .andExpect(jsonPath("$.queuePosition").value(1))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void shouldGetWaitingQueue() throws Exception {

        WaitingQueue queue = createQueue();

        when(waitingQueueService.getWaitingQueue())
                .thenReturn(List.of(queue));

        mockMvc.perform(
                        get("/api/waiting-queue")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(
                        jsonPath("$[0].status")
                                .value("WAITING")
                );
    }

    @Test
    void shouldGetQueueByStatus() throws Exception {

        WaitingQueue queue = createQueue();

        when(waitingQueueService
                .getQueueByStatus(QueueStatus.WAITING))
                .thenReturn(List.of(queue));

        mockMvc.perform(
                        get("/api/waiting-queue/status/WAITING")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                        .andExpect(
                                jsonPath("$[0].status")
                                        .value("WAITING")
                        );
    }

    @Test
    void shouldUpdateQueueStatus() throws Exception {

        WaitingQueue queue = createQueue();
        queue.setStatus(QueueStatus.CALLED);

        when(waitingQueueService.updateQueueStatus(
                1L,
                QueueStatus.CALLED
        )).thenReturn(queue);

        mockMvc.perform(
                        patch("/api/waiting-queue/1/status")
                                .contentType(
                                        "application/json"
                                )
                                .content("""
                                        {
                                            "status": "CALLED"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(
                        jsonPath("$.status")
                                .value("CALLED")
                );
    }
}