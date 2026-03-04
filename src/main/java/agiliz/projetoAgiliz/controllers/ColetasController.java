package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.dashColetas.DadosColeta;
import agiliz.projetoAgiliz.services.ColetasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/dados-coleta")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MOTOBOY')")
public class ColetasController {

    private final ColetasService service;

    @GetMapping
    public ResponseEntity<DadosColeta> getDadosColeta(@RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);

        return ok().body(service.getDadosColeta(startDateTime, endDateTime));
    }
}
