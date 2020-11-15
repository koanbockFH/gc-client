package com.example.attendencemonitor.service;

import com.example.attendencemonitor.service.api.resolver.ActionResolver;
import com.example.attendencemonitor.service.api.resolver.ResultResolver;
import com.example.attendencemonitor.service.api.ApiAccess;
import com.example.attendencemonitor.service.api.IModuleApi;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.IModuleService;
import com.example.attendencemonitor.service.model.ModuleModel;

public class ModuleService extends BaseService<IModuleApi> implements IModuleService
{
    public ModuleService()
    {
        super(ApiAccess.getInstance().getRetrofit().create(IModuleApi.class));
    }

    @Override
    public void saveOrUpdate(ModuleModel model, ICallback<ModuleModel> callback)
    {
        if(model.getId() == 0)
        {
            api.create(model).enqueue(new ResultResolver<>(callback));
        }
        else{
            api.update(model).enqueue(new ResultResolver<>(callback));
        }
    }

    @Override
    public void delete(ModuleModel model, IActionCallback callback)
    {
        api.delete(model.getId()).enqueue(new ActionResolver(callback));
    }

    @Override
    public void getById(int id, ICallback<ModuleModel> callback)
    {
        api.getById(id).enqueue(new ResultResolver<>(callback));
    }

    @Override
    public void getAll(ICallback<ModuleModel[]> callback)
    {
        api.getAll().enqueue(new ResultResolver<>(callback));
    }
}
