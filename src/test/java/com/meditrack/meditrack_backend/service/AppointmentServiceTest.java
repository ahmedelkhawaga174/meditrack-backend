package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.entity.*;
import com.meditrack.meditrack_backend.enums.AppointmentStatus;
import com.meditrack.meditrack_backend.enums.QueueStatus;
import com.meditrack.meditrack_backend.enums.SlotStatus;
import com.meditrack.meditrack_backend.exception.ResourceNotFoundException;
import com.meditrack.meditrack_backend.repository.AppointmentRepository;
import com.meditrack.meditrack_backend.repository.AvailabilitySlotRepository;
import com.meditrack.meditrack_backend.repository.DoctorRepository;
import com.meditrack.meditrack_backend.repository.PatientRepository;
import com.meditrack.meditrack_backend.repository.WaitingQueueRepository;
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

    @Mock
    private WaitingQueueRepository waitingQueueRepository;

    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentService(
                appointmentRepository,
                patientRepository,
                doctorRepository,
                availabilitySlotRepository,
                waitingQueueRepository
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

    @Test
    void shouldCheckInPatientSuccessfully() {

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        when(appointmentRepository.findById(1L))
                .thenReturn(Optional.of(appointment));

        when(waitingQueueRepository.findByAppointmentId(1L))
                .thenReturn(Optional.empty());

        when(appointmentRepository.save(any(Appointment.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        /*
         * Current AppointmentService uses:
         *
         * findTopByOrderByQueueOrderDesc()
         *
         * to find the last patient in the queue.
         *
         * Returning empty means this is the first patient,
         * so the new queue order must be 1.
         */
        when(waitingQueueRepository.findTopByOrderByQueueOrderDesc())
                .thenReturn(Optional.empty());

        when(waitingQueueRepository.save(any(WaitingQueueEntry.class)))
                .thenAnswer(invocation -> {
                    WaitingQueueEntry entry =
                            invocation.getArgument(0);

                    entry.setId(1L);

                    return entry;
                });

        Appointment result =
                appointmentService.checkInPatient(1L);

        assertNotNull(result);

        assertEquals(
                AppointmentStatus.CHECKED_IN,
                result.getStatus()
        );

        verify(appointmentRepository).findById(1L);

        verify(appointmentRepository)
                .save(appointment);

        verify(waitingQueueRepository)
                .findByAppointmentId(1L);

        verify(waitingQueueRepository)
                .findTopByOrderByQueueOrderDesc();

        verify(waitingQueueRepository)
                .save(argThat(entry ->
                        entry.getAppointment().equals(appointment)
                                && entry.getStatus() == QueueStatus.WAITING
                                && entry.getQueueOrder() == 1
                ));
    }

    @Test
    void shouldCheckInPatientAfterExistingPatient() {

        Appointment appointment = new Appointment();
        appointment.setId(2L);
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        WaitingQueueEntry existingEntry =
                new WaitingQueueEntry();

        existingEntry.setId(1L);
        existingEntry.setQueueOrder(3);
        existingEntry.setStatus(QueueStatus.WAITING);

        when(appointmentRepository.findById(2L))
                .thenReturn(Optional.of(appointment));

        when(waitingQueueRepository.findByAppointmentId(2L))
                .thenReturn(Optional.empty());

        when(appointmentRepository.save(any(Appointment.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        when(waitingQueueRepository.findTopByOrderByQueueOrderDesc())
                .thenReturn(Optional.of(existingEntry));

        when(waitingQueueRepository.save(any(WaitingQueueEntry.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        Appointment result =
                appointmentService.checkInPatient(2L);

        assertNotNull(result);

        assertEquals(
                AppointmentStatus.CHECKED_IN,
                result.getStatus()
        );

        verify(waitingQueueRepository)
                .findTopByOrderByQueueOrderDesc();

        verify(waitingQueueRepository)
                .save(argThat(entry ->
                        entry.getAppointment().equals(appointment)
                                && entry.getStatus() == QueueStatus.WAITING
                                && entry.getQueueOrder() == 4
                ));
    }

    @Test
    void shouldRejectCheckInWhenAppointmentIsNotConfirmed() {

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.CANCELLED);

        when(appointmentRepository.findById(1L))
                .thenReturn(Optional.of(appointment));

        assertThrows(
                IllegalArgumentException.class,
                () -> appointmentService.checkInPatient(1L)
        );

        verify(appointmentRepository, never())
                .save(any(Appointment.class));

        verifyNoInteractions(waitingQueueRepository);
    }

    @Test
    void shouldRejectCheckInWhenPatientIsAlreadyInQueue() {

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        WaitingQueueEntry existingEntry =
                new WaitingQueueEntry();

        existingEntry.setId(10L);
        existingEntry.setAppointment(appointment);
        existingEntry.setStatus(QueueStatus.WAITING);
        existingEntry.setQueueOrder(1);

        when(appointmentRepository.findById(1L))
                .thenReturn(Optional.of(appointment));

        when(waitingQueueRepository.findByAppointmentId(1L))
                .thenReturn(Optional.of(existingEntry));

        assertThrows(
                IllegalArgumentException.class,
                () -> appointmentService.checkInPatient(1L)
        );

        verify(appointmentRepository, never())
                .save(any(Appointment.class));

        verify(waitingQueueRepository, never())
                .save(any(WaitingQueueEntry.class));

        verify(waitingQueueRepository)
                .findByAppointmentId(1L);

        verify(waitingQueueRepository, never())
                .findTopByOrderByQueueOrderDesc();
    }
}