package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.membroSetor.MembroSetorRequest;
import agiliz.projetoAgiliz.dto.membroSetor.MembroSetorResponse;
import agiliz.projetoAgiliz.models.MembroSetor;
import agiliz.projetoAgiliz.repositories.IMembroSetorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembroSetorService {
    private final IMembroSetorRepository repository;
    private final ColaboradorService colaboradorService;
    private final SetorService setorService;

    public MembroSetorResponse inserir(MembroSetorRequest dto) {
        MembroSetor membro = new MembroSetor();

        membro.setColaborador(colaboradorService.getPorId(dto.fkColaborador()));
        membro.setSetor(setorService.getPorId(dto.fkSetor()));

        return new MembroSetorResponse(repository.save(membro));
    }

    public List<MembroSetorResponse> getAll() {
        return repository.findAllResponse();
    }

    public MembroSetorResponse getResponsePorId(UUID id) {
        return new MembroSetorResponse(getPorId(id));
    }

    public MembroSetor getPorId(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseEntityException(HttpStatus.NOT_FOUND, "Mebro de setor não encontrado", 404)
        );
    }

    public MembroSetorResponse alterar(MembroSetorRequest dto, UUID id) {
        MembroSetor membro = getPorId(id);

        membro.setColaborador(colaboradorService.getPorId(dto.fkColaborador()));
        membro.setSetor(setorService.getPorId(dto.fkSetor()));

        return new MembroSetorResponse(repository.save(membro));
    }

    public void alterarSetor(UUID id, UUID setorId) {
        MembroSetor membro = getPorId(id);
        membro.setSetor(setorService.getPorId(setorId));
        repository.save(membro);
    }

    public void deletar(UUID id) {
        if(!repository.existsById(id)) {
            throw new ResponseEntityException(HttpStatus.NOT_FOUND, "Mebro de setor não encontrado", 404);
        }

        repository.deleteById(id);
    }
}
