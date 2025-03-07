package com.example.codegenerator.dto;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerationRequest {
    private String classDes;
    private String className;
    private List<String> Methods; // 确保使用的是 List 或 Set
    private List<String> Attrs;

    @Override
    public String toString() {
        return "CodeGenerationRequest{" +
                "classDes='" + classDes + '\'' +
                ", className='" + className + '\'' +
                ", Methods=" + Methods +
                ", Attrs=" + Attrs +
                '}';
    }

    public String getClassDes() {
        return classDes;
    }

    public void setClassDes(String classDes) {
        this.classDes = classDes;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getMethods() {
        return Methods;
    }

    public void setMethods(List<String> methods) {
        Methods = methods;
    }

    public List<String> getAttrs() {
        return Attrs;
    }

    public void setAttrs(List<String> attrs) {
        Attrs = attrs;
    }

    // 自动生成 Getter/Setter 方法：
    // 右键代码区 → Generate → Getter and Setter → 全选所有字段 → OK
}