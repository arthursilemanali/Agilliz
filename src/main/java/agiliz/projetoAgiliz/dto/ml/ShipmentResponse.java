package agiliz.projetoAgiliz.dto.ml;

public record ShipmentResponse(
        Long id,
        String status,
        String substatus,
        String date_created,
        String last_updated,
        double declared_value,
        String tracking_number
) {
}
