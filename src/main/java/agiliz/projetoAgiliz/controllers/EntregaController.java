package agiliz.projetoAgiliz.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import agiliz.projetoAgiliz.services.ColaboradorService;

import agiliz.projetoAgiliz.dto.dashEntregas.DashEntregas;

import java.time.LocalDate;


@RestController
@CrossOrigin
@RequestMapping("/dash-entregas")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class EntregaController {
    
    @Autowired
    private ColaboradorService colaboradorService;

    @GetMapping
    public ResponseEntity<DashEntregas> getMethodName(@RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return ResponseEntity.status(HttpStatus.OK).body(colaboradorService.montarDash(start, end));
    }
    
}
