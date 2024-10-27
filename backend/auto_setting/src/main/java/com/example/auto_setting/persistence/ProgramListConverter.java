// ProgramListConverter.java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProgramListConverter {

    // 쉼표로 구분된 String을 List<Long>으로 변환
    public static List<Long> convertStringToList(String programIdList) {
        if (programIdList == null || programIdList.trim().isEmpty()) {
            return List.of(); // 빈 리스트 반환
        }

        return Arrays.stream(programIdList.split(","))
                .filter(str -> !str.trim().isEmpty()) // 빈 문자열 필터링
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    // List<Long>을 쉼표로 구분된 String으로 변환
    public static String convertListToString(List<Long> programIds) {
        if (programIds == null || programIds.isEmpty()) {
            return ""; // 빈 문자열 반환
        }

        return programIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
