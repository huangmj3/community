package com.huangmj.community.model;

import lombok.Data;

/**
 *  A shortcut for @ToString,
 *  @EqualsAndHashCode,
 *  @Getter on all fields,
 *  @Setter on all non-final fields,
 *  and @RequiredArgsConstructor!
 */
@Data
public class User {
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
}
