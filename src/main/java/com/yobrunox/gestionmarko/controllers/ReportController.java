package com.yobrunox.gestionmarko.controllers;

import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.dto.exception.ErrorDTO;
import com.yobrunox.gestionmarko.dto.report.CreateReportGenerateProductDTO;
import com.yobrunox.gestionmarko.dto.report.GETREPORT;
import com.yobrunox.gestionmarko.models.UserEntity;
import com.yobrunox.gestionmarko.repository.UserRepository;
import com.yobrunox.gestionmarko.security.JwtProvider;
import com.yobrunox.gestionmarko.services.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/report")
@AllArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;


    @GetMapping("getAll")
    public ResponseEntity<?> getAll(){

        ErrorDTO response = ErrorDTO.builder()
                .message("Reporte generado correctament")
                .code("M-200")
                .body(reportService.getAll())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("generate")
    public ResponseEntity<?> generateReport(@RequestBody CreateReportGenerateProductDTO report,
                                            @RequestHeader("Authorization") String authHeader){
        System.out.println("Aqui -1");

        String token = authHeader.replace("Bearer ","");
        System.out.println("Aqui -1");

        String username = jwtProvider.getUsernameFromToken(token);
        System.out.println("Aqui -3");

        if(!userRepository.existsByUsername(username)){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no encontrado");
        }
        System.out.println("Aqui -41");

        UserEntity user = userRepository.findByUsername(username).orElse(null);
        System.out.println("Aqui -5");

        if(report.getIdProduct() != null && report.getIdProduct().toString() != ""){
            GETREPORT rep = reportService.generateReportByProduct(report,user.getBusiness().getId());
            System.out.println("Aqui 2");

            ErrorDTO response = ErrorDTO.builder()
                    .message("Reporte generado correctament")
                    .code("M-200")
                    .body(rep)
                    .build();
            System.out.println("Aqui 3");

            return ResponseEntity.ok(response);

        }else if(report.getIdCategory() != null && report.getIdCategory().toString() != ""){
            System.out.println("Aqui 1");
            throw new BusinessException("M-400", HttpStatus.BAD_REQUEST,"En contruccion");

        }else{
            System.out.println("Aqui 4");

            throw new BusinessException("M-400", HttpStatus.BAD_REQUEST,"Error al solicitar generar reporte");

        }



    }
}
