package dev.ngb.blog_spring.series;

import dev.ngb.blog_spring.series.domain.SeriesInfo;

import java.util.List;

public interface SeriesService {
    List<SeriesInfo> getAvailableSeries();
}
