package agiliz.projetoAgiliz.dto.coleta;

import java.lang.reflect.RecordComponent;
import java.util.LinkedHashMap;
import java.util.Map;

public record ColetaCampos(
        String romaneio,
        String statusColeta,
        String nomeVendedor,
        String cnpj,
        String email,
        String horarioCorte,
        String diaHoraRegistro,
        String nomeColaborador,
        String emailColaborador
) {
    public static Map<String, String> camposColeta() {
        Map<String, String> campos = new LinkedHashMap<>();

        for (RecordComponent component : ColetaCampos.class.getRecordComponents()) {
            String nomeReal = component.getName();
            String nomeAmigavel = formatarNome(nomeReal);
            campos.put(nomeAmigavel, nomeReal);
        }

        return campos;
    }

    private static String formatarNome(String nomeCampo) {
        nomeCampo = nomeCampo.replaceAll("Coleta|Vendedor|Colaborador", "");

        String nomeFormatado = nomeCampo
                .replaceAll("([A-Z])", " $1")
                .trim()
                .replaceFirst("^.", Character.toUpperCase(nomeCampo.charAt(0)) + "");

        return nomeFormatado;
    }
}
