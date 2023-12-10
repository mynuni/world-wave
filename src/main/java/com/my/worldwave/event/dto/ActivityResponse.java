package com.my.worldwave.event.dto;

import com.my.worldwave.event.domain.Activity;
import com.my.worldwave.event.domain.ActivityType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ActivityResponse {
    private Long id;
    private Long targetId;
    private ActivityType activityType;
    private Long actionMemberId;
    private String actionMemberNickname;
    private String actionMemberProfileImage;
    private boolean isRead;
    private LocalDateTime createdAt;

    @Builder
    public ActivityResponse(Long id, Long targetId, ActivityType activityType, Long actionMemberId, String actionMemberNickname, String actionMemberProfileImage, boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.targetId = targetId;
        this.activityType = activityType;
        this.actionMemberId = actionMemberId;
        this.actionMemberNickname = actionMemberNickname;
        this.actionMemberProfileImage = actionMemberProfileImage;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public static ActivityResponse of(Activity activity) {
        return ActivityResponse.builder()
                .id(activity.getId())
                .targetId(activity.getTargetId())
                .activityType(activity.getActivityType())
                .actionMemberId(activity.getActionMember().getId())
                .actionMemberNickname(activity.getActionMember().getNickname())
                .actionMemberProfileImage(activity.getActionMember().getProfileImage() == null ? null : activity.getActionMember().getProfileImage().getStoredFileName())
                .isRead(activity.isRead())
                .createdAt(activity.getCreatedAt())
                .build();
    }

}
