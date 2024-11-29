package com.soothee.member.domain;

import com.soothee.common.constants.Role;
import com.soothee.common.constants.SnsType;
import com.soothee.common.domain.TimeEntity;
import com.soothee.common.exception.MyErrorMsg;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends TimeEntity {
    /** 회원 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberId;

    /** 회원 이메일 */
    @Column(name = "email", nullable = false)
    private String email;

    /** 회원 닉네임 */
    @Column(name = "name", nullable = false)
    private String name;

    /** 다크 모드 */
    @Column(name = "is_dark", nullable = false)
    private String isDark;

    /** 소프트 삭제 */
    @Column(name = "is_delete", nullable = false, length = 1)
    private String isDelete;

    /** 가입한 SNS */
    @Enumerated(EnumType.STRING)
    @Column(name = "sns_type", nullable = false)
    private SnsType snsType;

    /** OAuth2 ID */
    @Column(name = "oauth2_client_id", nullable = false)
    private String oauth2ClientId;

    /** 회원 등급 */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Builder
    public Member(String email, String name, SnsType snsType, String oauth2ClientId) {
        Assert.notNull(email, MyErrorMsg.NULL_VALUE.makeValue("아이디(이메일) "));
        Assert.notNull(name, MyErrorMsg.NULL_VALUE.makeValue("닉네임 "));
        Assert.notNull(snsType, MyErrorMsg.NULL_VALUE.makeValue("SNS 타입 "));
        Assert.notNull(oauth2ClientId, MyErrorMsg.NULL_VALUE.makeValue("OAuth2 구분 "));
        this.email = email;
        this.name = name;
        this.isDark = "N";
        this.isDelete = "N";
        this.snsType = snsType;
        this.oauth2ClientId = oauth2ClientId;
        this.role = Role.USER;
    }

    /**
     * 회원 정보 수정</hr>
     *
     * @param updateName String: 바꿀 회원 닉네임
     */
    public void updateName(String updateName) {
        this.name = updateName;
    }

    /**
     * 회원 화면 모드 수정</hr>
     *
     * @param isDark String: 바꿀 화면 모드가 다크모드면 Y 아니면 N
     */
    public void updateDarkModeYN(String isDark) {
        this.isDark = isDark;
    }

    /**
     * 회원 삭제</hr>
     */
    public void deleteMember() {
        this.isDelete = "Y";
    }
}