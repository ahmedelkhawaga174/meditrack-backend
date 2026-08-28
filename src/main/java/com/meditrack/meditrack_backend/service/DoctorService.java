package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.DoctorResponse;
import com.meditrack.meditrack_backend.dto.SlotResponse;
import com.meditrack.meditrack_backend.entity.Doctor;
import com.meditrack.meditrack_backend.enums.SlotStatus;
import com.meditrack.meditrack_backend.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public List<DoctorResponse> getAvailableDoctors(Long departmentId, LocalDate date) {

        List<Doctor> doctors = doctorRepository.findAll();

        return doctors.stream()
                .filter(doctor -> departmentId == null ||
                        (doctor.getDepartment() != null && doctor.getDepartment().getId().equals(departmentId)))
                .filter(doctor -> doctor.getAvailabilitySlots() != null && doctor.getAvailabilitySlots().stream()
                        .anyMatch(slot -> slot.getStatus() == SlotStatus.AVAILABLE &&
                                (date == null || slot.getDate().equals(date))))
                .map(doctor -> DoctorResponse.builder()
                        .id(doctor.getId())
                        .firstName(doctor.getFirstName())
                        .lastName(doctor.getLastName())
                        .specialization(doctor.getSpecialization())
                        .departmentId(doctor.getDepartment() != null ? doctor.getDepartment().getId() : null)
                        .departmentName(doctor.getDepartment() != null ? doctor.getDepartment().getName() : null)
                        .availableSlots(
                                doctor.getAvailabilitySlots().stream()
                                        .filter(slot -> slot.getStatus() == SlotStatus.AVAILABLE &&
                                                (date == null || slot.getDate().equals(date)))
                                        .map(slot -> SlotResponse.builder()
                                                .id(slot.getId())
                                                .date(slot.getDate())
                                                .startTime(slot.getStartTime())
                                                .endTime(slot.getEndTime())
                                                .build())
                                        .toList()
                        )
                        .build())
                .toList();
    }


    // view doctor info -> get doctor by id
    public DoctorResponse getDoctorById(long doctorId){
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(()->
                        new RuntimeException("Doctor not found with id "+ doctorId)
                );
    return DoctorResponse.builder()
            .id(doctor.getId())
            .firstName(doctor.getFirstName())
            .lastName(doctor.getLastName())
            .specialization(doctor.getSpecialization())
            .departmentId(
                    doctor.getDepartment() !=null? doctor.getDepartment().getId():null)
            .departmentName(
                    doctor.getDepartment()!=null? doctor.getDepartment().getName():null)
            .availableSlots(
                    doctor.getAvailabilitySlots().stream()
                            .filter(slot -> slot.getStatus() == SlotStatus.AVAILABLE)
                            .map(slot -> SlotResponse.builder()
                                    .id(slot.getId())
                                    .date(slot.getDate())
                                    .startTime(slot.getStartTime())
                                    .endTime(slot.getEndTime())
                                    .build())
                            .toList()
            )
            .build();
    }
}