package nextstep.subway.documentation;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.UserDto;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.utils.RestAssuredUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.util.List;
import java.util.Set;

import static nextstep.subway.acceptance.step.PathStep.경로_조회_요청;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@DisplayName("경로 탐색 문서화")
public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    PathResponse pathResponse;

    @BeforeEach
    void setUpData() {
        Station 교대역 = new Station(1L, "교대역");
        Station 양재역 = new Station(3L, "양재역");

        Section section = new Section(교대역, 양재역, 100, 10);
        Lines 이호선 = new Lines(
                Set.of(new Line("2호선", "green", 100, section))
        );

        Sections sections = new Sections(List.of(section));

        pathResponse = PathResponse.of(new Path(이호선, sections), 1250);
    }

    @Test
    void path() {
        when(pathService.searchPath(any(UserDto.class), anyLong(), anyLong(), anyString())).thenReturn(pathResponse);

        RestDocumentationFilter document = document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName("authorization").description("토큰").optional()
                ),
                requestParameters(
                        parameterWithName("source").description("출발역 ID"),
                        parameterWithName("target").description("도착역 ID"),
                        parameterWithName("type").description("경로 조회 타입(DISTANCE/DURATION)")
                ),
                responseFields(
                        fieldWithPath("stations[]").type(JsonFieldType.ARRAY).description("경로 상에 존재하는 역"),
                        fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("지하철역 ID"),
                        fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("지하철역 이름"),
                        fieldWithPath("distance").type(JsonFieldType.NUMBER).description("경로 총 길이"),
                        fieldWithPath("duration").type(JsonFieldType.NUMBER).description("경로 소요 시간"),
                        fieldWithPath("fare").type(JsonFieldType.NUMBER).description("지하철 이용 요금")
                )
        );

        경로_조회_요청(1, 2, "DISTANCE", RestAssuredUtils.given_절_생성(spec, document));
    }
}
