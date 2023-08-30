package nextstep.subway.path.domain.policy.discount;

import nextstep.subway.path.exception.NotSupportedAgeTypeException;

import java.util.Arrays;
import java.util.function.Predicate;

public enum AgeType {
    ADULT(age -> 19 <= age),
    TEENAGER(age -> 13 <= age && age < 19),
    CHILDREN(age -> 6 <= age && age < 13);

    private final Predicate<Integer> ageStandard;

    public static boolean isTeenager(Integer age) {
        return findAgeType(age) == TEENAGER;
    }

    public static boolean isChildren(Integer age) {
        return findAgeType(age) == CHILDREN;
    }

    private static AgeType findAgeType(Integer age) {
        return Arrays.stream(values())
                .filter(ageType -> ageType.ageStandard.test(age))
                .findAny()
                .orElseThrow(NotSupportedAgeTypeException::new);
    }

    AgeType(Predicate<Integer> ageStandard) {
        this.ageStandard = ageStandard;
    }
}
