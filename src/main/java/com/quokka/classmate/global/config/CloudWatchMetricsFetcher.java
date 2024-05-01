package com.quokka.classmate.global.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.cloudwatch.model.Statistic;

import java.util.Date;

public class CloudWatchMetricsFetcher {
    private final AmazonCloudWatch cloudWatch;


    public CloudWatchMetricsFetcher() {
        this.cloudWatch = AmazonCloudWatchClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_2)  // Specify your region, e.g., Seoul
                .build();
    }

    public Double getAverageCPUUtilization(String instanceIdentifier, int periodInSeconds) {
        GetMetricStatisticsRequest request = new GetMetricStatisticsRequest()
                .withStartTime(new Date(new Date().getTime() - 1000 * periodInSeconds * 2))
                .withEndTime(new Date())
                .withPeriod(periodInSeconds)
                .withNamespace("AWS/RDS")
                .withMetricName("CPUUtilization")
                .withStatistics(Statistic.Average)
                .withDimensions(new Dimension().withName("DBInstanceIdentifier").withValue(instanceIdentifier));

        GetMetricStatisticsResult result = cloudWatch.getMetricStatistics(request);
        return result.getDatapoints().stream()
                .mapToDouble(Datapoint::getAverage)
                .average()
                .orElse(Double.MAX_VALUE);
    }

}
