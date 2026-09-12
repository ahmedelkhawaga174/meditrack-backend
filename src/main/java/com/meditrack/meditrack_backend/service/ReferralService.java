package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.ReferralRequest;
import com.meditrack.meditrack_backend.dto.ReferralResponse;
import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.entity.Doctor;
import com.meditrack.meditrack_backend.entity.Patient;
import com.meditrack.meditrack_backend.entity.Referral;
import com.meditrack.meditrack_backend.enums.ReferralStatus;
import com.meditrack.meditrack_backend.repository.AppointmentRepository;
import com.meditrack.meditrack_backend.repository.DoctorRepository;
import com.meditrack.meditrack_backend.repository.ReferralRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReferralService {

    private final ReferralRepository referralRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    @Transactional
    public ReferralResponse createReferral(ReferralRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + request.getAppointmentId()));

        Doctor targetDoctor = doctorRepository.findById(request.getReferredToDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Target Doctor not found with ID: " + request.getReferredToDoctorId()));

        Referral referral = Referral.builder()
                .appointment(appointment)
                .referredToDoctor(targetDoctor)
                .referralReason(request.getReferralReason())
                .status(ReferralStatus.PENDING)
                .build();

        Referral savedReferral = referralRepository.save(referral);

        return mapToResponse(savedReferral);
    }

    @Transactional(readOnly = true)
    public ReferralResponse getReferralById(Long id) {
        Referral referral = referralRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Referral not found with ID: " + id));

        return mapToResponse(referral);
    }

    @Transactional(readOnly = true)
    public List<ReferralResponse> getPendingReferrals(Long doctorId) {
        List<Referral> pendingList = (doctorId != null)
                ? referralRepository.findByReferredToDoctorIdAndStatus(doctorId, ReferralStatus.PENDING)
                : referralRepository.findByStatus(ReferralStatus.PENDING);

        return pendingList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReferralResponse updateReferralStatus(Long referralId, ReferralStatus newStatus) {
        Referral referral = referralRepository.findById(referralId)
                .orElseThrow(() -> new IllegalArgumentException("Referral not found with ID: " + referralId));

        referral.setStatus(newStatus);
        Referral updatedReferral = referralRepository.save(referral);

        return mapToResponse(updatedReferral);
    }

    private ReferralResponse mapToResponse(Referral referral) {
        Appointment appointment = referral.getAppointment();
        Patient patient = appointment.getPatient();
        Doctor referringDoctor = appointment.getDoctor();
        Doctor referredToDoctor = referral.getReferredToDoctor();

        return ReferralResponse.builder()
                .id(referral.getId())
                .appointmentId(appointment.getId())
                .patientId(patient.getId())
                .patientName(patient.getFirstName() + " " + patient.getLastName())
                .referringDoctorId(referringDoctor.getId())
                .referringDoctorName("Dr. " + referringDoctor.getFirstName() + " " + referringDoctor.getLastName())
                .referredToDoctorId(referredToDoctor.getId())
                .referredToDoctorName("Dr. " + referredToDoctor.getFirstName() + " " + referredToDoctor.getLastName())
                .referralReason(referral.getReferralReason())
                .status(referral.getStatus())
                .createdAt(referral.getCreatedAt())
                .build();
    }
}