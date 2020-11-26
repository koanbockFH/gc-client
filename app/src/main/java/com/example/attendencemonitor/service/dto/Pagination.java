package com.example.attendencemonitor.service.dto;

import com.google.gson.annotations.Expose;

public class Pagination<T>
{
    @Expose
    private T[] items;
    @Expose
    private int currentPage;
    @Expose
    private int totalPages;

    public T[] getItems()
    {
        return items;
    }

    public void setItems(T[] items)
    {
        this.items = items;
    }

    public int getCurrentPage()
    {
        return currentPage;
    }

    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }

    public int getTotalPages()
    {
        return totalPages;
    }

    public void setTotalPages(int totalPages)
    {
        this.totalPages = totalPages;
    }
}
