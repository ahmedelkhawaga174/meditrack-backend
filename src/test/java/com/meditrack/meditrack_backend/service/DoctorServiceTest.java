package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.DoctorResponse;
import com.meditrack.meditrack_backend.entity.AvailabilitySlot;
import com.meditrack.meditrack_backend.entity.Department;
import com.meditrack.meditrack_backend.entity.Doctor;
import com.meditrack.meditrack_backend.enums.SlotStatus;
import com.meditrack.meditrack_backend.repository.DoctorRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    private DoctorService doctorService;


    @BeforeEach
    void setUp() {

        doctorService = new DoctorService(
                doctorRepository
        );
    }


    @Test
    void shouldGetDoctorByIdSuccessfully() {
        Department department = new Department();

        department.setId(1L);
        department.setName("Cardiology");
        department.setDescription("Heart department");

        Doctor doctor = new Doctor();

        doctor.setId(1L);
        doctor.setFirstName("Abdelrhman");
        doctor.setLastName("Ahmed");
        doctor.setSpecialization("Cardiologist");
        doctor.setDepartment(department);

        AvailabilitySlot availableSlot = new AvailabilitySlot();

        availableSlot.setId(1L);
        availableSlot.setDoctor(doctor);
        availableSlot.setDate(
                LocalDate.of(2026, 8, 30)
        );
        availableSlot.setStartTime(
                LocalTime.of(9, 0)
        );
        availableSlot.setEndTime(
                LocalTime.of(9, 30)
        );
        availableSlot.setStatus(
                SlotStatus.AVAILABLE
        );


        AvailabilitySlot bookedSlot = new AvailabilitySlot();

        bookedSlot.setId(2L);
        bookedSlot.setDoctor(doctor);
        bookedSlot.setDate(
                LocalDate.of(2026, 8, 30)
        );
        bookedSlot.setStartTime(
                LocalTime.of(10, 0)
        );
        bookedSlot.setEndTime(
                LocalTime.of(10, 30)
        );
        bookedSlot.setStatus(
                SlotStatus.BOOKED
        );


        doctor.setAvailabilitySlots(
                List.of(
                        availableSlot,
                        bookedSlot
                )
        );


        when(doctorRepository.findById(1L))
                .thenReturn(Optional.of(doctor));


        DoctorResponse result =
                doctorService.getDoctorById(1L);


        assertNotNull(result);

        assertEquals(
                1L,
                result.getId()
        );

        assertEquals(
                "Abdelrhman",
                result.getFirstName()
        );

        assertEquals(
                "Ahmed",
                result.getLastName()
        );

        assertEquals(
                "Cardiologist",
                result.getSpecialization()
        );

        assertEquals(
                1L,
                result.getDepartmentId()
        );

        assertEquals(
                "Cardiology",
                result.getDepartmentName()
        );


        assertNotNull(
                result.getAvailableSlots()
        );

        assertEquals(
                1,
                result.getAvailableSlots().size()
        );


        assertEquals(
                1L,
                result.getAvailableSlots()
                        .get(0)
                        .getId()
        );

        assertEquals(
                LocalDate.of(2026, 8, 30),
                result.getAvailableSlots()
                        .get(0)
                        .getDate()
        );

        assertEquals(
                LocalTime.of(9, 0),
                result.getAvailableSlots()
                        .get(0)
                        .getStartTime()
        );

        assertEquals(
                LocalTime.of(9, 30),
                result.getAvailableSlots()
                        .get(0)
                        .getEndTime()
        );


        verify(
                doctorRepository
        ).findById(1L);
    }


    @Test
    void shouldThrowExceptionWhenDoctorNotFound() {

        when(doctorRepository.findById(999L))
                .thenReturn(Optional.empty());


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> doctorService.getDoctorById(999L)
                );


        assertEquals(
                "Doctor not found with id 999",
                exception.getMessage()
        );

        verify(
                doctorRepository
        ).findById(999L);
    }

    @Test
    void shouldGetAvailableDoctorsWithFiltersSuccessfully() {
        Department department = new Department();
        department.setId(1L);
        department.setName("Cardiology");

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("Abdelrhman");
        doctor.setLastName("Ahmed");
        doctor.setSpecialization("Cardiologist");
        doctor.setDepartment(department);

        LocalDate filterDate = LocalDate.of(2026, 8, 30);

        AvailabilitySlot availableSlot = new AvailabilitySlot();
        availableSlot.setId(1L);
        availableSlot.setDoctor(doctor);
        availableSlot.setDate(filterDate);
        availableSlot.setStartTime(LocalTime.of(9, 0));
        availableSlot.setEndTime(LocalTime.of(9, 30));
        availableSlot.setStatus(SlotStatus.AVAILABLE);

        AvailabilitySlot bookedSlot = new AvailabilitySlot();
        bookedSlot.setId(2L);
        bookedSlot.setDoctor(doctor);
        bookedSlot.setDate(filterDate);
        bookedSlot.setStartTime(LocalTime.of(10, 0));
        bookedSlot.setEndTime(LocalTime.of(10, 30));
        bookedSlot.setStatus(SlotStatus.BOOKED);

        doctor.setAvailabilitySlots(List.of(availableSlot, bookedSlot));

        when(doctorRepository.findAll()).thenReturn(List.of(doctor));

        List<DoctorResponse> result = doctorService.getAvailableDoctors(1L, filterDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(1, result.get(0).getAvailableSlots().size());
        assertEquals(1L, result.get(0).getAvailableSlots().get(0).getId());

        verify(doctorRepository).findAll();
    }

    @Test
    void shouldGetAvailableDoctorsWhenFiltersAreNull() {
        Department department = new Department();
        department.setId(1L);
        department.setName("Cardiology");

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("Abdelrhman");
        doctor.setLastName("Ahmed");
        doctor.setDepartment(department);

        AvailabilitySlot availableSlot = new AvailabilitySlot();
        availableSlot.setId(1L);
        availableSlot.setDate(LocalDate.of(2026, 8, 30));
        availableSlot.setStatus(SlotStatus.AVAILABLE);

        doctor.setAvailabilitySlots(List.of(availableSlot));

        when(doctorRepository.findAll()).thenReturn(List.of(doctor));

        List<DoctorResponse> result = doctorService.getAvailableDoctors(null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(doctorRepository).findAll();
    }
}