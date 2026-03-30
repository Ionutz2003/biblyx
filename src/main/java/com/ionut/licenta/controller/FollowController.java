package com.ionut.licenta.controller;

import com.ionut.licenta.dto.FollowDTO;
import com.ionut.licenta.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{followerId}/follow/{followedId}")
    public ResponseEntity<String> follow(
            @PathVariable Long followerId,
            @PathVariable Long followedId) {
        return ResponseEntity.ok(followService.follow(followerId, followedId));
    }

    @DeleteMapping("/{followerId}/unfollow/{followedId}")
    public ResponseEntity<String> unfollow(
            @PathVariable Long followerId,
            @PathVariable Long followedId) {
        return ResponseEntity.ok(followService.unfollow(followerId, followedId));
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<FollowDTO>> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowers(userId));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<FollowDTO>> getFollowing(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowing(userId));
    }
}