package com.example.auto_setting.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramList {

    private Long id; // EXEC_LIST_ID와 동일
    private String programIdList; // EXEC_LIST와 동일
    private String empNo; // empNo는 unique함. EMP_NO와 동일
}
