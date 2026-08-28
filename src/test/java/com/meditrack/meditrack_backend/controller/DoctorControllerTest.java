package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.DoctorResponse;
import com.meditrack.meditrack_backend.dto.SlotResponse;
import com.meditrack.meditrack_backend.service.DoctorService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DoctorController.class)
@AutoConfigureMockMvc(addFilters = false)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DoctorService doctorService;


    @Test
    void shouldGetDoctorById() throws Exception {

        SlotResponse slot = SlotResponse.builder()
                .id(1L)
                .date(LocalDate.of(2026, 8, 30))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(9, 30))
                .build();


        DoctorResponse doctorResponse = DoctorResponse.builder()
                .id(1L)
                .firstName("Abdelrhman")
                .lastName("Ahmed")
                .specialization("Cardiologist")
                .departmentId(1L)
                .departmentName("Cardiology")
                .availableSlots(List.of(slot))
                .build();


        when(doctorService.getDoctorById(1L))
                .thenReturn(doctorResponse);


        mockMvc.perform(
                        get("/api/doctors/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Abdelrhman"))
                .andExpect(jsonPath("$.lastName").value("Ahmed"))
                .andExpect(jsonPath("$.specialization")
                        .value("Cardiologist"))

                .andExpect(jsonPath("$.departmentId").value(1))
                .andExpect(jsonPath("$.departmentName")
                        .value("Cardiology"))

                .andExpect(jsonPath("$.availableSlots.length()")
                        .value(1))

                .andExpect(jsonPath("$.availableSlots[0].id")
                        .value(1))

                .andExpect(jsonPath("$.availableSlots[0].date")
                        .value("2026-08-30"))

                .andExpect(jsonPath("$.availableSlots[0].startTime")
                        .value("09:00:00"))

                .andExpect(jsonPath("$.availableSlots[0].endTime")
                        .value("09:30:00"));
    }

    @Test
    void shouldGetAvailableDoctorsWithQueryParams() throws Exception {
        Long departmentId = 1L;
        LocalDate date = LocalDate.of(2026, 8, 30);

        SlotResponse slot = SlotResponse.builder()
                .id(1L)
                .date(date)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(9, 30))
                .build();

        DoctorResponse doctorResponse = DoctorResponse.builder()
                .id(1L)
                .firstName("Abdelrhman")
                .lastName("Ahmed")
                .specialization("Cardiologist")
                .departmentId(departmentId)
                .departmentName("Cardiology")
                .availableSlots(List.of(slot))
                .build();

        when(doctorService.getAvailableDoctors(departmentId, date))
                .thenReturn(List.of(doctorResponse));

        mockMvc.perform(
                        get("/api/doctors")
                                .param("department", "1")
                                .param("date", "2026-08-30")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Abdelrhman"))
                .andExpect(jsonPath("$[0].lastName").value("Ahmed"))
                .andExpect(jsonPath("$[0].specialization").value("Cardiologist"))
                .andExpect(jsonPath("$[0].departmentId").value(1))
                .andExpect(jsonPath("$[0].departmentName").value("Cardiology"))
                .andExpect(jsonPath("$[0].availableSlots.length()").value(1))
                .andExpect(jsonPath("$[0].availableSlots[0].id").value(1))
                .andExpect(jsonPath("$[0].availableSlots[0].date").value("2026-08-30"))
                .andExpect(jsonPath("$[0].availableSlots[0].startTime").value("09:00:00"))
                .andExpect(jsonPath("$[0].availableSlots[0].endTime").value("09:30:00"));

        verify(doctorService).getAvailableDoctors(departmentId, date);
    }
}