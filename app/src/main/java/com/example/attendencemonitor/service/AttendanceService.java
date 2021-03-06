package com.example.attendencemonitor.service;

import com.example.attendencemonitor.service.api.ApiAccess;
import com.example.attendencemonitor.service.api.IAttendanceApi;
import com.example.attendencemonitor.service.api.resolver.ActionResolver;
import com.example.attendencemonitor.service.api.resolver.ResultResolver;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.IAttendanceService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.dto.AttendDto;
import com.example.attendencemonitor.service.model.ModuleStatisticModel;
import com.example.attendencemonitor.service.model.ModuleStatisticModelBase;
import com.example.attendencemonitor.service.model.StudentTimeslotStatisticModel;
import com.example.attendencemonitor.service.model.TimeslotStatisticDetailModel;
import com.example.attendencemonitor.service.model.TimeslotStatisticModel;

import java.util.List;

public class AttendanceService extends BaseService<IAttendanceApi> implements IAttendanceService
{
    public AttendanceService()
    {
        super(ApiAccess.getInstance().getRetrofit().create(IAttendanceApi.class));
    }

    @Override
    public void attend(AttendDto dto, IActionCallback callback)
    {
        api.attend(dto).enqueue(new ActionResolver(callback));
    }

    @Override
    public void getModuleStats(int moduleId, ICallback<ModuleStatisticModel> callback)
    {
        api.getModuleStats(moduleId).enqueue(new ResultResolver<>(callback));
    }

    @Override
    public void getAllTimeslotStats(int moduleId, ICallback<List<TimeslotStatisticModel>> callback)
    {
        api.getAllTimeslotStats(moduleId).enqueue(new ResultResolver<>(callback));
    }

    @Override
    public void getTimeslotStats(int timeslotId, ICallback<TimeslotStatisticDetailModel> callback)
    {
        api.getTimeslotStats(timeslotId).enqueue(new ResultResolver<>(callback));
    }

    @Override
    public void getStudentTimeslotStats(int moduleId, int studentId, ICallback<StudentTimeslotStatisticModel> callback)
    {
        api.getStudentTimeslotStats(moduleId, studentId).enqueue(new ResultResolver<>(callback));
    }

    @Override
    public void getStudentModuleStats(int studentId, ICallback<List<ModuleStatisticModelBase>> callback)
    {
        api.getStudentModuleStats(studentId).enqueue(new ResultResolver<>(callback));
    }
}
