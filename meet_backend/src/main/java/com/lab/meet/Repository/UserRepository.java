package com.lab.meet.Repository;


import com.lab.meet.Model.GroupTask;
import com.lab.meet.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findUserByUsername(String username);

    User findSysUserById(Long id);

    @Modifying
    @Transactional
    @Query(value = "delete from User b where b.id = ?1")
    int deleteUserById(Long id);

}
