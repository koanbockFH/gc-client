package com.example.attendencemonitor.service.dto;

import com.google.gson.annotations.Expose;

public class LoginFormDto
{
    @Expose
    private String codeOrMail;

    @Expose
    private String password;

    public String getCodeOrMail()
    {
        return codeOrMail;
    }

    public void setCodeOrMail(String codeOrMail)
    {
        this.codeOrMail = codeOrMail;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
