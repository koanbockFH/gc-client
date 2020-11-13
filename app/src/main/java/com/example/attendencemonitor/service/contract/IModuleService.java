package com.example.attendencemonitor.service.contract;

import com.example.attendencemonitor.service.model.ModuleModel;

public interface IModuleService
{
    void saveOrUpdate(ModuleModel model, ICallback<ModuleModel> callback);
    void delete(ModuleModel model, IActionCallback callback);
    void getById(int id, ICallback<ModuleModel> callback);
    void getAll(ICallback<ModuleModel[]> callback);
}
