package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.dashFinanceira.DadosFinanceiros;
import agiliz.projetoAgiliz.services.FinanceiroService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.http.ResponseEntity.ok;

@RequestMapping("/dados-financeiros")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_FINANCEIRO')")
public class FinanceiroController {

    private final FinanceiroService service;
    
    @GetMapping
    public ResponseEntity<DadosFinanceiros> getDadosFinanceiros(@RequestParam("startDate") String startDateString
            , @RequestParam("endDate") String endDateString) {
        LocalDate startDate = LocalDate.parse(startDateString);
        LocalDate endDate = LocalDate.parse(endDateString);
        return ok().body(service.getDadosDash(startDate, endDate));
    }
}