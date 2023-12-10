package com.my.worldwave.event.repository;

import com.my.worldwave.event.domain.Activity;
import com.my.worldwave.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Query(value = "SELECT a FROM Activity a " +
            "JOIN FETCH a.actionMember m " +
            "LEFT JOIN FETCH m.profileImage p " +
            "WHERE a.targetMember.id = :targetMemberId ",
            countQuery = "SELECT COUNT(a) FROM Activity a WHERE a.targetMember.id = :targetMemberId")
    List<Activity> findAllByMemberId(@Param("targetMemberId") Long targetMemberId, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE Activity a " +
            "SET a.isRead = true " +
            "WHERE a.targetMember.id = :memberId " +
            "AND a.isRead = false")
    void updateReadAllByMemberId(Long memberId);

    @Query(value = "SELECT COUNT(a) FROM Activity a " +
            "WHERE a.targetMember.id = :memberId " +
            "AND a.isRead = :isRead")
    int countByMemberIdAndIsRead(Long memberId, boolean isRead);

}