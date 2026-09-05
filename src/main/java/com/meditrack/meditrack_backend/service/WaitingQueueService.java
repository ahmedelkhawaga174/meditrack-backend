package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.entity.WaitingQueue;
import com.meditrack.meditrack_backend.enums.AppointmentStatus;
import com.meditrack.meditrack_backend.enums.QueueStatus;
import com.meditrack.meditrack_backend.exception.ResourceNotFoundException;
import com.meditrack.meditrack_backend.repository.AppointmentRepository;
import com.meditrack.meditrack_backend.repository.WaitingQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final WaitingQueueRepository waitingQueueRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public WaitingQueue addToQueue(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found"
                        )
                );

        if (appointment.getStatus() != AppointmentStatus.CHECKED_IN) {
            throw new IllegalStateException(
                    "Only checked-in appointments can be added to the waiting queue"
            );
        }

        if (waitingQueueRepository.existsByAppointmentId(appointmentId)) {
            throw new IllegalStateException(
                    "Appointment is already in the waiting queue"
            );
        }

        LocalDate appointmentDate = appointment
                .getSlot()
                .getDate();

        Integer maxPosition =
                waitingQueueRepository.findMaxQueuePositionByDate(
                        appointmentDate
                );

        Integer nextPosition = maxPosition + 1;

        WaitingQueue waitingQueue = WaitingQueue.builder()
                .appointment(appointment)
                .queuePosition(nextPosition)
                .status(QueueStatus.WAITING)
                .build();

        return waitingQueueRepository.save(waitingQueue);
    }


    @Transactional(readOnly = true)
    public List<WaitingQueue> getWaitingQueue() {

        return waitingQueueRepository
                .findAllByOrderByQueuePositionAsc();
    }


    @Transactional(readOnly = true)
    public List<WaitingQueue> getQueueByStatus(
            QueueStatus status
    ) {

        return waitingQueueRepository
                .findByStatusOrderByQueuePositionAsc(status);
    }


    @Transactional
    public WaitingQueue updateQueueStatus(
            Long queueId,
            QueueStatus status
    ) {

        WaitingQueue waitingQueue =
                waitingQueueRepository.findById(queueId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Queue entry not found"
                                )
                        );

        waitingQueue.setStatus(status);

        return waitingQueueRepository.save(waitingQueue);
    }
}