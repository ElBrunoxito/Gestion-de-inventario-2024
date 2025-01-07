package com.yobrunox.gestionmarko.controllers;

import com.yobrunox.gestionmarko.dto.TypeComprobante.TypeComprobanteGetDTO;
import com.yobrunox.gestionmarko.dto.categoryAndUnit.GetCategoryAndUnitDTO;
import com.yobrunox.gestionmarko.dto.categoryAndUnit.GetTypePayment;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.dto.exception.ErrorDTO;
import com.yobrunox.gestionmarko.models.UserEntity;
import com.yobrunox.gestionmarko.repository.TypeComprobanteRepository;
import com.yobrunox.gestionmarko.repository.TypePaymentRepository;
import com.yobrunox.gestionmarko.repository.UserRepository;
import com.yobrunox.gestionmarko.config.JwtProvider;
import com.yobrunox.gestionmarko.services.CategoryService;
import com.yobrunox.gestionmarko.services.UnitService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryAndUnitController {

    private final CategoryService categoryService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final UnitService unitService;



    private final TypeComprobanteRepository typeComprobanteRepository;
    private final TypePaymentRepository typePaymentRepository;

    @GetMapping("admin/category/getAll")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ","");
        String username = jwtProvider.getUsernameFromToken(token);
        if(!userRepository.existsByUsername(username)){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no encontrado");
        }
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        Set<String> categoryAddDTOSet = categoryService.getCategories(user.getBusiness().getId());
        GetCategoryAndUnitDTO res = GetCategoryAndUnitDTO.builder()
                .categories(categoryService.getCategories(user.getBusiness().getId()))
                .units(unitService.getUnits())
                .build();

        ErrorDTO err = ErrorDTO.builder()
                .body(res)
                .build();
        return ResponseEntity.ok(err);
    }

    @GetMapping("user/typeMovement/getAll")
    public ResponseEntity<?> getAllTypeComprobante(@RequestHeader("Authorization") String authHeader) {

        List<TypeComprobanteGetDTO> res = typeComprobanteRepository.findAll().stream()
                .map(tc->new TypeComprobanteGetDTO(tc.getIdTypeComprobante(),tc.getTypeComprobante().name()))
                .collect(Collectors.toList());

        ErrorDTO err = ErrorDTO.builder()
                .body(res)
                .build();
        return ResponseEntity.ok(err);
    }


    @GetMapping("user/typePayment/getAll")
    public ResponseEntity<?> getAllTypePayment(@RequestHeader("Authorization") String authHeader) {
        List<GetTypePayment> res = typePaymentRepository.findAll().stream()
                .map(tp -> new GetTypePayment(tp.getIdTypePayment(),tp.getName()))
                .collect(Collectors.toList());

        ErrorDTO err = ErrorDTO.builder()
                .body(res)
                .build();
        return ResponseEntity.ok(err);

    }





    }
