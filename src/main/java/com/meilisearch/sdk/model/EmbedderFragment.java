package com.meilisearch.sdk.model;

import com.google.gson.JsonElement;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
public class EmbedderFragment {
    protected JsonElement value;
}
