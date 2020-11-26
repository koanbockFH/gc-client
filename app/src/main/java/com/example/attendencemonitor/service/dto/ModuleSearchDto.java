package com.example.attendencemonitor.service.dto;

import com.example.attendencemonitor.service.model.UserType;

public class ModuleSearchDto
{
    private Integer page;
    private Integer take;
    private String search;

    public Integer getPage()
    {
        return page;
    }

    public void setPage(Integer page)
    {
        this.page = page;
    }

    public Integer getTake()
    {
        return take;
    }

    public void setTake(Integer take)
    {
        this.take = take;
    }

    public String getSearch()
    {
        return search;
    }

    public void setSearch(String search)
    {
        this.search = search;
    }

}
