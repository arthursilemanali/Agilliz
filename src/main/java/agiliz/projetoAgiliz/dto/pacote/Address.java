package agiliz.projetoAgiliz.dto.pacote;

public record Address(
        Long address_id,
        String address_line,
        String street_name,
        String street_number,
        String zip_code,
        City city,
        State state,
        Country country,
        Neighborhood neighborhood
) {
}
