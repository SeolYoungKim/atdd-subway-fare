package nextstep.subway.line.domain;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Lines {
    private final Set<Line> lines;

    public Lines(Set<Line> lines) {
        this.lines = lines;
    }

    public Set<Line> getLines() {
        return Collections.unmodifiableSet(lines);
    }

    public List<Integer> getAdditionalFares() {
        return lines.stream()
                .map(Line::getAdditionalFee)
                .collect(Collectors.toList());
    }
}
