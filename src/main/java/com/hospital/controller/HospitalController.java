package com.hospital.controller;

import com.hospital.entity.*;
import com.hospital.repository.AdmissionRepository;
import com.hospital.service.HospitalManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class HospitalController {

    private final HospitalManagementService hospitalService;
    private final AdmissionRepository admissionRepo;

    public HospitalController(HospitalManagementService hs, AdmissionRepository ar) {
        this.hospitalService = hs;
        this.admissionRepo = ar;
    }

    @PostMapping("/patients/register")
    public ResponseEntity<Admission> registerPatient(
            @RequestParam("name") String name,
            @RequestParam("age") Integer age,
            @RequestParam("place") String place,
            @RequestParam("idCardType") Patient.IdCardType cardType,
            @RequestParam("document") MultipartFile document,
            @RequestParam("doctorId") Long doctorId,
            @RequestParam("severity") Admission.Severity severity) throws IOException {

        return ResponseEntity.ok(hospitalService.registerAndAdmitPatient(
                name, age, place, cardType, document, doctorId, severity));
    }

    @GetMapping("/patients/active")
    public ResponseEntity<List<Admission>> getActivePatients() {
        return ResponseEntity.ok(admissionRepo.findByStatus(Admission.AdmissionStatus.ACTIVE));
    }

    @GetMapping("/patients/discharged")
    public ResponseEntity<List<Admission>> getDischargedPatients() {
        return ResponseEntity.ok(admissionRepo.findByStatus(Admission.AdmissionStatus.DISCHARGED));
    }

    @PostMapping("/admissions/{admissionId}/items")
    public ResponseEntity<Admission> addBillingItem(
            @PathVariable Long admissionId,
            @RequestBody BillingItem item) {
        Admission admission = admissionRepo.findById(admissionId).orElseThrow();
        admission.addBillingItem(item);
        return ResponseEntity.ok(admissionRepo.save(admission));
    }

    @DeleteMapping("/admissions/{admissionId}/items/{itemId}")
    public ResponseEntity<Admission> removeBillingItem(
            @PathVariable Long admissionId,
            @PathVariable Long itemId) {
        Admission admission = admissionRepo.findById(admissionId).orElseThrow();
        admission.getBillingItems().removeIf(bi -> bi.getId().equals(itemId));
        return ResponseEntity.ok(admissionRepo.save(admission));
    }

    @PostMapping("/admissions/{admissionId}/discharge")
    public ResponseEntity<Invoice> dischargePatient(@PathVariable Long admissionId) {
        return ResponseEntity.ok(hospitalService.finalizeBillAndDischarge(admissionId));
    }
}