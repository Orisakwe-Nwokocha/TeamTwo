package com.prunny.project_service.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;
import static java.time.LocalDateTime.now;
import static lombok.AccessLevel.NONE;

@Getter
@Setter
@Entity
@Table(name = "projects")
@ToString
public class Project {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Column(nullable = false)
    private String manager;

    @ElementCollection
    @CollectionTable(
            name = "team_members",
            joinColumns = @JoinColumn(name = "project_id")
    )
    @Column(name = "member")
    private Set<String> teamMembers;

    @ElementCollection
    @CollectionTable(
            name = "task_ids",
            joinColumns = @JoinColumn(name = "project_id")
    )
    @Column(name = "task_id")
    private Set<Long> taskIDs;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Setter(NONE)
    private LocalDateTime dateCreated;
    @Setter(NONE)
    private LocalDateTime dateUpdated;


    @PrePersist
    private void setDateCreated() {
        this.dateCreated = now();
    }

    @PreUpdate
    private void setDateUpdated() {
        this.dateUpdated = now();
    }
}
