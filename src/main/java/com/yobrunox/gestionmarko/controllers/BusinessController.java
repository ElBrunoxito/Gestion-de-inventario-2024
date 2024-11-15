package com.yobrunox.gestionmarko.controllers;

import com.yobrunox.gestionmarko.dto.business.CreateUpdateBusinessDTO;
import com.yobrunox.gestionmarko.dto.business.GetBusinessSimpleDTO;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.dto.exception.ErrorDTO;
import com.yobrunox.gestionmarko.models.Business;
import com.yobrunox.gestionmarko.models.ERole;
import com.yobrunox.gestionmarko.models.UserEntity;
import com.yobrunox.gestionmarko.repository.UserRepository;
import com.yobrunox.gestionmarko.security.JwtProvider;
import com.yobrunox.gestionmarko.services.BusinessService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/")
@AllArgsConstructor
@CrossOrigin
public class BusinessController {

    private final BusinessService businessService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @GetMapping("admin/business/getBusiness")
    public ResponseEntity<?> getBusiness(@RequestHeader("Authorization") String authHeader) {
        //prinero obtenermos user
        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        if(!userRepository.existsByUsername(username)){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no encontrado");
        }
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        //luego el id del negocio por user
        Long idBusiness = user.getBusiness().getId();

        //obtener business por id
        return ResponseEntity.ok(businessService.getBusinessForId(idBusiness));
    }
    @GetMapping("user/business/getBusiness")
    public ResponseEntity<?> getBusinessNav(@RequestHeader("Authorization") String authHeader) {
        //prinero obtenermos user
        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        if(!userRepository.existsByUsername(username)){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no encontrado");
        }
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        //luego el id del negocio por user
        Business business = user.getBusiness();

        //obtener business por id
        return ResponseEntity.ok(
                new GetBusinessSimpleDTO(business.getName(),business.getUrlImage())
        );
    }


    @PostMapping("admin/business/create")
    public ResponseEntity<?> createBusiness(@RequestBody CreateUpdateBusinessDTO request,
                                            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no encontrado")
        );
        //System.out.println(user);
        /*request.setUsers(List.of(user));
        System.out.println(request);*/
        //Necesit enviar los usuarios

        //Crear negocio
        Business business = businessService.createBusiness(request);
        user.setBusiness(business);
        userRepository.save(user);
        ErrorDTO response = ErrorDTO.builder()
                .message(business != null?"Business creado correctamente":"Ocurrio un error")
                .code("M-200")
                .build();
        return ResponseEntity.ok(response);

    }



    //Update Business

    @PutMapping("admin/business/update")
    public ResponseEntity<?> updateBusiness(@RequestParam CreateUpdateBusinessDTO request,
                                            @RequestParam("file") MultipartFile file,
                                            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);

        if(!userRepository.existsByUsername(username)){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no encontrado");
        }

        UserEntity user = userRepository.findByUsername(username).orElse(null);

        /*
        CreateUpdateBusinessDTO request = CreateUpdateBusinessDTO.builder()
                .name("Mi Negocio S.A.C.")
                .description("Una empresa dedicada a la venta de productos electrónicos.")
                .ruc("20512345678")
                .address("Av. Principal 123, Lima, Perú")
                .phone("+51 987 654 321")
                .build();*/

        request.setId(user.getBusiness().getId());
        request.setFile(file);
        Business business = businessService.updateBusiness(request);

        ErrorDTO response = ErrorDTO.builder()
                .message(business != null?"Business actualizado correctamente":"Ocurrio un error")
                .code("M-200")
                .build();


        return ResponseEntity.ok(response);

    }







}
