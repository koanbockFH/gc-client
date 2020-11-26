package com.example.attendencemonitor.service.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class UserModel extends BaseModel implements Parcelable
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

    public UserModel(){}

    protected UserModel(Parcel in)
    {
        id = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        code = in.readString();
        mail = in.readString();
        userType = UserType.fromKey(in.readInt());
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>()
    {
        @Override
        public UserModel createFromParcel(Parcel in)
        {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size)
        {
            return new UserModel[size];
        }
    };

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

    public String getFullName(){ return this.getFirstName() + " " + this.getLastName(); }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeInt(getId());
        parcel.writeString(getFirstName());
        parcel.writeString(getLastName());
        parcel.writeString(getCode());
        parcel.writeString(getMail());
        parcel.writeInt(getUserType().getKey());
    }
}
