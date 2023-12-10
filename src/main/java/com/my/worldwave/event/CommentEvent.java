package com.my.worldwave.event;

import com.my.worldwave.member.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CommentEvent extends ApplicationEvent {
    private final Member actionMember;
    private final Member targetMember;
    private final Long postId;

    public CommentEvent(Object source, Member actionMember, Member targetMember, Long postId) {
        super(source);
        this.actionMember = actionMember;
        this.targetMember = targetMember;
        this.postId = postId;
    }

}
