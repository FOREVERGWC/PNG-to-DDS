package com.pngtodds.domain;

import cn.hutool.core.util.StrUtil;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 本地化词条
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Localisation implements Serializable {
    @Serial
    private static final long serialVersionUID = -6411458079852711096L;
    /**
     * 键名
     */
    private String key;
    /**
     * 键值
     */
    private String value;

    @Override
    public String toString() {
        return StrUtil.format(" {}:0 \"{}\"", key, value);
    }
}
