package com.hjin.photophoto.domain.view;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ViewRepository extends JpaRepository<View, Long> {

    Optional<View> findByUserId(Long userId);

}
