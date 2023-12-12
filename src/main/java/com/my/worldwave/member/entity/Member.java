package com.my.worldwave.member.entity;

import com.my.worldwave.util.domain.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "member")
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, length = 50)
    private String email;

    @Column(length = 100)
    private String password;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(length = 2)
    private String country;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "register_type")
    private RegisterType registerType;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private Gender gender;

    @Column(name = "age_range",length = 2)
    private Integer ageRange;

    @Column(name = "oauth2_access_token")
    private String oAuth2AccessToken;

    @Column(name = "is_activated", columnDefinition = "boolean default true")
    private boolean isActivated;

    public Member(String email, Role role) {
        this.email = email;
        this.role = role;
    }

    @Builder
    public Member(String email, String password, String nickname, String country, Role role, RegisterType registerType, ProfileImage profileImage, Gender gender, Integer ageRange, String oAuth2AccessToken) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.country = country;
        this.role = role;
        this.registerType = registerType;
        this.profileImage = profileImage;
        this.gender = gender;
        this.ageRange = ageRange;
        this.oAuth2AccessToken = oAuth2AccessToken;
        this.isActivated = true;
    }

    public void updateInfo(String nickname, Integer ageRange, Gender gender, String country) {
        this.nickname = nickname;
        this.ageRange = ageRange;
        this.gender = gender;
        this.country = country;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    // 리소스 서버와 연동 해제를 위해 OAuth2AccessToken을 저장해야 함
    public void updateOAuth2AccessToken(String accessToken) {
        this.oAuth2AccessToken = accessToken;
    }

    // 회원 비활성화 편의 메서드
    public void inactivate() {
        this.isActivated = false;
        this.email = null;
        this.password = null;
        this.oAuth2AccessToken = null;
        this.profileImage = null;
    }

    public void updateProfileImage(ProfileImage newProfileImage) {
        this.profileImage = newProfileImage;
    }

}
