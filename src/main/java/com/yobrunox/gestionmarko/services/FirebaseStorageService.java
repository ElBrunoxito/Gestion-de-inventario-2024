package com.yobrunox.gestionmarko.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    private final Storage storage = StorageOptions.getDefaultInstance().getService();
    private final String bucketName = "gs://marko-e432c.appspot.com"; // Reemplaza con el nombre de tu bucket

    public String uploadImage(MultipartFile file, Long idBusiness) throws IOException {
        String fileName =  file.getOriginalFilename(); // O puedes generar uno tú mismo
        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.create(fileName, file.getInputStream(), file.getContentType());

        String bucketName = bucket.getName().replace("gs://", "");
        String fileUrl = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", bucketName, fileName.replace(" ", "%20"));

        return fileUrl; // URL de acceso público a la imagen
    }

    public String createPdfBusiness(ByteArrayOutputStream baos,Long businessId,String fileN) throws IOException{
        ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

        // Nombre del archivo que se va a guardar
        long timestamp = System.currentTimeMillis(); // Obtén el tiempo en milisegundos
        String fileName = "pdf_" + timestamp + "_" + fileN + ".pdf";
        String filePath = businessId.toString() + "/" + fileName;

        // Obtener el cliente de almacenamiento
        Storage storage = StorageOptions.getDefaultInstance().getService();

        // Obtener el bucket
        Bucket bucket = StorageClient.getInstance().bucket();

        // Subir el archivo
        Blob blob = bucket.create(fileName, inputStream, "application/pdf");

        // Retornar la URL del archivo almacenado
        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucket.getName(),
                fileName.replace("/", "%2F"));


    }



}