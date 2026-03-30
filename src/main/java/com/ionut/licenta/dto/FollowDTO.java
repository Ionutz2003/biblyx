package com.ionut.licenta.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowDTO {
    private Long id;
    private String followerUsername;
    private String followedUsername;
    private LocalDateTime createdAt;
}