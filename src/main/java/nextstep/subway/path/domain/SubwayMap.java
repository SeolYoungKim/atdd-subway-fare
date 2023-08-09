package nextstep.subway.path.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.exception.PathNotFoundException;
import nextstep.subway.path.exception.StationNotInGivenLinesException;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class SubwayMap {
    private final List<Line> lines;
    private final PathType pathType;

    public SubwayMap(List<Line> lines, String type) {
        this.lines = lines;
        this.pathType = PathType.of(type);
    }

    public Path findPath(Station source, Station target) {
        validateStationsInLines(source, target);

        // 그래프 만들기
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = createGraph();

        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);
        validateGraphPath(result);

        List<Section> sections = result.getEdgeList().stream()
                .map(it -> it.getSection())
                .collect(Collectors.toList());

        return new Path(new Sections(sections));
    }

    private void validateStationsInLines(Station source, Station target) {
        Set<Station> stationsInLines = lines.stream()
                .flatMap(line -> line.getSections().stream())
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .collect(Collectors.toSet());

        if (!stationsInLines.contains(source) || !stationsInLines.contains(target)) {
            throw new StationNotInGivenLinesException();
        }
    }

    private SimpleDirectedWeightedGraph<Station, SectionEdge> createGraph() {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        addVertex(graph);
        addEdge(graph);
        addOppositeEdge(graph);

        return graph;
    }

    private void addVertex(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역(정점)을 등록
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(it -> graph.addVertex(it));
    }

    private void addEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> setUpGraph(graph, it));
    }

    private void addOppositeEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
                .forEach(it -> setUpGraph(graph, it));
    }

    private void setUpGraph(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, Section section) {
        SectionEdge sectionEdge = SectionEdge.of(section);
        graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        graph.setEdgeWeight(
                sectionEdge, pathType.getEdgeValue(section));
    }

    private void validateGraphPath(GraphPath<Station, SectionEdge> graphPath) {
        if (graphPath == null) {
            throw new PathNotFoundException();
        }
    }
}
