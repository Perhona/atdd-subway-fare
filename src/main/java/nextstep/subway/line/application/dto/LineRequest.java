package nextstep.subway.line.application.dto;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;
    private Long duration;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this(name, color, upStationId, downStationId, distance, 0L);
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, Long distance, Long duration) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getDuration() {
        return duration;
    }
}
