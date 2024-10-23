package com.example.auto_setting.persistence;

import com.example.auto_setting.entity.ProgramList;

public interface ProgramListRepository {

    ProgramList findByEmpNo(final String empNo);

    Long create(final String empNo, final String programIdList);

    void update(final String empNo, final String programIdList);

    void deleteByEmpNo(final String empNo);

}
