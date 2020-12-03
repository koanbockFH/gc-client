package com.example.attendencemonitor.service.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ModuleStatisticModel extends ModuleStatisticModelBase
{
    @Expose
    private List<StudentModuleStatisticModel> students;


    public List<StudentModuleStatisticModel> getStudents()
    {
        return students;
    }
}
