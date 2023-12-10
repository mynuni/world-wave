package com.my.worldwave.event.service;

import com.my.worldwave.event.domain.Activity;
import com.my.worldwave.event.dto.ActivityResponse;
import com.my.worldwave.event.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    @Transactional(readOnly = true)
    public List<ActivityResponse> getActivities(Long id) {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        List<Activity> activities = activityRepository.findAllByMemberId(id, pageRequest);
        return activities.stream()
                .map(ActivityResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public int getUnreadCount(Long memberId) {
        return activityRepository.countByMemberIdAndIsRead(memberId, false);
    }


    @Transactional
    public void readActivity(Long activityId) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(EntityNotFoundException::new);
        activity.read();
    }

    @Transactional
    public void readAllActivity(Long memberId) {
        activityRepository.updateReadAllByMemberId(memberId);
    }


}
