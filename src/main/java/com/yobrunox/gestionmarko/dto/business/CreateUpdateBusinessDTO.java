package com.yobrunox.gestionmarko.dto.business;

import com.yobrunox.gestionmarko.models.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateBusinessDTO {
    //Sustento a cambios
    private Long id;

    //Datos
    private String name;
    private String description;
    private String ruc;
    private String address;
    private String phone;

    private String message;
    private String urlImage;


    private MultipartFile file;

    private List<UserEntity> users;

}
