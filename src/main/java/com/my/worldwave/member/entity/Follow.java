package com.my.worldwave.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "follow",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"follower_id", "following_id"}) // UNIQUE CONSTRAINT
        })
@Entity
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    // 팔로워 (A가 B를 팔로우한 경우에서의 A)
    @ManyToOne
    @JoinColumn(name = "follower_id")
    private Member follower;

    // 팔로잉 (A가 팔로우하는 대상, B가 A에 의해 팔로우를 당한 경우에서 B)
    @ManyToOne
    @JoinColumn(name = "following_id")
    private Member following;

    public Follow(Member follower, Member following) {
        this.follower = follower;
        this.following = following;
    }

}
