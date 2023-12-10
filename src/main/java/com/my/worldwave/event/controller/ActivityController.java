package com.my.worldwave.event.controller;

import com.my.worldwave.event.dto.ActivityResponse;
import com.my.worldwave.event.service.ActivityService;
import com.my.worldwave.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api/activities")
@RequiredArgsConstructor
@RestController
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping("/{id}")
    @PreAuthorize("#id eq (authentication.principal.member.id)")
    public ResponseEntity<List<ActivityResponse>> getActivities(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ActivityResponse> activities = activityService.getActivities(userDetails.getMemberId());
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Integer> getUnreadCount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        int unreadCount = activityService.getUnreadCount(userDetails.getMemberId());
        return ResponseEntity.ok(unreadCount);
    }

    @PatchMapping("/read/{activityId}")
    public ResponseEntity<Void> readActivity(@PathVariable() Long activityId) {
        activityService.readActivity(activityId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/read/all")
    public ResponseEntity<Void> readAllActivity(@AuthenticationPrincipal CustomUserDetails userDetails) {
        activityService.readAllActivity(userDetails.getMemberId());
        return ResponseEntity.noContent().build();
    }

}
