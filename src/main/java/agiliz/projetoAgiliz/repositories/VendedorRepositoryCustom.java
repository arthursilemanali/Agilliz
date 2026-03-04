package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.models.Vendedor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class VendedorRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Vendedor> buscarPorFiltros(Map<String, String> filtros) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vendedor> cq = cb.createQuery(Vendedor.class);
        Root<Vendedor> root = cq.from(Vendedor.class);

        List<Predicate> predicates = new ArrayList<>();

        filtros.forEach((campo, valor) -> {
            if (campo.equals("horarioCorte")) {
                try {
                    LocalTime horario = LocalTime.parse(valor, DateTimeFormatter.ofPattern("HH:mm"));
                    predicates.add(cb.equal(root.get(campo), horario));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Formato de horário inválido. Use HH:mm " + e.getMessage());
                }
            } else if (campo.equals("retornoTotal")) {
                if (valor.startsWith(">") || valor.startsWith("<")) {
                    String operador = valor.substring(0, 1);
                    Double numero = Double.valueOf(valor.substring(1));
                    if (operador.equals(">")) {
                        predicates.add(cb.greaterThan(root.get(campo), numero));
                    } else {
                        predicates.add(cb.lessThan(root.get(campo), numero));
                    }
                } else {
                    predicates.add(cb.equal(root.get(campo), Double.valueOf(valor)));
                }
            } else {
                predicates.add(cb.like(root.get(campo), "%" + valor + "%"));
            }
        });

        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }
}