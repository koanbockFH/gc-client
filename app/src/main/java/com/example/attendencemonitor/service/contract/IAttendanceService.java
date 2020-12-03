package com.example.attendencemonitor.service.contract;

import com.example.attendencemonitor.service.dto.AttendDto;
import com.example.attendencemonitor.service.model.ModuleStatisticModel;
import com.example.attendencemonitor.service.model.ModuleStatisticModelBase;
import com.example.attendencemonitor.service.model.StudentTimeslotStatisticModel;
import com.example.attendencemonitor.service.model.TimeslotStatisticModel;

import java.util.List;

public interface IAttendanceService
{
    void attend(AttendDto dto, IActionCallback callback);

    void getModuleStats(int moduleId, ICallback<ModuleStatisticModel> callback);

    void getAllTimeslotStats(int moduleId, ICallback<List<TimeslotStatisticModel>> callback);

    void getStudentTimeslotStats(int moduleId, int studentId, ICallback<StudentTimeslotStatisticModel> callback);

    void getStudentModuleStats(int studentId, ICallback<List<ModuleStatisticModelBase>> callback);
}
