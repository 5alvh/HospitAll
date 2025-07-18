package com.tfg.back.model.dtos.doctor;

import com.tfg.back.model.dtos.appointment.AppointmentDtoGet;

import java.util.List;

public record DoctorSummaryResponse(List<AppointmentDtoGet> todayAppointments, Long pendingPrescriptions, Long totalPatients, Double averageRating) {


}
