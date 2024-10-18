/*
package com.example.auto_setting.service;

import com.example.auto_setting.persistence.ProgramListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProgramListService {

    private final ProgramListRepository programListRepository;

    public List<Integer> getProgramList() {
        String programList = programListRepository.getProgramList();

        String[] splitedProgramList = programList.split(",");

        List<Integer> convertedProgramList = new ArrayList<>();

        Integer targetProgramId = 0;

        // Approach 1: remove
        int index = convertedProgramList.indexOf(targetProgramId);
        convertedProgramList.remove(index);

        // or Approach 2: remove
        convertedProgramList.remove(targetProgramId);

        // Convert from List to String
        StringJoiner stringJoiner = new StringJoiner(",");
        for (Integer pid : convertedProgramList) {
            stringJoiner.add(String.valueOf(pid));
        }
        String convertedString = stringJoiner.toString();
        return null;
    }
}
*/
