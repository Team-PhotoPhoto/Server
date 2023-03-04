package com.hjin.photophoto.config.auth.dto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MailRequest {
    private Long receiverUserId;
    private String title;

    @Builder
    public MailRequest(Long receiverUserId, String title) {
        this.receiverUserId = receiverUserId;
        this.title = title;
    }
}
