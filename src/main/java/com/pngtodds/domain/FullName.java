package com.pngtodds.domain;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 姓名
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(callSuper = false)
public class FullName implements Serializable {
    @Serial
    private static final long serialVersionUID = -69275363005349611L;
    /**
     * 姓氏
     */
    private String lastName;
    /**
     * 名字
     */
    private String firstName;
}
