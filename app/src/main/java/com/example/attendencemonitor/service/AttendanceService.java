package com.example.attendencemonitor.service;

import com.example.attendencemonitor.service.api.ApiAccess;
import com.example.attendencemonitor.service.api.IAttendanceApi;
import com.example.attendencemonitor.service.api.resolver.ActionResolver;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.IAttendanceService;
import com.example.attendencemonitor.service.dto.AttendDto;

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
}
