package com.example.attendencemonitor.service.model;

import com.google.gson.annotations.Expose;

public class UserModel extends BaseModel
{
    @Expose
    private String firstName;

    @Expose
    private String lastName;

    @Expose
    private String code;

    @Expose
    private String mail;

    @Expose
    private UserType userType;

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getMail()
    {
        return mail;
    }

    public void setMail(String mail)
    {
        this.mail = mail;
    }

    public UserType getUserType()
    {
        return userType;
    }

    public void setUserType(UserType userType)
    {
        this.userType = userType;
    }
}
