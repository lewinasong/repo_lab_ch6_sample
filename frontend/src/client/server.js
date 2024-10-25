const express = require('express');
const fs = require('fs');
const path = require('path');
const app = express();
const port = 3000;

// API 엔드포인트 설정
app.get('/api/status', (req, res) => {
    // status.log 파일 경로 설정 (Windows 절대 경로 사용)
    const logFilePath = path.join('C:', 'Users', 'user', 'Downloads', 'status.log');
    
    // 파일을 읽어 응답
    fs.readFile(logFilePath, 'utf8', (err, data) => {
        if (err) {
            return res.status(500).json({ error: '로그 파일을 읽을 수 없습니다.' });
        }
        const logs = data.split('\n').filter(line => line); // 로그 데이터를 배열로 변환
        res.json({ logs });
    });
});

// 서버 시작
app.listen(port, () => {
    console.log(`서버가 http://localhost:${port} 에서 실행 중입니다.`);
});