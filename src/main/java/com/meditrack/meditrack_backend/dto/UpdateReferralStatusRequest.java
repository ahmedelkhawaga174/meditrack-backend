package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.enums.ReferralStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateReferralStatusRequest {

    @NotNull(message = "Referral status is required")
    private ReferralStatus status;
}