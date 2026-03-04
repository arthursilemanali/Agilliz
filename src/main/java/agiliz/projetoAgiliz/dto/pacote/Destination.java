package agiliz.projetoAgiliz.dto.pacote;

public record Destination(
        Integer receiver_id,
        String receiver_name,
        String receiver_phone,
        Address shipping_address,
        Double latitude,
        Double longitude
) {
}
