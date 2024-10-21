package com.example.auto_setting.service;

import com.example.auto_setting.entity.ProgramList;
import com.example.auto_setting.persistence.ProgramListRepository;
import com.example.auto_setting.util.ProgramListConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgramListService {

    private final ProgramListRepository programListRepository;

    public List<Integer> getProgramIdListByEmpNo(@NonNull final String empNo) {

        // DB에서 EMP_NO를 통해 ProgramList를 조회
        final ProgramList programList = programListRepository.findByEmpNo(empNo);

        final List<Integer> programIdlist = ProgramListConverter.convertStringToList(programList.getProgramIdList());

        return programIdlist;
    }

    public void createProgramList(@NonNull final String empNo, @NonNull final List<Integer> programList) {
        // List<Integer>를 쉼표로 구분된 String으로 변환
        final String convertedProgramIdList = ProgramListConverter.convertListToString(programList);

        programListRepository.create(empNo, convertedProgramIdList);
    }

    public void updateProgramList(@NonNull final String empNo, @NonNull final List<Integer> programList) {
        // List<Integer>를 쉼표로 구분된 String으로 변환
        final String convertedProgramIdList = ProgramListConverter.convertListToString(programList);

        programListRepository.update(empNo, convertedProgramIdList);
    }

    public void deleteProgramList(@NonNull final String empNo) {
        programListRepository.deleteByEmpNo(empNo);
    }

}
