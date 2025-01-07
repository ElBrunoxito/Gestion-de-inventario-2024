package com.yobrunox.gestionmarko.controllers;

import com.yobrunox.gestionmarko.dto.buy.BuyAddDTO;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.dto.exception.ErrorDTO;
import com.yobrunox.gestionmarko.models.Buy;
import com.yobrunox.gestionmarko.models.UserEntity;
import com.yobrunox.gestionmarko.repository.UserRepository;
import com.yobrunox.gestionmarko.config.JwtProvider;
import com.yobrunox.gestionmarko.services.BuyService;
import com.yobrunox.gestionmarko.services.DetailBuyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/admin/buy")
@AllArgsConstructor
public class BuyController {

    private final BuyService buyService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final DetailBuyService detailBuyService;

    /*
        @GetMapping("admin/product/getAll")
        public ResponseEntity<?> getAll(@RequestHeader("Authorization") String authHeader){
            String token = authHeader.replace("Bearer ","");
            String username = jwtProvider.getUsernameFromToken(token);
            UserEntity user = userRepository.findByUsername(username).orElseThrow(
                    () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no existente")

            );

            return ResponseEntity.ok(productService.getAll(user.getBusiness().getId()));
        }

        @GetMapping("user/product/{idProduct}")
        public ResponseEntity<?> getProduct(@PathVariable UUID idProduct,
                                            @RequestHeader("Authorization") String authHeader){

            return ResponseEntity.ok(productService.getProduct(idProduct));
        }*/
    @GetMapping("getAll")
    public ResponseEntity<?> getAll(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no existente")
        );
        return ResponseEntity.ok(buyService.getAllForBusiness(user.getBusiness().getId()));

    }

    @GetMapping("getBuyWithDetailsById/{idBuy}")
    public ResponseEntity<?> getBuyWithDetailsById(@PathVariable UUID idBuy) {
        return ResponseEntity.ok(buyService.getBuyWithDetails(idBuy));
    }

    @PostMapping("create")
    public ResponseEntity<?> createBuy(@RequestBody BuyAddDTO buyAddDTO,
                                       @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no existente")

        );
        buyAddDTO.setUser(user);
        Buy buy = buyService.createBuy(buyAddDTO);

        ErrorDTO response = ErrorDTO.builder()
                .message(buy != null?"Compra creado correctamente":"Ocurrio un error")
                .code("M-200")
                .build();
        return ResponseEntity.ok(response);
        //return ResponseEntity.notFound().build();
    }
    @PutMapping("update")
    public ResponseEntity<?> updateBuy(@RequestBody BuyAddDTO buyAddDTO,
                                       @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no existente")
        );


        System.out.println("user : " + user.getId());
        buyAddDTO.setUser(user);

        Buy buy = buyService.updateBuy(buyAddDTO);

        ErrorDTO response = ErrorDTO.builder()
                .message(buy != null?"Compra actualizado correctamente":"Ocurrio un error")
                .code("M-200")
                .build();
        return ResponseEntity.ok(response);
        //return ResponseEntity.notFound().build();
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id,
                                    @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no existente")
        );
        buyService.deleteBuy(id);

        ErrorDTO response = ErrorDTO.builder()
                .message("Compra eimando correctamente eliminado correctamente")
                .code("M-200")
                //.body(buy)
                .build();
        return ResponseEntity.ok(response);
    }

}
