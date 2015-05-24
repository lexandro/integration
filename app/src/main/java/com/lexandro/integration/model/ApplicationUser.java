package com.lexandro.integration.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document
// index to find user by app/openid
@CompoundIndexes({
        @CompoundIndex(name = "app_user_idx", def = "{'accountId': 1, 'openId': 1}")
})
public class ApplicationUser {

    @Id
    private String id;
    private String accountId;
    private String openId;

    // FIXME ehh duplication
    private String firstName;
    private String lastName;
    private String userEmail;
    private String language;

    private String uuid;

    private List<AttributeEntry> attributes;
}
