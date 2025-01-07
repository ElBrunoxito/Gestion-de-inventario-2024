package com.yobrunox.gestionmarko.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.dto.sale.collect.PdfAddDTO;
import com.yobrunox.gestionmarko.dto.sale.collect.ProductsPDFAddDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfService{
    // Definir fuentes
    private static Font titleFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    private static Font boldFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
    private static Font semiBoldFont = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
    private static Font regularFont = new Font(Font.FontFamily.HELVETICA, 8);
    private static Font smallFont = new Font(Font.FontFamily.HELVETICA, 6);
    public ByteArrayOutputStream generateStyledPdf(PdfAddDTO data) throws DocumentException {
        // Crear un ByteArrayOutputStream para almacenar el PDF
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Paragraph total = new Paragraph("TOTAL A PAGAR:      "+ (data.getTotal() - data.getDiscountAmount()), boldFont);
        total.setAlignment(Element.ALIGN_RIGHT);

        Paragraph thanks = new Paragraph(data.getMessage(), regularFont);
        thanks.setAlignment(Element.ALIGN_CENTER);




        float initialHeigth = data.getProducts().size() * getHeightRowTable(regularFont) + getHeightRowTable(boldFont)*getHeightForParagraph(total) + getHeightRowTable(regularFont)*getHeightForParagraph(thanks);

        // Definir márgenes
        float margin = 2f; // Márgenes en puntos
        float pageWidth = 164.41f; // Ancho de la página
        Document document = new Document(new Rectangle(pageWidth, initialHeigth), 4, 4, 3, 2);
        PdfWriter.getInstance(document, out);
        document.open();


        try {
            /*Image image = Image.getInstance(data.getUrlImage());
            image.scaleToFit(154, 50);
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);*/
            String urlImage = data.getUrlImage();
            Image image = Image.getInstance(new URL(urlImage)); // Usa directamente un objeto URL
            image.scaleToFit(154, 50);
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);
        }catch (Exception e){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND, "Error al generar imagen " + e.getMessage());

        }

        // Agregar la imagen al documento


        // Agregar encabezado (centrado)
        Paragraph header = new Paragraph(data.getNameBusiness() + "\n" + data.getAddressBusiness(), titleFont);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        // Información de contacto y venta
        document.add(new Paragraph("Cel: " + data.getPhoneBusiness(), regularFont));
        document.add(new Paragraph("NOTA DE VENTA ELECTRÓNICA", regularFont));
        document.add(new Paragraph(data.getCode(), boldFont));
        document.add(new Paragraph(" ", regularFont));  // Espaciado

        // Información del cliente
        //document.add(new Paragraph("RAZON SOCIAL: BODEGA", regularFont));
        document.add(new Paragraph("CLIENTE: " +data.getCustomer(), regularFont));
        //document.add(new Paragraph("DIRECCION: " + data.getAddress(), regularFont));
        //document.add(new Paragraph("TT: " + data.getAddress(), regularFont));
        document.add(new Paragraph("EMISION: " + this.getDate(data.getDateCollect()), regularFont));
        document.add(new Paragraph("MONEDA: SOL (PEN)" + data.getMoneyType(), regularFont));
        document.add(new Paragraph(" ", regularFont));  // Espaciado

        // Crear una tabla para los detalles de los productos
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100); // Ajustar al 100% del ancho de la página
        table.setWidths(new float[]{1f, 3f, 1f, 1f});

        // Encabezado de la tabla con línea debajo
        addTableHeaderWithLine(table, semiBoldFont);

        // Agregar detalles de los productos
        for(var i : data.getProducts()){
            addProductRowWithoutLines(table,i.getCantidad().toString(),i.getDescription(),i.getPrice().toString(),i.getTotal().toString(),regularFont);
        }
        if(data.getDiscountAmount() == 0 || data.getDiscountAmount() == null){

        }else{
            addProductRowWithoutLines(table,"1","Descuento","1",data.getDiscountAmount().toString(),regularFont);
        }
        // Agregar la tabla al documento
        document.add(table);




        // Agregar línea horizontal para separar la tabla del total
        Paragraph separator = new Paragraph(" ");
        separator.add(new Chunk(new LineSeparator(1f, 100f, BaseColor.BLACK, Element.ALIGN_CENTER, 0)));
        document.add(separator);

        // Agregar total a pagar

        document.add(total);

        document.add(thanks);

        // Cerrar el documento
        document.close();


        return out;
    }

    // Método para agregar el encabezado de la tabla con una línea debajo
    private void addTableHeaderWithLine(PdfPTable table, Font font) {
        Stream.of("CANT", "DESCRIPCION", "P.UNIT", "P.TOTAL").forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setPhrase(new Phrase(columnTitle, font));
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setBorder(Rectangle.BOTTOM); // Solo borde inferior (línea horizontal)
            header.setBorderWidthBottom(1f);    // Ajustar el grosor de la línea
            header.setPadding(2);  // Padding reducido en las celdas del encabezado
            table.addCell(header);
        });
    }

    // Método para agregar una fila de producto sin líneas y devolver la altura ocupada
    private void addProductRowWithoutLines(PdfPTable table, String cant, String desc, String pUnit, String pTotal, Font font) {
        PdfPCell cell1 = new PdfPCell(new Phrase(cant, font));
        PdfPCell cell2 = new PdfPCell(new Phrase(desc, font));
        PdfPCell cell3 = new PdfPCell(new Phrase(pUnit, font));
        PdfPCell cell4 = new PdfPCell(new Phrase(pTotal, font));

        // Eliminar los bordes de las celdas (sin líneas)
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        cell3.setBorder(Rectangle.NO_BORDER);
        cell4.setBorder(Rectangle.NO_BORDER);

        // Reducir el padding de todas las celdas de la fila
        cell1.setPadding(2);
        cell2.setPadding(2);
        cell3.setPadding(2);
        cell4.setPadding(2);

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);

//        return font.getSize() + 4; // Estimación de altura ocupada por la fila
    }

    private float getHeightRowTable(Font font){
        return font.getSize() + 4;
    }
    private float getHeightForParagraph(Paragraph paragraph){
        return paragraph.getTotalLeading();
    }

    private String getDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);

    }

}
