package com.example.auto_setting.service;

import com.example.auto_setting.entity.ProgramList;
import com.example.auto_setting.persistence.ProgramListJdbcRepository;
import com.example.auto_setting.util.ProgramListConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgramListService {

    private final ProgramListJdbcRepository programListRepository;

    // 직원 번호로 Program List를 조회하여 프로그램 ID 리스트 반환
    public List<Integer> getProgramIdListByEmpNo(@NonNull final String empNo) {
        // DB에서 EMP_NO를 통해 ProgramList를 조회
        System.out.println("getProgramIdListByEmpNo / empNo : " + empNo);

        final ProgramList programList = programListRepository.findByEmpNo(empNo);
        System.out.println("getProgramIdListByEmpNo / programList : " + programList);

        final List<Integer> programIdlist = ProgramListConverter.convertStringToList(programList.getProgramIdList());

        return programIdlist;
    }

    // 직원 번호로 Program List와 Program Name을 조합하여 반환하는 서비스 메서드
    public List<ProgramDto> getProgramListWithNameByEmpNo(@NonNull String empNo) {
        return programListRepository.findProgramListWithNameByEmpNo(empNo);
    }

    // 직원 번호로 Program List를 생성하는 메서드
    public void createProgramList(@NonNull final String empNo, @NonNull final List<Integer> programList) {
        // List<Integer>를 쉼표로 구분된 String으로 변환
        final String convertedProgramIdList = ProgramListConverter.convertListToString(programList);

        programListRepository.create(empNo, convertedProgramIdList);
    }

    // 직원 번호로 Program List를 업데이트하는 메서드
    public void updateProgramList(@NonNull final String empNo, @NonNull final List<Integer> programList) {
        // List<Integer>를 쉼표로 구분된 String으로 변환
        final String convertedProgramIdList = ProgramListConverter.convertListToString(programList);

        programListRepository.update(empNo, convertedProgramIdList);
    }

    // 직원 번호로 Program List를 삭제하는 메서드
    public void deleteProgramList(@NonNull final String empNo) {
        programListRepository.deleteByEmpNo(empNo);
    }

}
