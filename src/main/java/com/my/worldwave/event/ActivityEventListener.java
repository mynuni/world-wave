package com.my.worldwave.event;

import com.my.worldwave.event.domain.Activity;
import com.my.worldwave.event.domain.ActivityType;
import com.my.worldwave.event.repository.ActivityRepository;
import com.my.worldwave.notification.dto.NotificationMessage;
import com.my.worldwave.notification.service.NotificationService;
import com.my.worldwave.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.my.worldwave.event.dto.NotificationMessages.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class ActivityEventListener {

    private final ActivityRepository activityRepository;
    private final NotificationService notificationService;

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleFollowEvent(FollowEvent followEvent) {
        eventCreator(ActivityType.FOLLOW, followEvent.getActionMember(), followEvent.getTargetMember(), followEvent.getTargetMember().getId());
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleLikeEvent(LikeEvent likeEvent) {
        eventCreator(ActivityType.LIKE, likeEvent.getActionMember(), likeEvent.getTargetMember(), likeEvent.getPostId());
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCommentEvent(CommentEvent commentEvent) {
        eventCreator(ActivityType.COMMENT, commentEvent.getActionMember(), commentEvent.getTargetMember(), commentEvent.getPostId());
    }

    private void eventCreator(ActivityType activityType, Member actionMember, Member targetMember, Long targetId) {
        // 스스로 발생시킨 이벤트 무시
        if (actionMember.getId().equals(targetMember.getId())) { return; }

        Activity activity = Activity.builder()
                .activityType(activityType)
                .actionMember(actionMember)
                .targetMember(targetMember)
                .targetId(targetId)
                .build();

        activityRepository.save(activity);
//        notificationService.sendNotification(targetMember.getId(), createNotificationMessage(activity));
        notificationService.publishNotification(new NotificationMessage(String.valueOf(targetMember.getId()), createNotificationMessage(activity)));
    }

    private String createNotificationMessage(Activity activity) {
        switch (activity.getActivityType()) {
            case FOLLOW:
                return NEW_FOLLOWER;
            case LIKE:
                return NEW_LIKE;
            case COMMENT:
                return NEW_COMMENT;
            default:
                throw new IllegalArgumentException();
        }
    }

}