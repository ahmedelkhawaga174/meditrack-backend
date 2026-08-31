package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.WaitingQueueResponse;
import com.meditrack.meditrack_backend.entity.WaitingQueueEntry;
import com.meditrack.meditrack_backend.enums.QueueStatus;
import com.meditrack.meditrack_backend.exception.ResourceNotFoundException;
import com.meditrack.meditrack_backend.repository.WaitingQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final WaitingQueueRepository waitingQueueRepository;

    /**
     * Get all waiting queue entries ordered by queue order.
     */
    @Transactional(readOnly = true)
    public List<WaitingQueueResponse> getWaitingQueue() {

        return waitingQueueRepository
                .findAllByOrderByQueueOrderAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Update the status of a waiting queue entry.
     */
    @Transactional
    public WaitingQueueResponse updateStatus(
            Long id,
            QueueStatus status
    ) {

        if (status == null) {
            throw new IllegalArgumentException(
                    "Queue status is required"
            );
        }

        WaitingQueueEntry entry = waitingQueueRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Waiting queue entry not found"
                        ));

        entry.setStatus(status);

        WaitingQueueEntry savedEntry =
                waitingQueueRepository.save(entry);

        return toResponse(savedEntry);
    }

    /**
     * Change the position of a patient in the waiting queue.
     */
    @Transactional
    public WaitingQueueResponse updateOrder(
            Long id,
            Integer newOrder
    ) {

        if (newOrder == null || newOrder < 1) {
            throw new IllegalArgumentException(
                    "Queue order must be greater than 0"
            );
        }

        WaitingQueueEntry entry = waitingQueueRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Waiting queue entry not found"
                        ));

        List<WaitingQueueEntry> queue =
                waitingQueueRepository
                        .findAllByOrderByQueueOrderAsc();

        int oldOrder = entry.getQueueOrder();

        /*
         * Do not allow the requested position to be
         * greater than the number of patients in the queue.
         *
         * final is important because targetOrder is used
         * inside lambda expressions below.
         */
        final int targetOrder =
                Math.min(newOrder, queue.size());

        /*
         * Nothing to change.
         */
        if (oldOrder == targetOrder) {
            return toResponse(entry);
        }

        /*
         * Patient is moving UP in the queue.
         *
         * Example:
         *
         * 1 Ahmed
         * 2 Sara
         * 3 Mostafa
         *
         * Move Mostafa from 3 -> 1
         *
         * Result:
         *
         * 1 Mostafa
         * 2 Ahmed
         * 3 Sara
         */
        if (targetOrder < oldOrder) {

            queue.stream()
                    .filter(item ->
                            !item.getId().equals(id)
                                    && item.getQueueOrder() >= targetOrder
                                    && item.getQueueOrder() < oldOrder
                    )
                    .forEach(item ->
                            item.setQueueOrder(
                                    item.getQueueOrder() + 1
                            )
                    );

        }

        /*
         * Patient is moving DOWN in the queue.
         *
         * Example:
         *
         * 1 Ahmed
         * 2 Sara
         * 3 Mostafa
         *
         * Move Ahmed from 1 -> 3
         *
         * Result:
         *
         * 1 Sara
         * 2 Mostafa
         * 3 Ahmed
         */
        else {

            queue.stream()
                    .filter(item ->
                            !item.getId().equals(id)
                                    && item.getQueueOrder() > oldOrder
                                    && item.getQueueOrder() <= targetOrder
                    )
                    .forEach(item ->
                            item.setQueueOrder(
                                    item.getQueueOrder() - 1
                            )
                    );
        }

        /*
         * Set the selected patient's new position.
         */
        entry.setQueueOrder(targetOrder);

        /*
         * Save the updated queue positions.
         */
        waitingQueueRepository.saveAll(queue);

        WaitingQueueEntry savedEntry =
                waitingQueueRepository.save(entry);

        return toResponse(savedEntry);
    }

    /**
     * Convert WaitingQueueEntry entity to API response DTO.
     */
    private WaitingQueueResponse toResponse(
            WaitingQueueEntry entry
    ) {

        var appointment = entry.getAppointment();
        var patient = appointment.getPatient();
        var doctor = appointment.getDoctor();
        var slot = appointment.getSlot();

        return WaitingQueueResponse.builder()
                .id(entry.getId())

                .appointmentId(appointment.getId())

                .patientId(patient.getId())
                .patientFirstName(patient.getFirstName())
                .patientLastName(patient.getLastName())

                .doctorId(doctor.getId())
                .doctorFirstName(doctor.getFirstName())
                .doctorLastName(doctor.getLastName())

                .appointmentDate(slot.getDate())
                .appointmentStartTime(slot.getStartTime())
                .appointmentEndTime(slot.getEndTime())

                .status(entry.getStatus())
                .queueOrder(entry.getQueueOrder())
                .checkedInAt(entry.getCheckedInAt())

                .build();
    }
}