package com.example.attendencemonitor.service.model;

import com.google.gson.annotations.Expose;

import java.util.LinkedList;
import java.util.List;

public class ModuleModel extends BaseModel
{
    @Expose
    private String name;
    @Expose
    private String code;
    @Expose
    private String description;
    @Expose
    private UserModel teacher;
    @Expose
    private List<UserModel> students = new LinkedList<>();

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public UserModel getTeacher()
    {
        return teacher;
    }

    public void setTeacher(UserModel teacher)
    {
        this.teacher = teacher;
    }

    public List<UserModel> getStudents()
    {
        return students;
    }

    public void setStudents(List<UserModel> students)
    {
        this.students = students;
    }

    public void addStudent(UserModel student)
    {
        if(this.students.stream().anyMatch(o -> o.getId() == student.getId()))
        {
            return;
        }

        this.students.add(student);
    }

    public void removeStudent(UserModel student)
    {
        this.students.removeIf(u -> u.getId() == student.getId());
    }
}
