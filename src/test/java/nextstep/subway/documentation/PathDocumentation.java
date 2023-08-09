package nextstep.subway.documentation;

import static nextstep.subway.acceptance.step.PathStep.경로_조회_요청;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import java.util.List;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import nextstep.utils.RestAssuredUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

@DisplayName("경로 탐색 문서화")
public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    PathResponse pathResponse;

    @BeforeEach
    void setUpData() {
        pathResponse = new PathResponse(
                List.of(new StationResponse(1L, "교대역"),
                        new StationResponse(4L, "남부터미널역"),
                        new StationResponse(3L, "양재역")),
                10,
                10
        );
    }

    @Test
    void path() {
        when(pathService.searchPath(anyLong(), anyLong(), anyString())).thenReturn(pathResponse);

        RestDocumentationFilter document = document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
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
                        fieldWithPath("duration").type(JsonFieldType.NUMBER).description("경로 소요 시간")
                )
        );

        경로_조회_요청(1, 2, "DISTANCE", RestAssuredUtils.given_절_생성(spec, document));
    }
}
