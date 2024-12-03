package com.pngtodds.domain;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 技能
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(callSuper = false)
public class Skill implements Serializable {
    @Serial
    private static final long serialVersionUID = -1492826001219737459L;
    /**
     * 等级
     */
    private Integer skill;
    /**
     * 进攻
     */
    private Integer attack;
    /**
     * 防御
     */
    private Integer defense;
    /**
     * 计划
     */
    private Integer planning;
    /**
     * 后勤
     */
    private Integer logistics;
}
