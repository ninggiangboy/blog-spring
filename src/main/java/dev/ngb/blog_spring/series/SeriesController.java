package dev.ngb.blog_spring.series;

import dev.ngb.blog_spring.base.BaseController;
import dev.ngb.blog_spring.base.ResultResponse;
import dev.ngb.blog_spring.series.domain.SeriesInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${prefix.api}")
@RequiredArgsConstructor
public class SeriesController extends BaseController {
    private final SeriesService seriesService;

    @GetMapping("/series")
    public ResponseEntity<ResultResponse> getAllSeries() {
        List<SeriesInfo> seriesInfos = seriesService.getAvailableSeries();
        return buildResponse("Found all available series", seriesInfos);
    }
}
