package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.entity.*;
import com.meditrack.meditrack_backend.enums.AppointmentStatus;
import com.meditrack.meditrack_backend.enums.SlotStatus;
import com.meditrack.meditrack_backend.exception.ResourceNotFoundException;
import com.meditrack.meditrack_backend.repository.AppointmentRepository;
import com.meditrack.meditrack_backend.repository.AvailabilitySlotRepository;
import com.meditrack.meditrack_backend.repository.DoctorRepository;
import com.meditrack.meditrack_backend.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AvailabilitySlotRepository availabilitySlotRepository;

    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentService(
                appointmentRepository,
                patientRepository,
                doctorRepository,
                availabilitySlotRepository
        );
    }

    @Test
    void shouldBookAppointmentSuccessfully() {

        Patient patient = new Patient();
        patient.setId(1L);

        Doctor doctor = new Doctor();
        doctor.setId(1L);

        AvailabilitySlot slot = new AvailabilitySlot();
        slot.setId(1L);
        slot.setDoctor(doctor);
        slot.setStatus(SlotStatus.AVAILABLE);

        when(patientRepository.findById(1L))
                .thenReturn(Optional.of(patient));

        when(doctorRepository.findById(1L))
                .thenReturn(Optional.of(doctor));

        when(availabilitySlotRepository.findByIdAndStatus(
                1L,
                SlotStatus.AVAILABLE
        )).thenReturn(Optional.of(slot));

        when(appointmentRepository.save(any(Appointment.class)))
                .thenAnswer(invocation -> {
                    Appointment appointment = invocation.getArgument(0);
                    appointment.setId(1L);
                    return appointment;
                });

        Appointment result = appointmentService.bookAppointment(
                1L,
                1L,
                1L,
                "First consultation"
        );

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(patient, result.getPatient());
        assertEquals(doctor, result.getDoctor());
        assertEquals(slot, result.getSlot());
        assertEquals(
                AppointmentStatus.CONFIRMED,
                result.getStatus()
        );
        assertEquals(
                SlotStatus.BOOKED,
                slot.getStatus()
        );

        verify(appointmentRepository).save(any(Appointment.class));
        verify(availabilitySlotRepository).save(slot);
    }

    @Test
    void shouldThrowExceptionWhenPatientNotFound() {

        when(patientRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> appointmentService.bookAppointment(
                        999L,
                        1L,
                        1L,
                        "Test"
                )
        );

        verify(doctorRepository, never()).findById(anyLong());
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenDoctorNotFound() {

        Patient patient = new Patient();
        patient.setId(1L);

        when(patientRepository.findById(1L))
                .thenReturn(Optional.of(patient));

        when(doctorRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> appointmentService.bookAppointment(
                        1L,
                        999L,
                        1L,
                        "Test"
                )
        );

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenSlotNotAvailable() {

        Patient patient = new Patient();
        patient.setId(1L);

        Doctor doctor = new Doctor();
        doctor.setId(1L);

        when(patientRepository.findById(1L))
                .thenReturn(Optional.of(patient));

        when(doctorRepository.findById(1L))
                .thenReturn(Optional.of(doctor));

        when(availabilitySlotRepository.findByIdAndStatus(
                1L,
                SlotStatus.AVAILABLE
        )).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> appointmentService.bookAppointment(
                        1L,
                        1L,
                        1L,
                        "Test"
                )
        );

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenSlotBelongsToAnotherDoctor() {

        Patient patient = new Patient();
        patient.setId(1L);

        Doctor selectedDoctor = new Doctor();
        selectedDoctor.setId(1L);

        Doctor slotDoctor = new Doctor();
        slotDoctor.setId(2L);

        AvailabilitySlot slot = new AvailabilitySlot();
        slot.setId(1L);
        slot.setDoctor(slotDoctor);
        slot.setStatus(SlotStatus.AVAILABLE);

        when(patientRepository.findById(1L))
                .thenReturn(Optional.of(patient));

        when(doctorRepository.findById(1L))
                .thenReturn(Optional.of(selectedDoctor));

        when(availabilitySlotRepository.findByIdAndStatus(
                1L,
                SlotStatus.AVAILABLE
        )).thenReturn(Optional.of(slot));

        assertThrows(
                IllegalArgumentException.class,
                () -> appointmentService.bookAppointment(
                        1L,
                        1L,
                        1L,
                        "Test"
                )
        );

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void shouldReturnAppointmentWhenFound() {

        Appointment appointment = new Appointment();
        appointment.setId(1L);

        when(appointmentRepository.findById(1L))
                .thenReturn(Optional.of(appointment));

        Appointment result =
                appointmentService.getAppointment(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(appointmentRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenAppointmentNotFound() {

        when(appointmentRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> appointmentService.getAppointment(999L)
        );
    }
}