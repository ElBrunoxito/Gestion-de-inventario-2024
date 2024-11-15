package com.yobrunox.gestionmarko.controllers;

import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.dto.exception.ErrorDTO;
import com.yobrunox.gestionmarko.dto.sale.collect.CollectAddDTO;
import com.yobrunox.gestionmarko.dto.sale.collect.CollectGetDTO;
import com.yobrunox.gestionmarko.models.Collect;
import com.yobrunox.gestionmarko.models.ERole;
import com.yobrunox.gestionmarko.models.Sale;
import com.yobrunox.gestionmarko.models.UserEntity;
import com.yobrunox.gestionmarko.repository.SaleRepository;
import com.yobrunox.gestionmarko.repository.UserRepository;
import com.yobrunox.gestionmarko.security.JwtProvider;
import com.yobrunox.gestionmarko.services.CollectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@RestController
@RequestMapping("api/user/collect")
@AllArgsConstructor
public class CollectController {


    private final CollectService collectService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final SaleRepository saleRepository;

    @PostMapping("create")
    public ResponseEntity<?> createCollect(@RequestBody CollectAddDTO collectAddDTO,
                                           @RequestHeader("Authorization") String authHeader){

        Sale sale = saleRepository.findById(collectAddDTO.getIdSale()).orElseThrow(
                ()->new BusinessException("M-400", HttpStatus.NOT_FOUND, "Venta no encontrada")
        );

        if(sale.getCollect() != null){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Cobro ya fue creado");
        }

        //FALTA CAPTURAR EL ERROR DE LA CRRECAION DEL PDF CUANDO NO HAYA DATOS DE NEGOCIO

        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);

        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no existente")
        );

            //if(!user.getRoles().stream().anyMatch(role -> role.getRole() == ERole.ADMIN)){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.with(LocalTime.MIN);  // 00:00:00 de hoy
        LocalDateTime endOfDay = now.with(LocalTime.of(11, 59, 59));
        //if(sale.getSaleDate().isAfter(startOfDay) && sale.getSaleDate().isBefore(endOfDay)){

        //if(user.getRoles()){

        //}
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getRole() == ERole.ADMIN);
        boolean cumple =sale.getSaleDate().isBefore(startOfDay) || sale.getSaleDate().isAfter(endOfDay) ;
        if (cumple && !isAdmin) {

            throw new BusinessException("M-403", HttpStatus.FORBIDDEN,"No tienes permiso para agregar el cobro, por que ya vencio el dia");
        }


        Collect collect =  collectService.addCollect(collectAddDTO,user.getBusiness(),sale);

        String message = "";
        if (isAdmin && cumple) {
            message = "El cobro con "+ collect.getSale().getSaleTotal()+" se ha realizado :|, pero ya vencio el dia";
        }else{
            message = "El cobro con "+ collect.getSale().getSaleTotal()+" se ha realizado :)";
        }
        ErrorDTO response = ErrorDTO.builder()
                .message(message)
                .code("M-200")
                .body(collect)
                .build();
        return ResponseEntity.ok(response);

    }


    @GetMapping("getCollectByIdSale/{idSale}")
    public ResponseEntity<?> getCollectByIdSale(@PathVariable("idSale") UUID idSale){

        Sale sale = saleRepository.findById(idSale).orElseThrow(() -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Venta no existente"));
        CollectGetDTO collectGetDTO = CollectGetDTO.builder()
                .idCollect(sale.getCollect().getIdCollection())
                .subTotal(sale.getSaleTotal())
                .discount(sale.getCollect().getDiscountAmount())
                .total(sale.getCollect().getAmountCollection())
                .idTypePayment(sale.getCollect().getTypePayment().getIdTypePayment())
                .urlPdf(sale.getCollect().getUrlPdf())
                .build();

        ErrorDTO response = ErrorDTO.builder()
                //.message(message)
                .code("M-200")
                .body(collectGetDTO)
                .build();
        return ResponseEntity.ok(response);
    }
    /*
    @PutMapping("update")
    public ResponseEntity<?> updateCollect(){

    }*/
}
