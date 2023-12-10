package com.my.worldwave.event.domain;

import com.my.worldwave.member.entity.Member;
import com.my.worldwave.util.domain.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Activity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_member_id")
    private Member actionMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_member_id")
    private Member targetMember;

    private Long targetId;

    @ColumnDefault("false")
    private boolean isRead;

    @Builder
    public Activity(ActivityType activityType, Member actionMember, Member targetMember, Long targetId) {
        this.activityType = activityType;
        this.actionMember = actionMember;
        this.targetMember = targetMember;
        this.targetId = targetId;
    }

    public void read() {
        this.isRead = true;
    }

}
