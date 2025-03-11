package com.example.lettuce.domain.user.aggregate;

import com.example.lettuce.domain.user.command.dto.UpdateUserCommand;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    protected User user;

    @Column(name = "nickname", unique = true, length = 20, nullable = false, columnDefinition = "VARCHAR(20) COMMENT '회원 닉네임'")
    private String nickname;

    @Column(name = "profile_image_url", length = 255, columnDefinition = "VARCHAR(255) COMMENT '회원 프로필 이미지 URL'")
    private String profileImageUrl;

    public void updateBaseProfile(UpdateUserCommand profileCommand) {
        this.nickname = profileCommand.getNickname();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}