package com.yobrunox.gestionmarko.controllers;

import com.yobrunox.gestionmarko.dto.dashboard.DashboardDataGetDTO;
import com.yobrunox.gestionmarko.dto.exception.ErrorDTO;
import com.yobrunox.gestionmarko.services.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/dashboard")
@AllArgsConstructor
public class DashboardController {

    //private SimpMessagingTemplate messagingTemplate;
    private final DashboardService dashboardService;

    @GetMapping("data")
    public ResponseEntity<?>getData(){

        ErrorDTO res = ErrorDTO.builder()
                .body(dashboardService.getDashboardData())
                .build();
        return ResponseEntity.ok(res);
    }


    /*@Scheduled(fixedRate = 5000)
    public void sendUpdates() {
        System.out.println("AQUI 10");

        DashboardDataGetDTO updatedData = dashboardService.getDashboardData();
        System.out.println("AQUI 11");

        messagingTemplate.convertAndSend("/topic/updates", updatedData); // Enviar datos a los clientes
    }*/
}
