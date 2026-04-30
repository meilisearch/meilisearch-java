package com.meilisearch.sdk.enums;

import com.google.gson.annotations.SerializedName;

public enum PrefixSearchSetting {
    @SerializedName("indexingTime")
    INDEXING_TIME,
    @SerializedName("disabled")
    DISABLED
}
