package com.yobrunox.gestionmarko.controllers;

import com.itextpdf.text.DocumentException;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.dto.sale.collect.PdfAddDTO;
import com.yobrunox.gestionmarko.dto.sale.collect.ProductsPDFAddDTO;
import com.yobrunox.gestionmarko.services.PdfService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/auth")
@AllArgsConstructor
public class PruebasController {

    private final PdfService pdfService;

    @GetMapping("pdf/get")
    public ResponseEntity<byte[]> getAll(){
        try {
            List<ProductsPDFAddDTO> products = new ArrayList<>();
            products.add(new ProductsPDFAddDTO(1F, "SAN MATEO CON GAS 600ML", 18.00, 18.00));
            products.add(new ProductsPDFAddDTO(1F, "GUARANA 3000ML + 2 PEPSI", 28.00, 28.00));
            products.add(new ProductsPDFAddDTO(1F, "BIG COLA 3000ML + 1L", 35.50, 35.50));

            // Crear la factura
            PdfAddDTO invoice = new PdfAddDTO(
                    //"https://firebasestorage.googleapis.com/v0/b/marko-e432c.appspot.com/o/_816e5d72-811c-43d8-ba22-ff96002bdb28.jpg?alt=media",
                    //"https://firebasestorage.googleapis.com/v0/b/marko-e432c.appspot.com/o/Mi%20Negocio%20S.A.C.%2Fayaea.png?alt=media&token=68e78355-59b0-4e4a-8014-c618d5d613ed",
                    "https://firebasestorage.googleapis.com/v0/b/marko-e432c.appspot.com/o/Mi%20Negocio%20S.A.C.%2Fotro.png?alt=media&token=e8b6aa41-4bee-4820-8391-2b9ff18d5086",
                    "DISTRIBUIDORA MARKO",       // Nombre del negocio
                    "AV. TARMA 542",            // Dirección del negocio
                    "918055471",                 // Teléfono del negocio
                    "F#01-000192",              // Código
                    "JUANA VICTORIA VALERIO OSPINA", // Cliente
                    "JR GALVEZ 146",            // Dirección del cliente
                    new Date(),                 // Fecha de emisión
                    "SOL (PEN)",                // Tipo de moneda
                    products,                   // Lista de productos
                    1D,
                    81.50,                      // Total
                    "¡GRACIAS POR SU COMPRA!"   // Mensaje
            );

            ByteArrayOutputStream pdfStream = pdfService.generateStyledPdf(invoice);
            byte[] pdfBytes = pdfStream.toByteArray(); // Convertir a byte[]

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=archivo.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (DocumentException e) {
            //return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            throw new BusinessException("500",HttpStatus.BAD_REQUEST,e.getMessage());

        }
    }


}
