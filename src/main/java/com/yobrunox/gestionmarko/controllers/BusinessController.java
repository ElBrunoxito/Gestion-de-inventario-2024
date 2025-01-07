package com.yobrunox.gestionmarko.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yobrunox.gestionmarko.dto.business.CreateUpdateBusinessDTO;
import com.yobrunox.gestionmarko.dto.business.GetBusinessSimpleDTO;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.dto.exception.ErrorDTO;
import com.yobrunox.gestionmarko.models.Business;
import com.yobrunox.gestionmarko.models.UserEntity;
import com.yobrunox.gestionmarko.repository.UserRepository;
import com.yobrunox.gestionmarko.config.JwtProvider;
import com.yobrunox.gestionmarko.services.BusinessService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> updateBusiness(@RequestParam("file") MultipartFile file,
                                            @RequestParam("data") String  data,
                                            @RequestHeader("Authorization") String authHeader) {
        ObjectMapper objectMapper = new ObjectMapper();
        CreateUpdateBusinessDTO businessDTO = null;

        try {
            businessDTO = objectMapper.readValue(data, CreateUpdateBusinessDTO.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Error al deserializar los datos: " + e.getMessage());
        }


        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        System.out.println("Llego hasta aqui 1 ");



        if(!userRepository.existsByUsername(username)){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no encontrado");
        }
        System.out.println("Llego hasta aqui 2 ");

        UserEntity user = userRepository.findByUsername(username).orElse(null);
        System.out.println("Llego hasta aqui 3 ");


        /*
        CreateUpdateBusinessDTO request = CreateUpdateBusinessDTO.builder()
                .name("Mi Negocio S.A.C.")
                .description("Una empresa dedicada a la venta de productos electrónicos.")
                .ruc("20512345678")
                .address("Av. Principal 123, Lima, Perú")
                .phone("+51 987 654 321")
                .build();*/

        //request.setId(user.getBusiness().getId());
        //data.setFile(file);
        System.out.println("Llego hasta aqui 4");
        try {

            Business business = businessService.updateBusiness(businessDTO,file);

            System.out.println("Llego hasta aqui 5");

            ErrorDTO response = ErrorDTO.builder()
                    .message(business != null?"Business actualizado correctamente":"Ocurrio un error")
                    .code("M-200")
                    .build();


            return ResponseEntity.ok(response);
        }catch (Exception e){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Prueba");
        }



    }



    @GetMapping("admin/business/getUserRoles")
    public ResponseEntity<?> getUserAndRoles(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);

        if(!userRepository.existsByUsername(username)){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no encontrado");
        }

        UserEntity user = userRepository.findByUsername(username).orElse(null);

        var data = businessService.getUserAndRoles(user);
        ErrorDTO response = ErrorDTO.builder()
                .body(data)
                .code("M-200")
                .build();


        return ResponseEntity.ok(response);
    }







}
