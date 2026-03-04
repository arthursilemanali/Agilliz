package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.destinatario.DestinatarioRequest;
import agiliz.projetoAgiliz.dto.destinatario.DestinatarioResponse;
import agiliz.projetoAgiliz.models.Destinatario;
import agiliz.projetoAgiliz.repositories.IDestinatarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinatarioService {
    private final IDestinatarioRepository repository;

    public Destinatario getPorId(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseEntityException(HttpStatusCode.valueOf(404), "Não existe destinatario com o id fornecido", 404));
    }

    public DestinatarioResponse cadastrar(DestinatarioRequest dto) {
        var destinatario = new Destinatario();
        BeanUtils.copyProperties(dto, destinatario);
        return new DestinatarioResponse(repository.save(destinatario));
    }

    public Page<DestinatarioResponse> getDestinatariosPaginados(int pagina, int size) {
        return repository.findAllResponse(PageRequest.of(pagina, size, Sort.by("nome")));
    }

    public DestinatarioResponse getResponsePorId(UUID id) {
        return new DestinatarioResponse(getPorId(id));
    }

    public DestinatarioResponse alterar(UUID id, DestinatarioRequest dto) {
        var destinatario = getPorId(id);
        BeanUtils.copyProperties(dto, destinatario);
        return new DestinatarioResponse(repository.save(destinatario));
    }

    public void deletarPorId(UUID id) {
        getPorId(id);
        repository.deleteById(id);
    }

    public List<Destinatario> buscarPorIds(Set<UUID> ids) {
        return repository.findAllById(ids);
    }
}
