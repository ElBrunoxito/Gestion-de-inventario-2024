package com.yobrunox.gestionmarko.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    private final Storage storage = StorageOptions.getDefaultInstance().getService();
    private final String bucketName = "gs://marko-e432c.appspot.com";

    public String uploadImage(MultipartFile file, Long idBusiness) throws IOException {
        String fileName =  file.getOriginalFilename(); // O puedes generar uno tú mismo
        String filePath = idBusiness.toString() + "/" + fileName;

        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.create(filePath, file.getInputStream(), file.getContentType());

        String bucketName = bucket.getName().replace("gs://", "");
        String fileUrl = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", bucketName, filePath.replace("/", "%2F"));

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
        //Bucket bucket = storage.get(bucketName);

        // Subir el archivo
        Blob blob = bucket.create(filePath, inputStream, "application/pdf");

        // Retornar la URL del archivo almacenado
        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucket.getName(),
                filePath.replace("/", "%2F"));


    }
    public boolean deletePdfBusiness(Long businessId, String fileUrl) {
        String filePath = extractFilePathFromUrl(fileUrl);

        // Obtener el cliente de almacenamiento
        Storage storage = StorageOptions.getDefaultInstance().getService();
        Bucket bucket = StorageClient.getInstance().bucket();

        // Verificar si el archivo existe
        Blob blob = bucket.get(filePath);
        if (blob != null && blob.exists()) {
            // Eliminar el archivo
            blob.delete();
            return true; // Archivo eliminado exitosamente
        }

        return false; // El archivo no existe o no se pudo eliminar
    }
    private String extractFilePathFromUrl(String fileUrl) {
        // Ejemplo de URL: https://firebasestorage.googleapis.com/v0/b/my-bucket-name/o/businessId%2FfileName.pdf?alt=media
        // Buscar la posición después de "/o/" y antes del parámetro "?alt=media"
        int startIndex = fileUrl.indexOf("/o/") + 3; // La posición donde inicia el `filePath`
        int endIndex = fileUrl.indexOf("?alt=media"); // Donde termina el `filePath`
        if (startIndex > 2 && endIndex > startIndex) {
            // Decodificar el `filePath` (remover %2F y otros caracteres codificados)
            return java.net.URLDecoder.decode(fileUrl.substring(startIndex, endIndex), StandardCharsets.UTF_8);
        }
        throw new IllegalArgumentException("La URL proporcionada no es válida: " + fileUrl);
    }


}