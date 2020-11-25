package com.example.attendencemonitor.service;

import com.example.attendencemonitor.service.api.ApiAccess;
import com.example.attendencemonitor.service.api.IModuleApi;
import com.example.attendencemonitor.service.api.ITimeslotApi;
import com.example.attendencemonitor.service.api.resolver.ActionResolver;
import com.example.attendencemonitor.service.api.resolver.ResultResolver;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.IModuleService;
import com.example.attendencemonitor.service.contract.ITimeslotService;
import com.example.attendencemonitor.service.model.ModuleModel;
import com.example.attendencemonitor.service.model.TimeslotModel;

public class TimeslotService extends BaseService<ITimeslotApi> implements ITimeslotService
{
    public TimeslotService()
    {
        super(ApiAccess.getInstance().getRetrofit().create(ITimeslotApi.class));
    }

    @Override
    public void saveOrUpdate(int moduleId, TimeslotModel model, ICallback<TimeslotModel> callback)
    {
        if(model.getId() == 0)
        {
            api.create(moduleId, model).enqueue(new ResultResolver<>(callback));
        }
        else{
            api.update(moduleId, model).enqueue(new ResultResolver<>(callback));
        }
    }

    @Override
    public void delete(TimeslotModel model, IActionCallback callback)
    {
        api.delete(model.getId()).enqueue(new ActionResolver(callback));
    }

    @Override
    public void getById(int id, ICallback<TimeslotModel> callback)
    {
        api.getById(id).enqueue(new ResultResolver<>(callback));
    }

    @Override
    public void getAll(int moduleId, ICallback<TimeslotModel[]> callback)
    {
        api.getAll(moduleId).enqueue(new ResultResolver<>(callback));
    }
}
