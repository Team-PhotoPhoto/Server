package com.hjin.photophoto.web.posts.dto;

import com.hjin.photophoto.domain.subjects.Subjects;

public class SubjectsResponse {

    private Long subjectId;
    private String contents;

    public SubjectsResponse(Subjects entity) {
        this.subjectId = entity.getSubjectId();
        this.contents = entity.getContents();
    }
}
