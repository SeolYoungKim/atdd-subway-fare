package nextstep.subway.unit;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Distance;
import nextstep.subway.domain.Duration;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LineServiceTest {
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    @Test
    void addSection() {
        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 역삼역 = stationRepository.save(new Station("역삼역"));
        Station 삼성역 = stationRepository.save(new Station("삼성역"));
        Line 이호선 = lineRepository.save(createLine(강남역, 역삼역));

        lineService.addSection(이호선.getId(), SectionRequest.of(역삼역.getId(), 삼성역.getId(), 10, 20));

        Line line = lineService.findById(이호선.getId());

        assertThat(line.getSections().size()).isEqualTo(2);
    }

    private Line createLine(Station 강남역, Station 역삼역) {
        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, Distance.from(10), Duration.from(20));
        return line;
    }
}
