package com.lab.meet.Repository;

import com.lab.meet.Model.PersonMeet;
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
public interface PersonMeetRepository extends JpaRepository<PersonMeet, Long>, JpaSpecificationExecutor<PersonMeet> {

    PersonMeet findPersonMeetById(Long id);

    @Modifying
    @Transactional
    @Query(value = "delete  from PersonMeet b where b.id = ?1")
    int deletePersonMeetById(Long id);

    @Modifying
    @Transactional
    @Query(value = "delete from PersonMeet b where b.user.id = ?1")
    int deletePersonMeetByUserId(Long id);
}
