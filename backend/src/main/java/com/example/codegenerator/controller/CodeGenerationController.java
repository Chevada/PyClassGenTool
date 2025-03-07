package com.example.codegenerator.controller;

import com.example.codegenerator.dto.CodeGenerationRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.http.ResponseEntity;

@RestController
@CrossOrigin(origins = "*")
public class CodeGenerationController {

    // 获取 Python 脚本的绝对路径
    private static final String PY_SCRIPT_PATH = new File("E:/learing/论文复现/CodeT5/sh/codet5_generator.py")
            .getAbsolutePath();

    @PostMapping(value = "/generate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
        public ResponseEntity<String> generateCode(@RequestBody CodeGenerationRequest request) { //
            // 构建输入内容
            String input = buildModelInput(request);

            // 执行生成过程并获得结果
            String result = executeCodeT5(input);

            // 返回生成结果
            return ResponseEntity.ok(result);
        }

    private String buildModelInput(CodeGenerationRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getClassDes() != null ? request.getClassDes() : "")
                .append(" className ")
                .append(request.getClassName() != null ? request.getClassName() : "");

        // 安全处理集合
        if (request.getMethods() != null) {
            request.getMethods().forEach(method ->
                    sb.append(" Method ").append(method));
        }
        if (request.getAttrs() != null) {
            request.getAttrs().forEach(attr ->
                    sb.append(" Attribute ").append(attr));
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    // 执行代码生成逻辑并返回生成的结果
    private String executeCodeT5(String input) {
        StringBuilder result = new StringBuilder();
        Process process = null;
        System.out.println("进入执行函数...");
        try {
            // 转义特殊字符（Unix/Windows 兼容）
//            String sanitizedInput = input.replace("\"", "\\\"")
//                    .replace("'", "\\'");

            System.out.println("开始执行....");
            // 启动Python脚本
            process = new ProcessBuilder(
                    "python",
                    PY_SCRIPT_PATH,
                    input
            ).redirectErrorStream(true) // 合并错误流
                    .start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
            }

            process.waitFor(); // 等待脚本执行完毕
            System.out.println("脚本执行完毕...");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error during code generation: " + e.getMessage();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        System.out.println("生成结果：" + result.toString());
        return result.toString();
    }
}