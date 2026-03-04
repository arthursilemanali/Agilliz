package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.enums.StatusColeta;
import agiliz.projetoAgiliz.models.Coleta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ColetaRepositoryCustom {

    private static final Map<String, String> CAMPO_MAPEADO = new HashMap<>();

    static {
        CAMPO_MAPEADO.put("romaneio", "romaneio");
        CAMPO_MAPEADO.put("statusColeta", "statusColeta");
        CAMPO_MAPEADO.put("nomeVendedor", "vendedor.nomeVendedor");
        CAMPO_MAPEADO.put("cnpj", "vendedor.cnpj");
        CAMPO_MAPEADO.put("horarioCorte", "horarioCorte");
        CAMPO_MAPEADO.put("diaHoraRegistro", "diaHoraRegistro");
        CAMPO_MAPEADO.put("nomeColaborador", "colaborador.nomeColaborador");
        CAMPO_MAPEADO.put("emailColaborador", "colaborador.emailColaborador");
    }

    private final EntityManager entityManager;

    public ColetaRepositoryCustom(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Coleta> findByFilters(Map<String, String> filtros) {
        if (filtros == null || filtros.isEmpty()) {
            String jpql = "SELECT DISTINCT c FROM Coleta c LEFT JOIN c.coletores col LEFT JOIN col.colaborador colaborador";
            Query query = entityManager.createQuery(jpql);
            return query.getResultList();
        }

        StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT c FROM Coleta c LEFT JOIN c.coletores col LEFT JOIN col.colaborador colaborador WHERE");

        boolean isFirst = true;

        for (Map.Entry<String, String> entry : filtros.entrySet()) {
            String campoMapeado = CAMPO_MAPEADO.get(entry.getKey());
            if (campoMapeado != null) {
                if (!isFirst) {
                    queryBuilder.append(" AND");
                }

                if ("horarioCorte".equals(entry.getKey())) {
                    try {
                        LocalTime horarioCorte = LocalTime.parse(entry.getValue());
                        queryBuilder.append(" c.").append(campoMapeado).append(" = :").append(entry.getKey());
                        filtros.put(entry.getKey(), horarioCorte.toString());
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Formato de horário inválido para 'horarioCorte'. Use o formato HH:mm:ss.");
                    }
                } else if ("diaHoraRegistro".equals(entry.getKey())) {
                    try {
                        LocalDateTime diaHoraRegistro = LocalDateTime.parse(entry.getValue());
                        queryBuilder.append(" c.").append(campoMapeado).append(" = :").append(entry.getKey());
                        filtros.put(entry.getKey(), diaHoraRegistro.toString());
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Formato de data e hora inválido para 'diaHoraRegistro'. Use o formato yyyy-MM-ddTHH:mm:ss.");
                    }
                } else if ("statusColeta".equals(entry.getKey())) {
                    queryBuilder.append(" ").append(campoMapeado).append(" = :").append(entry.getKey());
                    filtros.put(entry.getKey(), String.valueOf(StatusColeta.fromAlias(entry.getValue()).getCodigo()));
                } else if ("romaneio".equals(entry.getKey())) {
                    queryBuilder.append(" ").append(campoMapeado).append(" = :").append(entry.getKey());
                } else {
                    queryBuilder.append(" ").append(campoMapeado).append(" LIKE :").append(entry.getKey());
                    filtros.put(entry.getKey(), "%" + entry.getValue() + "%");
                }
                isFirst = false;
            }
        }

        Query query = entityManager.createQuery(queryBuilder.toString());

        for (Map.Entry<String, String> entry : filtros.entrySet()) {
            String campoMapeado = CAMPO_MAPEADO.get(entry.getKey());
            if (campoMapeado != null) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }

        return query.getResultList();
    }
}
