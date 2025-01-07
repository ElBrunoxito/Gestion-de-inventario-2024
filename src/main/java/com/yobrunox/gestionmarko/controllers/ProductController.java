package com.yobrunox.gestionmarko.controllers;

import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.dto.exception.ErrorDTO;
import com.yobrunox.gestionmarko.dto.product.ProductAddDTO;
import com.yobrunox.gestionmarko.dto.product.ProductoGetDTO;
import com.yobrunox.gestionmarko.models.ERole;
import com.yobrunox.gestionmarko.models.Product;
import com.yobrunox.gestionmarko.models.UserEntity;
import com.yobrunox.gestionmarko.repository.UserRepository;
import com.yobrunox.gestionmarko.config.JwtProvider;
import com.yobrunox.gestionmarko.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api")
@AllArgsConstructor
@CrossOrigin()
public class ProductController {

    private final ProductService productService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @GetMapping("admin/product/getAll")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no existente")

        );
        if(!user.getRoles().stream().anyMatch(role -> role.getRole() == ERole.ADMIN)){
            new BusinessException("M-403", HttpStatus.FORBIDDEN,"No tienes permiso para ver los productos");
        }

        return ResponseEntity.ok(productService.getAll(user.getBusiness().getId()));
    }
    @GetMapping("user/product/getAll")
    public ResponseEntity<?> getAllForUser(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no existente")

        );
        if(!user.getRoles().stream().anyMatch(role -> role.getRole() == ERole.ADMIN)){
            new BusinessException("M-403", HttpStatus.FORBIDDEN,"No tienes permiso para ver los productos");
        }

        return ResponseEntity.ok(productService.getAllProductsForUser(user.getBusiness().getId()));
    }
/*
    @GetMapping("user/product/{idProduct}")
    public ResponseEntity<?> getProductAdmin(@PathVariable UUID idProduct,
            @RequestHeader("Authorization") String authHeader){

        return ResponseEntity.ok(productService.getProductByUser(idProduct));
    }*/

    @GetMapping("admin/product/{idProduct}")
    public ResponseEntity<?> getProductUser(@PathVariable UUID idProduct,
                                        @RequestHeader("Authorization") String authHeader){

        return ResponseEntity.ok(productService.getProductByAdmin(idProduct));
    }

    @PostMapping("admin/product/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductAddDTO productAddDTO,
                                           @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        if(!userRepository.existsByUsername(username)){
                throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no encontrado");
        }
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        //productAddDTO.setInitialStock(initStock!=null?initStock:0);
        Product product = productService.createProduct(productAddDTO,user);

        ProductoGetDTO body = new ProductoGetDTO(product);

        ErrorDTO response = ErrorDTO.builder()
                .message(product != null?"Producto " + product.getDescription() + " creado correctamente":"Ocurrio un error")
                .code("M-200")
                .body(body)
                .build();
        return ResponseEntity.ok(response);
        //return ResponseEntity.notFound().build();
    }
    @PutMapping("admin/product/update")
    public ResponseEntity<?> updateProduct(@RequestBody ProductAddDTO productAddDTO,
                                           @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        if(!userRepository.existsByUsername(username)){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no encontrado");
        }
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        //UPDATE
        Product product = productService.updateProduct(productAddDTO,user);
        ProductoGetDTO body = new ProductoGetDTO(product);

        ErrorDTO response = ErrorDTO.builder()
                .message(product != null?"Producto " + product.getDescription() + " se ha actualizado correctamente":"Ocurrio un error")
                .code("M-200")
                .body(body)
                .build();
        return ResponseEntity.ok(response);
    }



}
