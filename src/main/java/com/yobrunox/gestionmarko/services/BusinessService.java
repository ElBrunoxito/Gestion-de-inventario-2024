package com.yobrunox.gestionmarko.services;

import com.yobrunox.gestionmarko.dto.UserAndRoleDTO;
import com.yobrunox.gestionmarko.dto.business.CreateUpdateBusinessDTO;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.models.Business;
import com.yobrunox.gestionmarko.models.UserEntity;
import com.yobrunox.gestionmarko.repository.BusinessRepository;
import com.yobrunox.gestionmarko.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BusinessService {
    private final BusinessRepository businessRepository;
    private final FirebaseStorageService firebaseStorageService;
    private final UserRepository userRepository;

    public Business createBusiness(CreateUpdateBusinessDTO createBusinessDTO){

        Business business = Business.builder()
                .name(createBusinessDTO.getName())

                .description(createBusinessDTO.getDescription())
                .ruc(createBusinessDTO.getRuc())
                .address(createBusinessDTO.getAddress())
                .phone(createBusinessDTO.getPhone())
                .users(createBusinessDTO.getUsers())
                .build();
        return businessRepository.save(business);
    }
    /*public Business updateBusiness(CreateUpdateBusinessDTO createBusinessDTO){

        Business business = Business.builder()
                .name(createBusinessDTO.getName())
                .description(createBusinessDTO.getDescription())
                .ruc(createBusinessDTO.getRuc())
                .address(createBusinessDTO.getAddress())
                .phone(createBusinessDTO.getPhone())
                .users(createBusinessDTO.getUsers())
                .build();
        return businessRepository.save(business);
    }*/


    //Update Business
    public Business updateBusiness(CreateUpdateBusinessDTO createUpdateBusinessDTO, MultipartFile file){
        /*Business business = businessRepository.findById(createUpdateBusinessDTO.getId()).orElseThrow(
                () -> new BusinessException("M-500", HttpStatus.BAD_REQUEST,"Business no existente, error")
        );*/


        if(!businessRepository.existsById(createUpdateBusinessDTO.getId())){
            throw new BusinessException("M-500", HttpStatus.BAD_REQUEST,"Business no existente");
        }
        String urlImage = "";
        System.out.println("PTMR");

        try{
            urlImage = firebaseStorageService.uploadImage(file, createUpdateBusinessDTO.getId());
            System.out.println("URL: " + urlImage);

        }catch (IOException e){

            System.out.println("Error");
            throw new BusinessException("M-500", HttpStatus.BAD_REQUEST,"Ha ocurrido un error con la red: " + e.getMessage());
        }
        System.out.println("PTMR");

        Business business = Business.builder()
                .id(createUpdateBusinessDTO.getId())
                .name(createUpdateBusinessDTO.getName())
                .description(createUpdateBusinessDTO.getDescription())
                .ruc(createUpdateBusinessDTO.getRuc())
                .address(createUpdateBusinessDTO.getAddress())
                .phone(createUpdateBusinessDTO.getPhone())
                .urlImage(urlImage)
                .message(createUpdateBusinessDTO.getMessage())
                .build();

        return businessRepository.save(business);
    }



    public Business getBusinessForId(Long id){
        return businessRepository.findById(id).orElse(null);
    }



    public List<UserAndRoleDTO> getUserAndRoles(UserEntity admin){
        List<UserEntity> data = userRepository.getByAdminWithRolesId(admin.getId()).orElseThrow(
                ()-> new BusinessException("M-500", HttpStatus.BAD_REQUEST,"Ha oocuurido un error para obtener la data")
        );
        List<UserAndRoleDTO> userAndRoles = new ArrayList<>();
        userAndRoles.add(new UserAndRoleDTO(admin.getUsername(),admin.getRoles().stream()
                .map(role -> role.getRole().name())
                .collect(Collectors.toList())));

        userAndRoles.addAll(data.stream().map(user -> {
            UserAndRoleDTO dto = new UserAndRoleDTO();
            dto.setUsername(user.getUsername());
            dto.setRoles(user.getRoles().stream()
                    .map(role -> role.getRole().name())
                    .collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList()));

        return userAndRoles;


    }

}
