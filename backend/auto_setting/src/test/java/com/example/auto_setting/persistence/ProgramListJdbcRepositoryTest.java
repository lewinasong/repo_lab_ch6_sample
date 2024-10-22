package com.example.auto_setting.persistence;

import com.example.auto_setting.entity.ProgramList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

// 이 테스트에서는 application-dev.properties를 사용합니다
@SpringBootTest
@ActiveProfiles("dev")
class ProgramListJdbcRepositoryTest {

    @Autowired
    private ProgramListJdbcRepository programListJdbcRepository;

    @Test
    public void test() {
        String empNo = "1234567";
        String programIdList = "1,2,3";
        programListJdbcRepository.create(empNo, programIdList);

        ProgramList programList = programListJdbcRepository.findByEmpNo(empNo);
        assertThat(programList.getProgramIdList()).isEqualTo(programIdList);
        assertThat(programList.getEmpNo()).isEqualTo(empNo);

        Long id = programList.getId();
        String updatedProgramIdList = "2,1,3,4";

        programListJdbcRepository.update(empNo, updatedProgramIdList);
        assertThat(programList.getProgramIdList()).isEqualTo(programIdList);
        assertThat(programList.getEmpNo()).isEqualTo(empNo);
        assertThat(programList.getId()).isEqualTo(id);

        programListJdbcRepository.deleteByEmpNo(empNo);
        ProgramList afterDelete = programListJdbcRepository.findByEmpNo(empNo);
        assertThat(afterDelete).isNull();
    }
}