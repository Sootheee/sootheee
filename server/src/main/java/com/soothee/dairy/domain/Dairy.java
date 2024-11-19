package com.soothee.dairy.domain;

import com.soothee.common.domain.TimeEntity;
import com.soothee.member.domain.Member;
import com.soothee.reference.domain.Condition;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.thymeleaf.util.StringUtils;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "dairy")
public class Dairy extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "score", nullable = false)
    private Float score;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cond_id", nullable = false)
    private Condition cond;

    @Column(name = "content", length = 600)
    private String content;

    @Column(name = "hope")
    private String hope;

    @Column(name = "thank")
    private String thank;

    @Column(name = "learn")
    private String learn;

    @Column(name = "is_delete")
    private String isDelete;

    @Builder
    public Dairy(Member member, Float score, Condition cond, String content, String hope, String thank, String learn) {
        this.member = member;
        this.score = score;
        this.cond = cond;
        this.content = content;
        this.hope = hope;
        this.thank = thank;
        this.learn = learn;
        this.isDelete = "N";
    }

    public void updateDairy(Dairy dairy) {

    }

    public void deleteDairy(Dairy dairy) {
        if (StringUtils.equals(dairy.getId(), this.getId())) {
            this.isDelete = "Y";
        }
    }
}