package com.example.demodaraasapuwaservice.dao;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", schema = "panhinda")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    @OneToMany(mappedBy = "userByUserId")
    private List<ProcessBatchEntity> processBatchesById;
}
