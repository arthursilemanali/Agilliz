package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.setor.BonificacaoLider;
import agiliz.projetoAgiliz.dto.setor.SetorRequest;
import agiliz.projetoAgiliz.dto.setor.SetorResponse;
import agiliz.projetoAgiliz.models.Colaborador;
import agiliz.projetoAgiliz.models.Pacote;
import agiliz.projetoAgiliz.models.Setor;
import agiliz.projetoAgiliz.repositories.ISetorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SetorService {
    private final ISetorRepository repository;
    private final ColaboradorService colaboradorService;

    public SetorResponse inserir(SetorRequest dto) {
        validarLimiteCEP(dto);
        Setor setor = new Setor();
        BeanUtils.copyProperties(dto, setor);

        if(dto.fkLiderSetor() != null) {
            setor.setLiderSetor(getLiderSetor(dto.fkLiderSetor()));
        }

        return new SetorResponse(repository.save(setor));
    }

    public List<SetorResponse> getAll() {
        return repository.findAllResponse();
    }

    public SetorResponse getResponsePorId(UUID id) {
        return new SetorResponse(getPorId(id));
    }

    public Setor getPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(
                        () -> new ResponseEntityException(HttpStatus.NOT_FOUND, "Setor não encontrado", 404)
                );
    }

    public SetorResponse alterar(SetorRequest dto, UUID id) {
        validarLimiteCEP(dto);
        Setor setor = getPorId(id);
        BeanUtils.copyProperties(dto, setor);

        if(dto.fkLiderSetor() != null && setor.getLiderSetor().getIdColaborador() != dto.fkLiderSetor()) {
            setor.setLiderSetor(getLiderSetor(dto.fkLiderSetor()));
        }

        return new SetorResponse(repository.save(setor));
    }

    public void alterarLiderSetor(UUID id, UUID idColaborador) {
        Setor setor = getPorId(id);
        setor.setLiderSetor(getLiderSetor(idColaborador));
        repository.save(setor);
    }

    public void deletar(UUID id) {
        if(!repository.existsById(id)) {
            throw new ResponseEntityException(HttpStatus.NOT_FOUND, "Setor não encontrado", 404);
        }

        repository.deleteById(id);
    }

    public void associarSetor(Pacote pacote) {
        if(pacote.getDestinatario() == null) {
            return;
        }

        Optional<Setor> setorAssociado = repository.findByCepRange(pacote.getDestinatario().getCep());

        if(setorAssociado.isEmpty()) {
            return;
        }

        pacote.setSetor(setorAssociado.get());
    }

    private Colaborador getLiderSetor(UUID idColaborador) {
        if(repository.existsSetorByLider(idColaborador)) {
            throw new ResponseEntityException(HttpStatus.NOT_FOUND, "O colaborador fornecido já lidera um setor", 404);
        }

        return colaboradorService.getPorId(idColaborador);
    }

    private void validarLimiteCEP(SetorRequest dto) {
        if(dto.limiteInferiorCep().length() != dto.limiteSuperiorCep().length()) {
            throw new ResponseEntityException(
                    HttpStatus.BAD_REQUEST,
                    "O limite inferior não possui a mesma quantidade de dígitos do limite superior",
                    400
            );
        }

        if(Integer.parseInt(dto.limiteInferiorCep()) >= Integer.parseInt(dto.limiteSuperiorCep())) {
            throw new ResponseEntityException(
                    HttpStatus.BAD_REQUEST,
                    "O limite inferior não pode ser maior ou igual ao limite superior",
                    400
            );
        }

        if(repository.existsByRangeCep(dto.limiteInferiorCep(), dto.limiteSuperiorCep())) {
            throw new ResponseEntityException(
                    HttpStatus.BAD_REQUEST,
                    "Já existe um setor cadastrado com esse limite inferior ou superior",
                    400
            );
        }
    }

    public List<BonificacaoLider> getBonificacaoLideres(LocalDate vigenciaInicio, LocalDate vigenciaFim) {
        return repository.findBonificacaoVigenteLideres(
                LocalDateTime.of(vigenciaInicio, LocalTime.MIDNIGHT),
                LocalDateTime.of(vigenciaFim, LocalTime.MIDNIGHT)
        );
    }
}
