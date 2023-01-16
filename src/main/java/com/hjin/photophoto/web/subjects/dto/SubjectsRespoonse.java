package com.hjin.photophoto.web.subjects.dto;

import com.hjin.photophoto.domain.subject.Subject;

public class SubjectsRespoonse {
    private String contents;

    public SubjectsRespoonse(Subject entity) {
        this.contents = entity.getContents();
    }
}
