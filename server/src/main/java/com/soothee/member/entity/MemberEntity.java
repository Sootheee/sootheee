package com.soothee.member.entity;

import com.soothee.common.constants.SnsType;
import com.soothee.common.entity.TimeEntity;
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
public class MemberEntity extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "isDelete", nullable = false, length = 1)
    private String isDelete;

    @Enumerated(EnumType.STRING)
    @Column(name = "snsType", nullable = false)
    private SnsType snsType;

    @Builder
    public MemberEntity(String email, String memberName, String isDelete, SnsType snsType) {
        Assert.notNull(email, "email은 필수입니다.");
        //todo assertion
        this.email = email;
        this.memberName = memberName;
        this.isDelete = isDelete;
        this.snsType = snsType;
    }

    public void updateEmail(String email){
        //todo email 형식 검사
        this.email = email;
    }
}