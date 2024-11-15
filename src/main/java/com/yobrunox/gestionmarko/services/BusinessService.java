package com.yobrunox.gestionmarko.services;

import com.yobrunox.gestionmarko.dto.business.CreateUpdateBusinessDTO;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.models.Business;
import com.yobrunox.gestionmarko.repository.BusinessRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class BusinessService {
    private final BusinessRepository businessRepository;
    private final FirebaseStorageService firebaseStorageService;

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
    public Business updateBusiness(CreateUpdateBusinessDTO createUpdateBusinessDTO){
        /*Business business = businessRepository.findById(createUpdateBusinessDTO.getId()).orElseThrow(
                () -> new BusinessException("M-500", HttpStatus.BAD_REQUEST,"Business no existente, error")
        );*/

        if(!businessRepository.existsById(createUpdateBusinessDTO.getId())){
            throw new BusinessException("M-500", HttpStatus.BAD_REQUEST,"Business no existente");
        }
        String urlImage = "";
        System.out.println("PTMR");

        try{
            urlImage = firebaseStorageService.uploadImage(createUpdateBusinessDTO.getFile(), createUpdateBusinessDTO.getId());
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
                .build();

        return businessRepository.save(business);
    }



    public Business getBusinessForId(Long id){
        return businessRepository.findById(id).orElse(null);
    }
}
