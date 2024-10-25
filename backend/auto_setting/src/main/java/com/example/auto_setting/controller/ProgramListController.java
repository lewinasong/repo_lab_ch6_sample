package com.example.auto_setting.controller;

import com.example.auto_setting.service.ProgramDto;
import com.example.auto_setting.service.ProgramListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/program")
@RequiredArgsConstructor
public class ProgramListController {

    private final ProgramListService programListService;
    @CrossOrigin(origins = "http://10.233.4.217:8089") // 프론트엔드 주소
    // 직원 번호로 프로그램 리스트 조회 (GET)
    @GetMapping("PagePgmDtlEmpno/{empNo}")
    public ResponseEntity<List<Integer>> getProgramListByEmpNo(@PathVariable String empNo) {
        try {
            System.out.println("PagePgmDtlEmpno ");

            List<Integer> programList = programListService.getProgramIdListByEmpNo(empNo);
            return new ResponseEntity<>(programList, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching program list: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 프로그램 리스트 생성 (POST)
    @PostMapping("/create")
    public ResponseEntity<String> createProgramList(@RequestParam String empNo, @RequestBody List<Integer> programList) {
        try {
            programListService.createProgramList(empNo, programList);
            return new ResponseEntity<>("Program list created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 프로그램 리스트 수정 (PUT)
    @PutMapping("/update")
    public ResponseEntity<String> updateProgramList(@RequestParam String empNo, @RequestBody List<Integer> programList) {
        try {
            programListService.updateProgramList(empNo, programList);
            return new ResponseEntity<>("Program list updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 프로그램 리스트 삭제 (DELETE)
    @DeleteMapping("/delete/{empNo}")
    public ResponseEntity<String> deleteProgramList(@PathVariable String empNo) {
        try {
            programListService.deleteProgramList(empNo);
            return new ResponseEntity<>("Program list deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 직원 번호로 Program List + Program Name 조회 (GET)
    @GetMapping("/PagePgmDtl/{empNo}")
    public ResponseEntity<?> getProgramListWithNameByEmpNo(@PathVariable String empNo) {
        try {
            System.out.println("PagePgmDtl ");
            List<ProgramDto> programListWithNames = programListService.getProgramListWithNameByEmpNo(empNo);
            System.out.println("programListWithNames : "+ programListWithNames);

            if (programListWithNames.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(programListWithNames, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
