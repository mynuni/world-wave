package com.my.worldwave.member.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class ProfileImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalFileName;
    private String filePath;
    private long fileSize;

//    @OneToOne
//    @JoinColumn(name = "member_id")
//    private Member member;

    @Builder
    public ProfileImg(String originalFileName, String filePath, long fileSize) {
        this.originalFileName = originalFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
//        this.member = member;
    }

}
