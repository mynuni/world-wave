package com.my.worldwave.member.repository;

import com.my.worldwave.member.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImgRepository extends JpaRepository<ProfileImage, Long> {
}
