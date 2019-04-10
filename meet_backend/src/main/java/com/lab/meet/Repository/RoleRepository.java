package com.lab.meet.Repository;


import com.lab.meet.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findSysRoleByName(String name);

    Role findSysRoleById(Long id);
}
