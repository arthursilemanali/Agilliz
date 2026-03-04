package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.vigenciaPagamentoAut.VigenciaPagamentoAutReq;
import agiliz.projetoAgiliz.dto.vigenciaPagamentoAut.VigenciaPagamentoAutRes;
import agiliz.projetoAgiliz.models.VigenciaPagamentoAut;
import agiliz.projetoAgiliz.repositories.IVigenciaPagamentoAutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VigenciaPagamentoAutService {
    private final IVigenciaPagamentoAutRepository repository;

    public VigenciaPagamentoAutRes inserir(VigenciaPagamentoAutReq dto) {
        if(repository.jaCadastrado()) {
            throw new ResponseEntityException(HttpStatus.BAD_REQUEST, "Já existe uma vigência cadastrada", 400);
        }

        VigenciaPagamentoAut vigencia = new VigenciaPagamentoAut();
        vigencia.setDiaSemana(dto.diaSemana());
        return new VigenciaPagamentoAutRes(repository.save(vigencia));
    }

    public Optional<VigenciaPagamentoAut> getVigencia() {
        return repository.findVigenciaPagamentoAut();
    }

    public Optional<VigenciaPagamentoAutRes> getVigenciaResponse() {
        Optional<VigenciaPagamentoAut> vigencia = getVigencia();
        return vigencia.map(VigenciaPagamentoAutRes::new);
    }

    public void alterarDiaSemana(VigenciaPagamentoAutReq dto) {
        Optional<VigenciaPagamentoAut> vigenciaOpt = getVigencia();

        if(vigenciaOpt.isEmpty()) {
            throw new ResponseEntityException(
                    HttpStatus.BAD_REQUEST,
                    "Cadastre uma vigência primeiro para realizar uma alteração",
                    400
            );
        }

        VigenciaPagamentoAut vigencia = vigenciaOpt.get();
        vigencia.setDiaSemana(dto.diaSemana());
        repository.save(vigencia);
    }

    public void deletar() {
        if(!repository.jaCadastrado()) {
            throw new ResponseEntityException(HttpStatus.BAD_REQUEST, "Vigência não encontrada", 400);
        }

        repository.deleteAll();
    }

    public DayOfWeek getVigenciaDiaSemana() {
        Optional<VigenciaPagamentoAut> vigenciaOpt = getVigencia();
        return vigenciaOpt.isEmpty() ? DayOfWeek.MONDAY : vigenciaOpt.get().getDayOfWeek();
    }
}
