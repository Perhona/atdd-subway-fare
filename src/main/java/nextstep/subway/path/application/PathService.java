package nextstep.subway.path.application;

import nextstep.subway.Exception.ErrorCode;
import nextstep.subway.Exception.SubwayException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.path.application.dto.PathResponse;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final StationRepository stationRepository;
    private final LineService lineService;

    public PathService(StationRepository stationRepository, LineService lineService) {
        this.stationRepository = stationRepository;
        this.lineService = lineService;
    }

    public PathResponse getShortestPath(Long source, Long target, PathType type) {
        Station sourceStation = stationRepository.findById(source).orElseThrow(() -> new SubwayException(ErrorCode.STATION_NOT_FOUND, ""));
        Station targetStation = stationRepository.findById(target).orElseThrow(() -> new SubwayException(ErrorCode.STATION_NOT_FOUND, ""));
        List<Section> sectionList = lineService.getSectionList();
        return new Path(sectionList).shortestPath(sourceStation, targetStation, type);
    }
}
