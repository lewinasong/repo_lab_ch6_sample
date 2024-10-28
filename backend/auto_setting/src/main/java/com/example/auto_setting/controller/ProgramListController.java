package com.example.auto_setting.controller;

import com.example.auto_setting.service.ProgramDto;
import com.example.auto_setting.service.ProgramListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/program")
@RequiredArgsConstructor
public class ProgramListController {

    private final ProgramListService programListService;

   // @CrossOrigin(origins = "http://localhost:8089") // 프론트엔드 주소 허용
   @CrossOrigin(origins = "http://3.209.94.202:8089") // 프론트엔드 주소
    // 직원 번호로 프로그램 ID 리스트 조회 (GET)
    @GetMapping("PagePgmDtlEmpno/{empNo}")
    public ResponseEntity<List<Long>> getProgramIdListByEmpNo(@PathVariable String empNo) {
        try {
            List<Long> programList = programListService.getProgramIdListByEmpNo(empNo);
            if (programList == null || programList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 데이터가 없을 경우 404 반환
            }
            return new ResponseEntity<>(programList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // 상세한 예외를 로그에 기록
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "http://3.209.94.202:8089") // 프론트엔드 주소
    // 프로그램 리스트 생성 (POST)
    @PostMapping("/create")
    public ResponseEntity<String> createProgramList(@RequestParam String empNo, @RequestBody List<Long> programList) {
        try {
            programListService.createProgramList(empNo, programList);
            return new ResponseEntity<>("Program list created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // 상세한 예외를 로그에 기록
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "http://3.209.94.202:8089") // 프론트엔드 주소
    // 프로그램 실행 순서 업데이트 (PUT) - JSON 형태로 empNo와 programOrder 리스트 수신
    @PutMapping("/updateOrder")
    public ResponseEntity<String> updateProgramExecutionOrder(@RequestBody Map<String, Object> request) {
        try {
            // JSON으로 전달된 empNo와 programOrder 추출
            String empNo = (String) request.get("empNo");
            List<Long> programOrder = ((List<?>) request.get("programOrder")).stream()
                    .map(obj -> obj != null ? Long.parseLong(obj.toString()) : null)
                    .collect(Collectors.toList());

            // 유효성 검사 및 로깅 추가
            if (empNo == null || empNo.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Employee number cannot be null or empty");
            }
            if (programOrder == null || programOrder.isEmpty()) {
                return ResponseEntity.badRequest().body("Program order cannot be null or empty");
            }

            // 서비스 메서드 호출
            programListService.updateProgramExecutionOrder(empNo, programOrder);
            return ResponseEntity.ok("Program execution order updated successfully");
        } catch (Exception e) {
            e.printStackTrace(); // 상세한 예외를 로그에 기록
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update execution order: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://3.209.94.202:8089") // 프론트엔드 주소
    // 프로그램 리스트 삭제 (DELETE)
    @DeleteMapping("/delete/{empNo}")
    public ResponseEntity<String> deleteProgramList(@PathVariable String empNo) {
        try {
            programListService.deleteProgramList(empNo);
            return new ResponseEntity<>("Program list deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // 상세한 예외를 로그에 기록
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "http://3.209.94.202:8089") // 프론트엔드 주소
    // 직원 번호로 Program List + Program Name 조회 (GET)
    @GetMapping("/PagePgmDtl/{empNo}")
    public ResponseEntity<?> getProgramListWithNameByEmpNo(@PathVariable String empNo) {
        try {
            List<ProgramDto> programListWithNames = programListService.getProgramListWithNameByEmpNo(empNo);
            if (programListWithNames.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(programListWithNames, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // 상세한 예외를 로그에 기록
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
