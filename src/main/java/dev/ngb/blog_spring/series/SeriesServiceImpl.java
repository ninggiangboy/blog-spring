package dev.ngb.blog_spring.series;

import dev.ngb.blog_spring.series.domain.SeriesInfo;
import dev.ngb.blog_spring.series.domain.SeriesProjection;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeriesServiceImpl implements SeriesService {

    private final SeriesRepository seriesRepository;
    private final ModelMapper mapper;

    @Override
    public List<SeriesInfo> getAvailableSeries() {
        return seriesRepository.findByIsActiveTrue(SeriesProjection.class)
                .stream().map((element) -> mapper.map(element, SeriesInfo.class)).toList();
    }
}
