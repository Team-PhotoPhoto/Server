package com.hjin.photophoto.domain.subject;

import com.hjin.photophoto.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Subject extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long subjectId;

    @Column(length = 200, nullable = false)
    private String contents;

    @Builder
    public Subject(Long subjectId, String contents) {
        this.contents = contents;
        this.subjectId = subjectId;
    }
}
