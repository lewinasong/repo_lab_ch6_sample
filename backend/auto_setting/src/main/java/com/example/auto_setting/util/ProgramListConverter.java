package com.example.auto_setting.util;

import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProgramListConverter {

    public static final String PROGRAM_LIST_DELIMITER = ",";

    // EXEC_LIST를 ,로 split. (예시. "1,3,4,7,2" => [1,3,4,7,2])
    public static List<Integer> convertStringToList(@NonNull final String targetString) {
        // delimiter로 String을 split하여 String 배열로 변환
        final String[] splitProgramIdList = targetString.split(PROGRAM_LIST_DELIMITER);

        // String 배열을 List<Integer>로 형변환
        final List<Integer> programIdlist = Arrays.stream(splitProgramIdList)
                .map(Integer::parseInt)
                .toList();
        return programIdlist;
    }

    // Program Id List를 EXEC_LIST 형태로 변환 (예시. [1,3,4,7,2] => "1,3,4,7,2")
    public static String convertListToString(@NonNull final List<Integer> programList) {
        final String convertedProgramIdList = programList.stream()
                .map(String::valueOf) // Integer를 String으로 변환
                .collect(Collectors.joining(PROGRAM_LIST_DELIMITER));
        return convertedProgramIdList;
    }
}
