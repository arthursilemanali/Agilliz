package agiliz.projetoAgiliz.dto.vendedor;
import java.lang.reflect.RecordComponent;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

public record VendedorCampos(
        String nomeVendedor,
        String cnpj,
        String rua,
        String cep,
        String telefoneVendedor,
        Double retornoTotal,
        LocalTime horarioCorte,
        String email
) {
    public static Map<String, String> camposVendedor() {
        Map<String, String> campos = new LinkedHashMap<>();

        for (RecordComponent component : VendedorCampos.class.getRecordComponents()) {
            String nomeReal = component.getName();
            String nomeAmigavel = formatarNome(nomeReal);
            campos.put(nomeAmigavel, nomeReal);
        }

        return campos;
    }

    private static String formatarNome(String nomeCampo) {
        nomeCampo = nomeCampo.replaceAll("Vendedor", "");

        String nomeFormatado = nomeCampo
                .replaceAll("([A-Z])", " $1")
                .trim()
                .replaceFirst("^.", Character.toUpperCase(nomeCampo.charAt(0)) + "");

        return nomeFormatado;
    }
}
