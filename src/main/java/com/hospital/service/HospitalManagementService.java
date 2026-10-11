package com.hospital.service;

import com.hospital.entity.*;
import com.hospital.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class HospitalManagementService {

    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;
    private final AdmissionRepository admissionRepo;
    private final BillingItemRepository billingRepo;
    private final InvoiceRepository invoiceRepo;

    private final Path fileRoot = Paths.get("uploads");

    public HospitalManagementService(PatientRepository p, DoctorRepository d,
                                     AdmissionRepository a, BillingItemRepository b,
                                     InvoiceRepository i) {
        this.patientRepo = p;
        this.doctorRepo = d;
        this.admissionRepo = a;
        this.billingRepo = b;
        this.invoiceRepo = i;
    }

    @Transactional
    public Admission registerAndAdmitPatient(String name, Integer age, String place,
                                             Patient.IdCardType cardType, MultipartFile file,
                                             Long doctorId, Admission.Severity severity) throws IOException {
        // 1. File Upload
        if (!Files.exists(fileRoot)) Files.createDirectories(fileRoot);
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), this.fileRoot.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

        // 2. Persist Patient
        Patient patient = new Patient();
        patient.setUniqueUhid("UHID-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        patient.setFullName(name);
        patient.setAge(age);
        patient.setPlace(place);
        patient.setIdCardType(cardType);
        patient.setDocumentPath(filename);
        patient = patientRepo.save(patient);

        // 3. Assign Available Doctor
        Doctor doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        // 4. Create Active Admission
        Admission admission = new Admission();
        admission.setPatient(patient);
        admission.setDoctor(doctor);
        admission.setInjurySeverity(severity);
        admission.setStatus(Admission.AdmissionStatus.ACTIVE);
        admission = admissionRepo.save(admission);

        // 5. Prepopulate initial doctor fee and equal hospital margin
        BigDecimal docFee = calculateBaseDoctorFee(severity, doctor.getBaseConsultationFee());

        BillingItem docItem = new BillingItem();
        docItem.setItemCategory(BillingItem.ItemCategory.DOCTOR_FEE);
        docItem.setDescription("Doctor Consultation & Treatment Fee");
        docItem.setAmount(docFee);
        admission.addBillingItem(docItem);

        BillingItem hospitalItem = new BillingItem();
        hospitalItem.setItemCategory(BillingItem.ItemCategory.HOSPITAL_CHARGE);
        hospitalItem.setDescription("Hospital Overhead & Operational Charge");
        hospitalItem.setAmount(docFee); // Hospital mirrors fee charged by doctor
        admission.addBillingItem(hospitalItem);

        return admissionRepo.save(admission);
    }

    private BigDecimal calculateBaseDoctorFee(Admission.Severity severity, BigDecimal baseFee) {
        return switch (severity) {
            case MINOR -> baseFee.multiply(new BigDecimal("1.0"));
            case MODERATE -> baseFee.multiply(new BigDecimal("1.5"));
            case MAJOR -> baseFee.multiply(new BigDecimal("2.0"));
        };
    }

    @Transactional
    public Invoice finalizeBillAndDischarge(Long admissionId) {
        Admission admission = admissionRepo.findById(admissionId)
                .orElseThrow(() -> new IllegalArgumentException("Admission record not found"));

        if (admission.getStatus() == Admission.AdmissionStatus.DISCHARGED) {
            throw new IllegalStateException("Patient is already discharged.");
        }

        BigDecimal subtotal = admission.getBillingItems().stream()
                .map(BillingItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Net payable = Line items + 1500 registration charge
        BigDecimal totalPayable = subtotal.add(admission.getRegistrationFee());

        Invoice invoice = new Invoice();
        invoice.setAdmission(admission);
        invoice.setInvoiceNumber("INV-" + System.currentTimeMillis());
        invoice.setSubtotal(subtotal);
        invoice.setRegistrationFeeCredited(admission.getRegistrationFee());
        invoice.setNetPayable(totalPayable);
        invoiceRepo.save(invoice);

        // Transition status from ACTIVE to DISCHARGED
        admission.setStatus(Admission.AdmissionStatus.DISCHARGED);
        admission.setDischargedAt(LocalDateTime.now());
        admissionRepo.save(admission);

        return invoice;
    }
}