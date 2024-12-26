package com.soothee.member.domain;

import com.soothee.common.constants.*;
import com.soothee.common.domain.Domain;
import com.soothee.common.domain.TimeEntity;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.custom.valid.SootheeValidation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends TimeEntity implements Domain {
    /** 회원 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberId;

    /** 회원 이메일 */
    @Column(name = "email", nullable = false)
    private String email;

    /** 회원 닉네임 */
    @Column(name = "name", nullable = false, length = 6)
    private String name;

    /** 다크 모드 */
    @Column(name = "is_dark", nullable = false, length = 1)
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
        this.email = email;
        this.name = name;
        this.isDark = BooleanYN.N.toString();
        this.isDelete = BooleanYN.N.toString();
        this.snsType = snsType;
        this.oauth2ClientId = oauth2ClientId;
        this.role = Role.USER;
    }

    /**
     * 회원 정보 수정
     * 1. 입력된 수정할 닉네임이 없거나 올바르지 않는 경우 Exception 발생
     *
     * @param updateName 바꿀 회원 닉네임
     */
    public void updateName(String updateName) throws IncorrectValueException, NullValueException {
        SootheeValidation.checkName(updateName);
        this.name = updateName;
    }

    /**
     * 회원 화면 모드 수정
     *
     * @param isDark 바꿀 화면 모드가 다크모드면 Y 아니면 N
     */
    public void updateDarkModeYN(BooleanYN isDark) {
        this.isDark = isDark.toString();
    }

    /** 회원 삭제 */
    public void deleteMember() {
        this.isDelete = BooleanYN.Y.toString();
    }

    @Override
    public Long getId() {
        return memberId;
    }

    /**
     * valid
     * 1. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     */
    public void valid() throws IncorrectValueException, NullValueException {
        SootheeValidation.checkDomainId(getMemberId(), DomainType.MEMBER);
        validNew();
    }

    /**
     * valid
     * 1. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     */
    public void validNew() throws IncorrectValueException, NullValueException {
        SootheeValidation.checkEmail(getEmail());
        SootheeValidation.checkName(getName());
        SootheeValidation.checkBoolean(getIsDark(), BooleanType.DARK_MODE);
        SootheeValidation.checkBoolean(getIsDelete(), BooleanType.DELETE);
        SootheeValidation.checkSnsType(getSnsType());
        SootheeValidation.checkNullForNecessaryString(getOauth2ClientId(), StringType.OAUTH2);
        SootheeValidation.checkRole(getRole());
    }
}