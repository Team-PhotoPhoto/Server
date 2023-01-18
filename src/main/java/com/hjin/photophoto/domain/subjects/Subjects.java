package com.hjin.photophoto.domain.subjects;

import com.hjin.photophoto.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Subjects extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long subjectId;

    @Column(length = 200, nullable = false)
    private String contents;

    @Builder
    public Subjects(Long subjectId, String contents) {
        this.contents = contents;
        this.subjectId = subjectId;
    }
}
