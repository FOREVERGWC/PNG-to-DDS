package com.pngtodds.domain;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 内阁肖像
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(callSuper = false)
public class Small implements Serializable {
    // TODO 合并Large和Small
    @Serial
    private static final long serialVersionUID = 1796336876757809822L;
    /**
     * 键名
     */
    private String key;
    /**
     * 键值
     */
    private String value;
    /**
     * 路径
     */
    private String path;
}
