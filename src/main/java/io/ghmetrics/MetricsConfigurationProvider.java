package io.ghmetrics;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@Singleton
public class MetricsConfigurationProvider {

    @Singleton
    @Produces
    public MeterFilter enableHistogram() {
        return new MeterFilter() {
            @Override
            public DistributionStatisticConfig configure(Meter.Id id, DistributionStatisticConfig config) {
                if (id.getType().equals(Meter.Type.TIMER)) {
                    return DistributionStatisticConfig.builder()
                            .percentiles(0.5, 0.95, 0.99, 1) // median, 95th percentile, 99th not aggregable
                            .build()
                            .merge(config);
                }
                return config;
            }
        };
    }
}
