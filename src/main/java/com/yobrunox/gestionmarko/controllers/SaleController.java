package com.yobrunox.gestionmarko.controllers;

import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.dto.exception.ErrorDTO;
import com.yobrunox.gestionmarko.dto.sale.SaleAddDTO;
import com.yobrunox.gestionmarko.dto.sale.SaleGetSimpleDTO;
import com.yobrunox.gestionmarko.models.*;
import com.yobrunox.gestionmarko.repository.UserRepository;
import com.yobrunox.gestionmarko.config.JwtProvider;
import com.yobrunox.gestionmarko.services.DetailSaleService;
import com.yobrunox.gestionmarko.services.SaleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("api")
@AllArgsConstructor
public class    SaleController {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final SaleService saleService;
    private final DetailSaleService detailSaleService;

    @GetMapping("user/sale/getAll")
    public ResponseEntity<?> getAll(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no existente")
        );

        Set<Role> roles = user.getRoles();
        if(roles.stream().anyMatch(role -> role.getRole() == ERole.ADMIN)){
            return ResponseEntity.ok(saleService.getAllForBusinessAdmin(user.getBusiness().getId()));
        }else if(roles.stream().anyMatch(role -> role.getRole() == ERole.USER)){
            return ResponseEntity.ok(saleService.getAllForBusinessUser(user.getBusiness().getId()));
        }
        throw new BusinessException("M-500", HttpStatus.BAD_REQUEST,"Ha ocurrido un error en el servidor");
    }


    @GetMapping("user/sale/getSaleWithDetailsById/{idSale}")
    public ResponseEntity<?> getBuyWithDetailsById(@PathVariable UUID idSale,
                                                   @RequestHeader("Authorization") String authHeader)
    {



        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no existente")
        );
        System.out.println("AQUI 1");


        Set<Role> roles = user.getRoles();
        String messge = "";
        if(roles.stream().anyMatch(role -> role.getRole() == ERole.ADMIN)){
            messge = "Venta obtenida correctamente";
        }else{
            messge = "Solo puede ver la venta";
        }

        System.out.println("AQUI 2");


        ErrorDTO response = ErrorDTO.builder()
                .message(messge)
                .code("M-200")
                .body(saleService.getSaleWithDetails(idSale))
                .build();
        return ResponseEntity.ok(response);

    }



/*
    @GetMapping("getDetailsById/{id}")
    public ResponseEntity<?> getDetailsById(@PathVariable UUID id,
                                            @RequestHeader("Authorization") String authHeader) {


        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no existente")
        );

        Set<Role> roles = user.getRoles();
        String messge = "";
        if(roles.stream().anyMatch(role -> role.getRole() == ERole.ADMIN)){
            messge = "Venta obtenida correctamente";
        }else{
            messge = "Solo puede ver la venta";
        }

        ErrorDTO response = ErrorDTO.builder()
                .message(messge)
                .code("M-200")
                .body(detailSaleService.getAllDetailSaleForIdSale(id))
                .build();
        return ResponseEntity.ok(response);
    }

*/
    @PostMapping("user/sale/create")
    public ResponseEntity<?> createBuy(@RequestBody SaleAddDTO saleAddDTO,
                                       @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no existente")
        );



        saleAddDTO.setUser(user);
        Sale sale = saleService.createSale(saleAddDTO);

        SaleGetSimpleDTO s = new SaleGetSimpleDTO(sale.getIdSale(),sale.getCode());

        ErrorDTO response = ErrorDTO.builder()
                .message(sale != null?"Venta creada correctamente":"Ocurrio un error")
                .body(s)
                .code("M-200")
                .build();
        return ResponseEntity.ok(response);
        //return ResponseEntity.notFound().build();
    }


    @GetMapping("user/sale/hasCollect/{id}")
    public ResponseEntity<?> hasCollect(@PathVariable UUID id,
            @RequestHeader("Authorization") String authHeader) {

        return ResponseEntity.ok(saleService.saleHasCollect(id));
    }

    @PutMapping("admin/sale/update")
    public ResponseEntity<?> updateBuy(@RequestBody SaleAddDTO saleAddDTO,
                                       @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no existente")
        );


        //if(!saleService.saleHasCollect(saleAddDTO.getIdSale())){
         //   throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Te falta ");

        //}


        //System.out.println("user : " + user.getId());
        saleAddDTO.setUser(user);

        Sale sale = saleService.updateSale(saleAddDTO);
        SaleGetSimpleDTO s = new SaleGetSimpleDTO(sale.getIdSale(),sale.getCode());

        ErrorDTO response = ErrorDTO.builder()
                .message(sale != null?"Venta actualizado correctamente":"Ocurrio un error")
                .code("M-200")
                .body(s)
                .build();
        return ResponseEntity.ok(response);
        //return ResponseEntity.notFound().build();
    }








    @DeleteMapping("user/sale/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id,
                                    @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no existente")
        );
        saleService.deleteSale(id, user.getBusiness().getId());

        ErrorDTO response = ErrorDTO.builder()
                .message("Venta eliminado correctamente")
                .code("M-200")
                //.body(buy)
                .build();
        return ResponseEntity.ok(response);
    }

}
