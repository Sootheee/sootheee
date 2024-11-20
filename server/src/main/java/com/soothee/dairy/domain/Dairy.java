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
    /** 다이어리 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** 등록 회원 */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /** 오늘의 점수 */
    @Column(name = "score", nullable = false)
    private Float score;

    /** 컨디션 */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cond_id", nullable = false)
    private Condition cond;

    /** 하루 요약 */
    @Column(name = "content", length = 600)
    private String content;

    /** 바랐던 방향성 */
    @Column(name = "hope")
    private String hope;

    /** 감사한 일 */
    @Column(name = "thank")
    private String thank;

    /** 배울 점 */
    @Column(name = "learn")
    private String learn;

    /** 소프트 삭제 */
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

    /**
     * 다이어리 수정</hr>
     *
     * @param dairy Dairy : 해당 다이어리
     */
    public void updateDairy(Dairy dairy) {

    }

    /**
     * 다이어리 삭제</hr>
     *
     * @param dairy Dairy : 해당 다이어리
     */
    public void deleteDairy(Dairy dairy) {
        if (StringUtils.equals(dairy.getId(), this.getId())) {
            this.isDelete = "Y";
        }
    }
}