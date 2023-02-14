package com.hjin.photophoto.domain.view;

import com.hjin.photophoto.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class View extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_id")
    private Long viewId;

    @Column(nullable = false, name = "user_id")
    private Long userId;

    @Column(nullable = false, name = "count")
    private int count;

    @Builder
    public View(Long viewId, Long userId, int count) {
        this.viewId = viewId;
        this.userId = userId;
        this.count = count;
    }

    public void updateCount(int count) {this.count = count;}
}
