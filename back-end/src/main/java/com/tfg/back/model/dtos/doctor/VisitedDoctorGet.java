package com.tfg.back.model.dtos.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

public record VisitedDoctorGet (UUID id, String fullName){
}
