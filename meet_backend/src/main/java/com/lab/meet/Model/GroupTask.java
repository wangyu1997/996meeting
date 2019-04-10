package com.lab.meet.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "group_task_table")
public class GroupTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String file_ids;
    private String head_img;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "task_user_table",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    private Long publish_time = new Date().getTime();

    private Long start_time = new Date().getTime();

    @Transient
    private Set<Long> user_ids = new HashSet<>();

    public Set<Long> getUser_ids() {
        Set<Long> res = new HashSet<>();
        for (User user : users)
            res.add(user.getId());
        return res;
    }
}
