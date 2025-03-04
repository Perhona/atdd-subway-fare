package nextstep.subway.line.application;


import nextstep.subway.Exception.ErrorCode;
import nextstep.subway.Exception.SubwayException;
import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.application.dto.LineSectionResponse;
import nextstep.subway.line.application.dto.UpdateLineRequest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.section.dto.SectionResponse;
import nextstep.subway.station.application.dto.StationResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getSections().allStations());
    }

    public LineResponse createLine(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(() -> new SubwayException(ErrorCode.STATION_NOT_FOUND, ""));
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(() -> new SubwayException(ErrorCode.STATION_NOT_FOUND, ""));

        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance(), lineRequest.getDuration(), lineRequest.getAdditionalFare());
        lineRepository.save(line);
        return createLineResponse(line);
    }

    public List<LineResponse> showLines() {
        return lineRepository.findAll()
                .stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse showLine(Long id) {
        return createLineResponse(lineRepository.findById(id).orElseThrow(() -> new SubwayException(ErrorCode.LINE_NOT_FOUND, "")));
    }

    public void updateLine(Long id, UpdateLineRequest updateLineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new SubwayException(ErrorCode.LINE_NOT_FOUND, ""));
        line.setName(updateLineRequest.getName());
        line.setColor(updateLineRequest.getColor());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private LineSectionResponse createLineSectionResponse(Line line) {
        List<SectionResponse> sectionResponses = line.getSections().get().stream()
                .map(this::createSectionResponse)
                .collect(Collectors.toList());
        return new LineSectionResponse(line.getId(), line.getName(), sectionResponses);
    }

    private SectionResponse createSectionResponse(Section section) {
        return new SectionResponse(section.getId(), createStationResponse(section.getUpStation()), createStationResponse(section.getDownStation()), section.getDistance(), section.getDuration());
    }

    public LineSectionResponse showLineSections(Long id) {
        return createLineSectionResponse(lineRepository.findById(id).orElseThrow(() -> new SubwayException(ErrorCode.LINE_NOT_FOUND, "")));
    }

    public SectionResponse addSection(Long id, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(id).get();

        Station upStation = stationRepository.findById(sectionRequest.getUpStationId()).get();
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId()).get();

        Section section = new Section(line, upStation, downStation, sectionRequest.getDistance(), sectionRequest.getDuration());
        line.addSection(section);
        return createSectionResponse(section);
    }

    public void deleteSection(Long id, Long stationId) {
        Line line = lineRepository.findById(id).get();
        line.deleteSection(stationId);
    }

    public List<Section> getSectionList() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .flatMap(line -> line.getSections().get().stream())
                .collect(Collectors.toList());
    }
}
