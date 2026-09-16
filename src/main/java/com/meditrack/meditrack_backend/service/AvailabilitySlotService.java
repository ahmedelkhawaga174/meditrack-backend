package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.CreateAvailabilityRequest;
import com.meditrack.meditrack_backend.dto.SlotResponse;
import com.meditrack.meditrack_backend.entity.AvailabilitySlot;
import com.meditrack.meditrack_backend.entity.Doctor;
import com.meditrack.meditrack_backend.enums.SlotStatus;
import com.meditrack.meditrack_backend.exception.ResourceNotFoundException;
import com.meditrack.meditrack_backend.repository.AvailabilitySlotRepository;
import com.meditrack.meditrack_backend.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilitySlotService {

    private final AvailabilitySlotRepository availabilitySlotRepository;
    private final DoctorRepository doctorRepository;

    // =====================================================
    // CREATE AVAILABILITY
    // =====================================================

    @Transactional
    public List<SlotResponse> createAvailability(
            Long doctorId,
            CreateAvailabilityRequest request
    ) {

        Doctor doctor =
                doctorRepository.findById(doctorId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found with id: " + doctorId
                                ));

        validateRequest(request);

        List<AvailabilitySlot> slots = new ArrayList<>();

        LocalTime currentTime = request.getStartTime();

        while (currentTime.isBefore(request.getEndTime())) {

            LocalTime slotEnd =
                    currentTime.plusMinutes(
                            request.getSlotDurationMinutes()
                    );

            if (slotEnd.isAfter(request.getEndTime())) {
                break;
            }

            boolean alreadyExists =
                    availabilitySlotRepository
                            .existsByDoctorIdAndDateAndStartTime(
                                    doctorId,
                                    request.getDate(),
                                    currentTime
                            );

            if (!alreadyExists) {

                AvailabilitySlot slot =
                        AvailabilitySlot.builder()
                                .doctor(doctor)
                                .date(request.getDate())
                                .startTime(currentTime)
                                .endTime(slotEnd)
                                .status(SlotStatus.AVAILABLE)
                                .build();

                slots.add(slot);
            }

            currentTime = slotEnd;
        }

        if (slots.isEmpty()) {
            throw new IllegalStateException(
                    "No new availability slots were created"
            );
        }

        return availabilitySlotRepository
                .saveAll(slots)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET DOCTOR AVAILABILITY
    // =====================================================

    @Transactional(readOnly = true)
    public List<SlotResponse> getDoctorAvailability(
            Long doctorId
    ) {

        doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: " + doctorId
                        ));

        return availabilitySlotRepository
                .findByDoctorIdOrderByDateAscStartTimeAsc(doctorId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // DELETE AVAILABLE SLOT
    // =====================================================

    @Transactional
    public void deleteAvailableSlot(
            Long doctorId,
            Long slotId
    ) {

        AvailabilitySlot slot =
                availabilitySlotRepository.findById(slotId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Slot not found with id: " + slotId
                                ));

        if (!slot.getDoctor().getId().equals(doctorId)) {
            throw new IllegalArgumentException(
                    "This slot does not belong to this doctor"
            );
        }

        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new IllegalStateException(
                    "Only available slots can be deleted"
            );
        }

        availabilitySlotRepository.delete(slot);
    }

    // =====================================================
    // VALIDATION
    // =====================================================

    private void validateRequest(
            CreateAvailabilityRequest request
    ) {

        if (request.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Availability date cannot be in the past"
            );
        }

        if (!request.getStartTime()
                .isBefore(request.getEndTime())) {

            throw new IllegalArgumentException(
                    "Start time must be before end time"
            );
        }

        if (request.getSlotDurationMinutes() <= 0) {
            throw new IllegalArgumentException(
                    "Slot duration must be greater than zero"
            );
        }
    }

    // =====================================================
    // MAPPING
    // =====================================================

    private SlotResponse mapToResponse(
            AvailabilitySlot slot
    ) {

        return SlotResponse.builder()
                .id(slot.getId())
                .date(slot.getDate())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .build();
    }
}