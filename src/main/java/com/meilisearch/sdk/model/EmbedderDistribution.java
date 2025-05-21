package com.meilisearch.sdk.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Describes the natural distribution of search results for embedders. Contains mean and sigma
 * values, each between 0 and 1.
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmbedderDistribution {
    /** Mean value of the distribution, between 0 and 1 */
    private Double mean;

    /** Sigma (standard deviation) value of the distribution, between 0 and 1 */
    private Double sigma;

    /**
     * Creates a uniform distribution with default values
     *
     * @return An EmbedderDistribution instance with mean=0.5 and sigma=0.5
     */
    public static EmbedderDistribution uniform() {
        return new EmbedderDistribution().setMean(0.5).setSigma(0.5);
    }

    /**
     * Creates a custom distribution with specified mean and sigma values
     *
     * @param mean Mean value between 0 and 1
     * @param sigma Sigma value between 0 and 1
     * @return An EmbedderDistribution instance with the specified values
     * @throws IllegalArgumentException if mean or sigma are outside the valid range
     */
    public static EmbedderDistribution custom(double mean, double sigma) {
        if (mean < 0 || mean > 1) {
            throw new IllegalArgumentException("Mean must be between 0 and 1");
        }
        if (sigma < 0 || sigma > 1) {
            throw new IllegalArgumentException("Sigma must be between 0 and 1");
        }
        return new EmbedderDistribution().setMean(mean).setSigma(sigma);
    }
}
