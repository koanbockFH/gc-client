package com.example.attendencemonitor.service.contract;

import com.example.attendencemonitor.service.dto.AttendDto;

public interface IAttendanceService
{
    void attend(AttendDto dto, IActionCallback callback);
}
