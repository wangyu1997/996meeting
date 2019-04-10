package com.lab.meet.Repository;

import com.lab.meet.Model.GroupTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
public interface GroupTaskRepository extends JpaRepository<GroupTask, Long>, JpaSpecificationExecutor<GroupTask> {
    GroupTask findGroupTaskById(Long id);

    @Modifying
    @Transactional
    @Query(value = "delete from GroupTask b where b.id = ?1")
    int deleteGroupTaskById(Long id);


    @Modifying
    @Transactional
    @Query(value = "delete from task_user_table where user_id = ?1", nativeQuery = true)
    int deleteTasksRelationByUserID(Long id);

    @Transactional
    @Query(value = "select * from group_task_table where id in (select task_id from task_user_table where user_id = ?1) " +
            "and title like ?2 and description like ?3 and publish_time between ?4 and ?5 and start_time between ?6 and ?7", nativeQuery = true)
    Page<GroupTask> findUsersGroupTasks(Long id, String title, String description,
                                        Long publish_time_start, Long publish_time_end,
                                        Long start_time_start, Long start_time_end, Pageable pageable);
}
