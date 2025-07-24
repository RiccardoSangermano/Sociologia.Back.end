package it.epicode.Sociologia.Back.end.repository;

import it.epicode.Sociologia.Back.end.enums.Roles;
import it.epicode.Sociologia.Back.end.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {


    Optional<Role> findByNome(Roles nome);

}