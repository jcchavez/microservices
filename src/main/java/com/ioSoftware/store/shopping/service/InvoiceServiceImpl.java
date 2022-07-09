package com.ioSoftware.store.shopping.service;

import com.ioSoftware.store.shopping.client.CustomerClient;
import com.ioSoftware.store.shopping.client.ProductClient;
import com.ioSoftware.store.shopping.entity.InvoiceItem;
import com.ioSoftware.store.shopping.model.Customer;
import com.ioSoftware.store.shopping.model.Product;
import com.ioSoftware.store.shopping.repository.InvoiceItemsRepository;
import com.ioSoftware.store.shopping.repository.InvoiceRepository;
import com.ioSoftware.store.shopping.entity.Invoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoiceItemsRepository invoiceItemsRepository;
    @Autowired
    CustomerClient customerClient;

    @Autowired
    ProductClient productClient;

    @Override
    public List<Invoice> findInvoiceAll() {
        return  invoiceRepository.findAll();
    }
    //TODO implement Freign web services calls to obtain information about Customer & Product


    @Override
    public Invoice createInvoice(Invoice invoice) {
        Invoice invoiceDB = invoiceRepository.findByNumberInvoice ( invoice.getNumberInvoice () );
        if (invoiceDB !=null){
            return  invoiceDB;
        }
        invoice.setState("CREATED");
        invoiceDB = invoiceRepository.save(invoice);
        //for every item on Invoice, update the Stock Calling the product-service via Feign Client
        invoiceDB.getItems().forEach( invoiceItem -> {
            productClient.updateStockProduct( invoiceItem.getProductId(), invoiceItem.getQuantity() * -1);
        });
        //TODO optionally for each item obtain the Product & Customer Info from Freign clients.

        return invoiceDB;
    }


    @Override
    public Invoice updateInvoice(Invoice invoice) {
        Invoice invoiceDB = getInvoice(invoice.getId());
        if (invoiceDB == null){
            return  null;
        }
        invoiceDB.setCustomerId(invoice.getCustomerId());
        invoiceDB.setDescription(invoice.getDescription());
        invoiceDB.setNumberInvoice(invoice.getNumberInvoice());
        invoiceDB.getItems().clear();
        invoiceDB.setItems(invoice.getItems());
        return invoiceRepository.save(invoiceDB);
    }


    @Override
    public Invoice deleteInvoice(Invoice invoice) {
        Invoice invoiceDB = getInvoice(invoice.getId());
        if (invoiceDB == null){
            return  null;
        }
        invoiceDB.setState("DELETED");
        return invoiceRepository.save(invoiceDB);
    }

    /**
     * Get an invoice, connecting via Feign Client to get the Customer Info from Customer-service microservice
     * and for each item, gets the Product Info from Product-service microservice
     * @param id from de Invoice
     * @return Invoice with complete info
     */
    @Override
    public Invoice getInvoice(Long id) {

        Invoice invoice= invoiceRepository.findById(id).orElse(null);
        if (null != invoice ){
            Customer customer = customerClient.getCustomer(invoice.getCustomerId()).getBody();
            invoice.setCustomer(customer);
            List<InvoiceItem> listItem=invoice.getItems().stream().map(invoiceItem -> {
                Product product = productClient.getProduct(invoiceItem.getProductId()).getBody();
                invoiceItem.setProduct(product);
                return invoiceItem;
            }).collect(Collectors.toList());
            invoice.setItems(listItem);
        }
        return invoice ;
    }
}
