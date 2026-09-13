package com.meditrack.meditrack_backend.service;


import com.meditrack.meditrack_backend.dto.PatientPrescriptionResponse;
import com.meditrack.meditrack_backend.dto.PrescriptionRequest;
import com.meditrack.meditrack_backend.dto.PrescriptionResponse;
import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.repository.AppointmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@AllArgsConstructor
public class PrescriptionService {

    private final AppointmentRepository appointmentRepository;

    private final Map<Long, List<PrescriptionResponse>> prescriptions = new ConcurrentHashMap<>();

    private final AtomicLong prescriptionIdGenerator =
            new AtomicLong(1);



    public PrescriptionResponse issueprescription( Long consultationId, PrescriptionRequest request) {
        Appointment appointment = appointmentRepository.findById(consultationId).orElseThrow(() -> new IllegalArgumentException("consultation not found with ID: " + consultationId));

        Long prescriptionId = prescriptionIdGenerator.getAndIncrement();

        PrescriptionResponse response = PrescriptionResponse.builder()
                .prescriptionId(prescriptionId)
                .consultantId(consultationId)
                .patientId(appointment.getPatient().getId())
                .doctorId(appointment.getDoctor().getId())
                .medicineName(request.getMedicationName())
                .dosage(request.getDosage())
                .frequency(request.getFrequency())
                .duration(request.getDuration())
                .issuedDate(LocalDateTime.now())
                .prescribedDoctor(
                        "Dr. "
                                + appointment.getDoctor().getFirstName()
                                + " "
                                + appointment.getDoctor().getLastName()
                )
                .build();

        prescriptions
                .computeIfAbsent(consultationId, id -> new ArrayList<>())
                .add(response);

        // 5. Return response
        return response;

    }

    //view prescription for patient

    public List<PatientPrescriptionResponse> getPatientPrescriptions(
            Long patientId
    ) {


        List<PatientPrescriptionResponse> result = new ArrayList<>();

        for (List<PrescriptionResponse> prescriptionList : prescriptions.values()) {

            for (PrescriptionResponse prescription : prescriptionList) {

                if (prescription.getPatientId().equals(patientId)) {

                    result.add(
                            PatientPrescriptionResponse.builder()
                                    .prescriptionId(
                                            prescription.getPrescriptionId()
                                    )
                                    .medicineName(
                                            prescription.getMedicineName()
                                    )
                                    .dosage(
                                            prescription.getDosage()
                                    )
                                    .frequency(
                                            prescription.getFrequency()
                                    )
                                    .duration(
                                            prescription.getDuration()
                                    )
                                    .issuedDate(
                                            prescription.getIssuedDate()
                                    )
                                    .prescribingDoctor(
                                            prescription.getPrescribedDoctor()
                                    )
                                    .build()
                    );
                }
            }
        }

        return result;
    }




}
