package com.hjin.photophoto.domain.subject;

import com.hjin.photophoto.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
