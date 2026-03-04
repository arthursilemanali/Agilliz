package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.models.Colaborador;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Repository
public class ColaboradorRepositoryCustom {

    private static final Map<String, String> MAPEAMENTO_CAMPOS = Map.of(
            "nomeColaborador", "nome_colaborador",
            "cpf", "cpf",
            "rg", "rg",
            "dataNascimento", "data_nascimento",
            "dataAdmissao", "data_admissao",
            "emailColaborador", "email_colaborador",
            "telefoneColaborador", "telefone_colaborador",
            "classeCarteira", "classe_carteira"
    );
    @PersistenceContext
    private EntityManager entityManager;

    public List<Colaborador> findByFiltro(String campo, String valor) {
        String campoBanco = MAPEAMENTO_CAMPOS.get(campo);

        if (campoBanco == null) {
            throw new IllegalArgumentException("Campo inválido: " + campo);
        }

        String sql;
        Query query;

        if (campoBanco.matches("(?i)data_nascimento|data_admissao")) {
            DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatterSaida = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate dataFormatada = LocalDate.parse(valor, formatterEntrada);

            sql = "SELECT * FROM funcionario WHERE " + campoBanco + " = :valor";
            query = entityManager.createNativeQuery(sql, Colaborador.class);

            query.setParameter("valor", dataFormatada.format(formatterSaida));
        } else {
            sql = "SELECT * FROM funcionario WHERE " + campoBanco + " LIKE :valor";
            query = entityManager.createNativeQuery(sql, Colaborador.class);
            query.setParameter("valor", "%" + valor + "%");
        }

        return query.getResultList();
    }
}
