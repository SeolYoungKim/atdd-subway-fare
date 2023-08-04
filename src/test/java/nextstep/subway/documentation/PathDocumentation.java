package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.acceptance.step.PathStep;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.util.List;

import static io.restassured.RestAssured.when;
import static nextstep.subway.acceptance.step.PathStep.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@DisplayName("경로 탐색 문서화")
public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                List.of(new StationResponse(1L, "교대역"),
                        new StationResponse(4L, "남부터미널역"),
                        new StationResponse(3L, "양재역")),
                10
        );
        
        when(pathService.searchPath(anyLong(), anyLong())).thenReturn(pathResponse);

        RestDocumentationFilter document = document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                        parameterWithName("source").description("출발역 ID"),
                        parameterWithName("target").description("도착역 ID")
                ),
                responseFields(
                        fieldWithPath("stations[]").type(JsonFieldType.ARRAY).description("최단경로 상에 존재하는 역"),
                        fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("지하철역 ID"),
                        fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("지하철역 이름"),
                        fieldWithPath("distance").type(JsonFieldType.NUMBER).description("최단경로의 총 길이")
                )
        );

        출발_역에서_도착_역까지의_최단거리_조회_문서화(spec, document);
    }
}
