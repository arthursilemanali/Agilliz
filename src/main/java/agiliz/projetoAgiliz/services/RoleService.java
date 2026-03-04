package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.enums.PermissoesEnum;
import agiliz.projetoAgiliz.models.Colaborador;
import agiliz.projetoAgiliz.models.Role;
import agiliz.projetoAgiliz.repositories.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {
    @Autowired
    private IRoleRepository roleRepository;

    public Role verificarInsercao(Integer codigoPermissao) {
        var permissao = PermissoesEnum.fromCodigo(codigoPermissao);

        return roleRepository.findByNome(permissao)
                .orElseGet(() -> roleRepository.save(new Role(permissao)));
    }

    public void associarPermissao(Integer codigo, Colaborador colaborador) {
        var role = verificarInsercao(codigo);

        if (role.getColaboradores() == null) {
            role.setColaboradores(new HashSet<>());
        }

        if (colaborador.getRoles() == null) {
            colaborador.setRoles(new HashSet<>());
        }

        // Verifica se o colaborador já tem essa role antes de adicionar
        if (!role.getColaboradores().contains(colaborador)) {
            role.getColaboradores().add(colaborador);
        }

        if (!colaborador.getRoles().contains(role)) {
            colaborador.getRoles().add(role);
        }

        roleRepository.save(role);
    }
}