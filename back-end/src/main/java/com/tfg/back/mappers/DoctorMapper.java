package com.tfg.back.mappers;

import com.tfg.back.enums.UserStatus;
import com.tfg.back.model.*;
import com.tfg.back.model.dtos.doctor.DoctorDtoCreate;
import com.tfg.back.model.dtos.doctor.DoctorDtoGet;
import com.tfg.back.model.dtos.doctor.DoctorDtoUpdate;
import com.tfg.back.model.dtos.doctor.VisitedDoctorGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DoctorMapper {

    private final PasswordEncoder passwordEncoder;
    private final AppointmentMapper appointmentMapper;
    private final MedicalPrescriptionMapper medicalPrescriptionMapper;

    @Autowired
    public DoctorMapper(PasswordEncoder passwordEncoder, AppointmentMapper appointmentMapper, MedicalPrescriptionMapper medicalPrescriptionMapper) {
        this.passwordEncoder = passwordEncoder;
        this.appointmentMapper = appointmentMapper;
        this.medicalPrescriptionMapper = medicalPrescriptionMapper;
    }

    public Doctor toEntity(Doctor doctor,DoctorDtoUpdate dto) {
        Objects.requireNonNull(dto, "Doctor DTO cannot be null");
        Objects.requireNonNull(doctor, "Doctor cannot be null");
        doctor.setFullName(dto.fullName());
        doctor.setEmail(dto.email());
        doctor.setAddress(dto.address());
        doctor.setPhoneNumber(dto.phoneNumber());
        doctor.setDateOfBirth(dto.dateOfBirth());
        doctor.setAddress(dto.address());
        doctor.setMedicalLicenseNumber(dto.medicalLicenseNumber());
        doctor.setSpecialization(dto.specialization());
        return doctor;
    }

    public Doctor toEntity(DoctorDtoCreate dto, Department department) {
        Objects.requireNonNull(dto, "Doctor DTO cannot be null");

        Doctor doctor = new Doctor();

        doctor.setFullName(dto.fullName());
        doctor.setEmail(dto.email());
        doctor.setAddress(dto.address());
        doctor.setHashedPassword(passwordEncoder.encode(dto.hashedPassword()));
        doctor.setPhoneNumber(dto.phoneNumber());
        doctor.setDateOfBirth(dto.dateOfBirth());
        doctor.setStatus(UserStatus.ACTIVE);

        doctor.setMedicalLicenseNumber(dto.medicalLicenseNumber());
        doctor.setDepartment(department);
        doctor.setSpecialization(dto.specialization());

        Set<WorkingHours> workingHoursSet = new HashSet<>();
        for (WorkingHours wh : dto.workingHours()) {
            wh.setDoctor(doctor);
            for (TimeInterval ti : wh.getTimeIntervals()) {
                ti.setWorkingHours(wh);
            }
            workingHoursSet.add(wh);
        }

        doctor.setWorkingHours(workingHoursSet);

        return doctor;
    }

    public DoctorDtoGet toDtoGet(Doctor doctor) {
        Objects.requireNonNull(doctor, "Doctor cannot be null");
        return DoctorDtoGet.builder()
                .id(doctor.getId())
                .fullName(doctor.getFullName())
                .email(doctor.getEmail())
                .phoneNumber(doctor.getPhoneNumber())
                .dateOfBirth(doctor.getDateOfBirth())
                .createdAt(doctor.getCreatedAt())
                .medicalLicenseNumber(doctor.getMedicalLicenseNumber())
                .department(doctor.getDepartment())
                .specialization(doctor.getSpecialization())
                .address(doctor.getAddress())
                .workingHours(doctor.getWorkingHours())
                .build();
    }

    public List<DoctorDtoGet> toDtoGetList(List<Doctor> doctors) {
        return doctors.stream().map(this::toDtoGet).toList();
    }


    public static VisitedDoctorGet toVisitedDoctorGet(Doctor doctor) {
        VisitedDoctorGet visitedDoctorGet = new VisitedDoctorGet(doctor.getId(), doctor.getFullName());
        return visitedDoctorGet;
    }

    public static List<VisitedDoctorGet> toVisitedDoctorGetList(List<Doctor> doctors) {
        return doctors.stream()
                .map(DoctorMapper::toVisitedDoctorGet)
                .collect(Collectors.toList());
    }
}

