package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.entity.AvailabilitySlot;
import com.meditrack.meditrack_backend.entity.WaitingQueue;
import com.meditrack.meditrack_backend.enums.AppointmentStatus;
import com.meditrack.meditrack_backend.enums.QueueStatus;
import com.meditrack.meditrack_backend.exception.ResourceNotFoundException;
import com.meditrack.meditrack_backend.repository.AppointmentRepository;
import com.meditrack.meditrack_backend.repository.WaitingQueueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaitingQueueServiceTest {

    @Mock
    private WaitingQueueRepository waitingQueueRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    private WaitingQueueService waitingQueueService;

    @BeforeEach
    void setUp() {

        waitingQueueService = new WaitingQueueService(
                waitingQueueRepository,
                appointmentRepository
        );
    }

    @Test
    void shouldAddAppointmentToQueueSuccessfully() {

        AvailabilitySlot slot = new AvailabilitySlot();
        slot.setDate(LocalDate.of(2026, 9, 4));

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.CHECKED_IN);
        appointment.setSlot(slot);

        when(appointmentRepository.findById(1L))
                .thenReturn(Optional.of(appointment));

        when(waitingQueueRepository.existsByAppointmentId(1L))
                .thenReturn(false);

        when(waitingQueueRepository.findMaxQueuePositionByDate(
                LocalDate.of(2026, 9, 4)))
                .thenReturn(2);

        WaitingQueue savedQueue = new WaitingQueue();
        savedQueue.setId(1L);
        savedQueue.setAppointment(appointment);
        savedQueue.setQueuePosition(3);
        savedQueue.setStatus(QueueStatus.WAITING);

        when(waitingQueueRepository.save(any(WaitingQueue.class)))
                .thenReturn(savedQueue);

        WaitingQueue result =
                waitingQueueService.addToQueue(1L);

        assertNotNull(result);
        assertEquals(3, result.getQueuePosition());
        assertEquals(QueueStatus.WAITING, result.getStatus());

        verify(appointmentRepository).findById(1L);
        verify(waitingQueueRepository)
                .existsByAppointmentId(1L);
        verify(waitingQueueRepository)
                .findMaxQueuePositionByDate(
                        LocalDate.of(2026, 9, 4)
                );
        verify(waitingQueueRepository)
                .save(any(WaitingQueue.class));
    }

    @Test
    void shouldThrowExceptionWhenAppointmentNotFound() {

        when(appointmentRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> waitingQueueService.addToQueue(999L)
        );

        verify(waitingQueueRepository, never())
                .save(any(WaitingQueue.class));
    }

    @Test
    void shouldThrowExceptionWhenAppointmentIsNotCheckedIn() {

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        when(appointmentRepository.findById(1L))
                .thenReturn(Optional.of(appointment));

        assertThrows(
                IllegalStateException.class,
                () -> waitingQueueService.addToQueue(1L)
        );

        verify(waitingQueueRepository, never())
                .save(any(WaitingQueue.class));
    }

    @Test
    void shouldThrowExceptionWhenAppointmentAlreadyInQueue() {

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.CHECKED_IN);

        when(appointmentRepository.findById(1L))
                .thenReturn(Optional.of(appointment));

        when(waitingQueueRepository.existsByAppointmentId(1L))
                .thenReturn(true);

        assertThrows(
                IllegalStateException.class,
                () -> waitingQueueService.addToQueue(1L)
        );

        verify(waitingQueueRepository, never())
                .save(any(WaitingQueue.class));
    }

    @Test
    void shouldReturnWaitingQueue() {

        WaitingQueue queue1 = new WaitingQueue();
        queue1.setId(1L);
        queue1.setQueuePosition(1);
        queue1.setStatus(QueueStatus.WAITING);

        WaitingQueue queue2 = new WaitingQueue();
        queue2.setId(2L);
        queue2.setQueuePosition(2);
        queue2.setStatus(QueueStatus.WAITING);

        when(waitingQueueRepository.findAllByOrderByQueuePositionAsc())
                .thenReturn(List.of(queue1, queue2));

        List<WaitingQueue> result =
                waitingQueueService.getWaitingQueue();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getQueuePosition());
        assertEquals(2, result.get(1).getQueuePosition());
    }

    @Test
    void shouldReturnQueueByStatus() {

        WaitingQueue queue = new WaitingQueue();
        queue.setId(1L);
        queue.setQueuePosition(1);
        queue.setStatus(QueueStatus.WAITING);

        when(waitingQueueRepository
                .findByStatusOrderByQueuePositionAsc(
                        QueueStatus.WAITING))
                .thenReturn(List.of(queue));

        List<WaitingQueue> result =
                waitingQueueService.getQueueByStatus(
                        QueueStatus.WAITING
                );

        assertEquals(1, result.size());
        assertEquals(
                QueueStatus.WAITING,
                result.get(0).getStatus()
        );
    }

    @Test
    void shouldUpdateQueueStatus() {

        WaitingQueue queue = new WaitingQueue();
        queue.setId(1L);
        queue.setStatus(QueueStatus.WAITING);

        when(waitingQueueRepository.findById(1L))
                .thenReturn(Optional.of(queue));

        when(waitingQueueRepository.save(queue))
                .thenReturn(queue);

        WaitingQueue result =
                waitingQueueService.updateQueueStatus(
                        1L,
                        QueueStatus.CALLED
                );

        assertEquals(
                QueueStatus.CALLED,
                result.getStatus()
        );

        verify(waitingQueueRepository).findById(1L);
        verify(waitingQueueRepository).save(queue);
    }

    @Test
    void shouldThrowExceptionWhenQueueEntryNotFound() {

        when(waitingQueueRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> waitingQueueService.updateQueueStatus(
                        999L,
                        QueueStatus.CALLED
                )
        );

        verify(waitingQueueRepository, never())
                .save(any(WaitingQueue.class));
    }
}