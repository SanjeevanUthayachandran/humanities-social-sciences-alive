package com.hssa.payment.repository;

import com.hssa.payment.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    Optional<Invoice> findByPaymentIdAndDeletedAtIsNull(UUID paymentId);
    Optional<Invoice> findByInvoiceNumberAndDeletedAtIsNull(String invoiceNumber);
}
