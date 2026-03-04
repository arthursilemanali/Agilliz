package agiliz.projetoAgiliz.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.models.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import agiliz.projetoAgiliz.dto.colaborador.LoginDTO;
import agiliz.projetoAgiliz.dto.colaborador.UserDetailsDTO;
import agiliz.projetoAgiliz.models.Colaborador;
import agiliz.projetoAgiliz.repositories.IColaboradorRepository;

@Service
public class AutenticacaoService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(AutenticacaoService.class);

    @Autowired
    IColaboradorRepository colaboradorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("[AutenticacaoService.loadUserByUsername] início");

        Colaborador user = colaboradorRepository.findyEmailColaborador(username)
                .orElseThrow(
                        () -> new ResponseEntityException(HttpStatus.NOT_FOUND
                                ,"Usuário não encontrado"
                                , 404)
                );
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getNome()))
                .collect(Collectors.toList());

        logger.info("[AutenticacaoService.loadUserByUsername] Colaborador {} logado às {} ",user.getEmailColaborador(), LocalDateTime.now());
        return new UserDetailsDTO(user.getNomeColaborador(), user.getEmailColaborador(), authorities, user.getSenhaColaborador());
    }

    public void criar(Colaborador usuarioLoginDTO) {
        logger.info("[AutenticacaoService.criar] inicio");
        final Colaborador novoColaborador = colaboradorRepository.save(usuarioLoginDTO);
    }

}
