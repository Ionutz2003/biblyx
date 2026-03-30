package com.ionut.licenta.service;

import com.ionut.licenta.dto.FollowDTO;
import com.ionut.licenta.entity.Follow;
import com.ionut.licenta.entity.User;
import com.ionut.licenta.repository.FollowRepository;
import com.ionut.licenta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public String follow(Long followerId, Long followedId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (followerId.equals(followedId))
            throw new RuntimeException("Cannot follow yourself!");
        if (followRepository.existsByFollowerAndFollowed(follower, followed))
            throw new RuntimeException("Already following!");

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);
        followRepository.save(follow);
        return "Followed successfully!";
    }

    public String unfollow(Long followerId, Long followedId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Follow follow = followRepository.findByFollowerAndFollowed(follower, followed)
                .orElseThrow(() -> new RuntimeException("Not following!"));
        followRepository.delete(follow);
        return "Unfollowed successfully!";
    }

    public List<FollowDTO> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return followRepository.findByFollowed(user)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<FollowDTO> getFollowing(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return followRepository.findByFollower(user)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private FollowDTO convertToDTO(Follow follow) {
        return new FollowDTO(
                follow.getId(),
                follow.getFollower().getUsername(),
                follow.getFollowed().getUsername(),
                follow.getCreatedAt()
        );
    }
}