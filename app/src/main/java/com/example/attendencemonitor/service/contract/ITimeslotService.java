package com.example.attendencemonitor.service.contract;

import com.example.attendencemonitor.service.model.ModuleModel;
import com.example.attendencemonitor.service.model.TimeslotModel;

public interface ITimeslotService
{
    void saveOrUpdate(int moduleId, TimeslotModel model, ICallback<TimeslotModel> callback);
    void delete(TimeslotModel model, IActionCallback callback);
    void getById(int id, ICallback<TimeslotModel> callback);
    void getAll(int moduleId, ICallback<TimeslotModel[]> callback);
}
