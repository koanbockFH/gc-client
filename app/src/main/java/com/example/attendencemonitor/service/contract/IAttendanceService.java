package com.example.attendencemonitor.service.contract;

import com.example.attendencemonitor.service.dto.AttendDto;
import com.example.attendencemonitor.service.model.ModuleStatisticModel;
import com.example.attendencemonitor.service.model.TimeslotStatisticModel;

import java.util.List;

public interface IAttendanceService
{
    void attend(AttendDto dto, IActionCallback callback);

    void getModuleStats(int moduleId, ICallback<ModuleStatisticModel> callback);

    void getAllTimeslotStats(int moduleId, ICallback<List<TimeslotStatisticModel>> callback);
}
