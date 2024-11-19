package com.soothee.member.domain;

import com.soothee.common.constants.SnsType;
import com.soothee.common.domain.TimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import org.thymeleaf.util.StringUtils;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends TimeEntity {
    /** 회원 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** 회원 이메일 */
    @Column(name = "email", nullable = false)
    private String email;

    /** 회원 닉네임 */
    @Column(name = "member_name", nullable = false)
    private String memberName;

    /** 소프트 삭제 */
    @Column(name = "is_delete", nullable = false, length = 1)
    private String isDelete;

    /** 가입한 SNS */
    @Enumerated(EnumType.STRING)
    @Column(name = "sns_type", nullable = false)
    private SnsType snsType;

    @Builder
    public Member(String email, String memberName, SnsType snsType) {
        Assert.notNull(email, "email은 필수입니다.");
        Assert.notNull(memberName, "닉네임은 필수입니다.");
        Assert.notNull(snsType, "SNS타입은 필수입니다.");
        //todo assertion
        this.email = email;
        this.memberName = memberName;
        this.isDelete = "N";
        this.snsType = snsType;
    }

    /**
     * 회원 정보 수정
     * @param member Member 해당 멤버
     */
    public void updateMember(Member member) {
        //todo email 형식 검사
        this.email = email;
    }

    /**
     * 회원 삭제
     * @param member Member 해당 맴버
     */
    public void deleteMember(Member member) {
        if (StringUtils.equals(member.getId(), this.getId())) {
            this.isDelete = "Y";
        }
    }
}