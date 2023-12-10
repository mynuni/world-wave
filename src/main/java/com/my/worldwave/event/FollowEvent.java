package com.my.worldwave.event;

import com.my.worldwave.member.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FollowEvent extends ApplicationEvent {

    private final Member actionMember;
    private final Member targetMember;

    public FollowEvent(Object source, Member actionMember, Member targetMember) {
        super(source);
        this.actionMember = actionMember;
        this.targetMember = targetMember;
    }

}
