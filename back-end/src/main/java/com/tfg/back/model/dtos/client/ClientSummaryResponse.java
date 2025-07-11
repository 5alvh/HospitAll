package com.tfg.back.model.dtos.client;


import com.tfg.back.model.Notification;
import com.tfg.back.model.dtos.appointment.AppointmentDtoGet;
import com.tfg.back.model.dtos.medicalPrescription.MedicalPrescriptionDtoGet;

import java.util.List;

public record ClientSummaryResponse(
        List<AppointmentDtoGet> appointments,
        List<MedicalPrescriptionDtoGet> prescriptions,
        List<Notification> notifications
){
}
