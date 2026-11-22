package com.gautamsingh.portfolio.model;

import java.util.List;

/** Public profile payload consumed by the "Ping API" widget on the portfolio. */
public record EngineerProfile(
        String name,
        String college,
        List<String> coreStrengths,
        int hackathons
) {
}
