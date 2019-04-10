package com.lab.meet.Repository;

import com.lab.meet.Model.GroupSummary;
import com.lab.meet.Model.GroupTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
public interface GroupSummaryRepository extends JpaRepository<GroupSummary, Long>, JpaSpecificationExecutor<GroupSummary> {
    GroupSummary findGroupSummaryById(Long id);

    Set<GroupSummary> findGroupSummariesByTaskId(Long id);

    @Modifying
    @Transactional
    @Query(value = "delete  from GroupSummary b where b.id = ?1")
    int deleteGroupSummaryById(Long id);

    @Modifying
    @Transactional
    @Query(value = "delete from GroupSummary b where b.user.id = ?1")
    int deleteGroupSummaryByUserId(Long id);

    @Query(value = "select task_id from group_summary_table where id = ?1", nativeQuery = true)
    Long findTaskIdBySummaryId(Long id);
}
