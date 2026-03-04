package agiliz.projetoAgiliz.dto.vendedor;

import agiliz.projetoAgiliz.enums.OrigemPacote;
import agiliz.projetoAgiliz.models.Pacote;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public record Ecommerces(
        String ecommerce
) {
    public static List<String> getEcommerces(List<Pacote> pacotes){
        var ecommerces = new ArrayList<String>();
        if(pacotes.stream().filter(pacote -> pacote.getOrigem().equals(OrigemPacote.MERCADO_LIVRE)).count()>1){
            ecommerces.add("Mercado livre");
        }

        if(pacotes.stream().filter(pacote -> pacote.getOrigem().equals(OrigemPacote.SHOPEE)).count()>1){
            ecommerces.add("Shopee");
        }

        if(pacotes.stream().filter(pacote -> pacote.getOrigem().equals(OrigemPacote.OUTROS)).count()>1){
            ecommerces.add("Outros");
        }

        return ecommerces;
    }
}
