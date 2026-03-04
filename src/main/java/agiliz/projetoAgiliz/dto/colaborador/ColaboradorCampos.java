package agiliz.projetoAgiliz.dto.colaborador;

import java.lang.reflect.RecordComponent;
import java.util.LinkedHashMap;
import java.util.Map;

public record ColaboradorCampos(
        String nomeColaborador,
        String cpf,
        String rg,
        String classeCarteira,
        String dataNascimento,
        String emailColaborador,
        String dataAdmissao,
        String telefoneColaborador
) {
    public static Map<String, String> camposColaborador() {
        Map<String, String> campos = new LinkedHashMap<>();

        for (RecordComponent component : ColaboradorCampos.class.getRecordComponents()) {
            String nomeReal = component.getName();
            String nomeAmigavel = formatarNome(nomeReal);
            campos.put(nomeAmigavel, nomeReal);
        }

        return campos;
    }

    private static String formatarNome(String nomeCampo) {
        String nomeFormatado = nomeCampo.replaceAll("Colaborador", "");

        nomeFormatado = nomeFormatado
                .replaceAll("([A-Z])", " $1")
                .trim()
                .replaceFirst("^.", Character.toUpperCase(nomeFormatado.charAt(0)) + "");

        return nomeFormatado;
    }
}
