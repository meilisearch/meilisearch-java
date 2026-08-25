package com.meilisearch.sdk;

import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@Accessors(chain = true)
public class RenderTemplateRequest {
    private Map<String, Object> template;
    private Map<String, Object> input;

    public RenderTemplateRequest() {}

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("template", this.template);
        jsonObject.putOpt("input", this.input);
        return jsonObject.toString();
    }
}
