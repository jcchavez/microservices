package com.ioSoftware.store.shopping.repository;

import com.ioSoftware.store.shopping.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceItemsRepository extends JpaRepository<InvoiceItem,Long> {
}
