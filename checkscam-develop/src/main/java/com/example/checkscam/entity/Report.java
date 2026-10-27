package com.example.checkscam.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report extends BaseEntity {

    @Column(columnDefinition = "text")
    private String info;

    @Column(name = "info_2")
    private String info2; // tên chủ tài khoản ( nếu báo cáo stk )

    @Column(name = "info_3")
    private String info3; // tên ngân hàng ( nếu báo cáo tk)

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "status")
    private Integer status;

    @Column(name = "type")
    private Integer type;

    @Column(name = "id_scam_type_before_handle")
    private Long idScamTypeAfterHandle;

    @Column(name = "email_author_report")
    private String emailAuthorReport;

    @Column(name = "reason")
    private String reason;

    @Column(name = "info_description")
    private String infoDescription;

    @Column(name = "date_report")
    private LocalDateTime dateReport;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Attachment> attachments;
}

