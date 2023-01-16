package com.hjin.photophoto.service.subjects;

import com.hjin.photophoto.domain.subject.SubjectRepository;
import com.hjin.photophoto.web.posts.dto.InboxResponse;
import com.hjin.photophoto.web.subjects.dto.SubjectsRespoonse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SubjectsService {
    private final SubjectRepository subjectRepository;

    @Transactional(readOnly = true)
    public List<SubjectsRespoonse> findAll() {
        return subjectRepository.findAll()
                .stream()
                .map(SubjectsRespoonse::new)
                .collect(Collectors.toList());
    }


}
