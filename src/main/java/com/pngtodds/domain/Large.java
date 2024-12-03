package com.pngtodds.domain;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 人物肖像
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(callSuper = false)
public class Large implements Serializable {
    @Serial
    private static final long serialVersionUID = -3121951304403290863L;
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
