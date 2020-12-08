package com.example.attendencemonitor.service.api.resolver;

import com.example.attendencemonitor.service.api.ApiAccess;
import com.example.attendencemonitor.service.dto.StandardExceptionDto;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Base resolver defining exception handling - again to provide an abstraction layer for retrofit and presentation layer
 * @param <T> type of response - can be void
 */
public abstract class BaseResolver<T>
{
    protected Exception handleHttpException(Response<T> response)
    {
        Converter<ResponseBody, StandardExceptionDto> converter = ApiAccess.getInstance().getRetrofit().responseBodyConverter(StandardExceptionDto.class, new Annotation[0]);
        StandardExceptionDto errorResponse = null;
        try
        {
            if (response.errorBody() != null)
            {
                errorResponse = converter.convert(response.errorBody());
            }
        }
        catch (IOException e)
        {
            return e;
        }
        return errorResponse == null ? new RuntimeException("Something went wrong") : new RuntimeException(errorResponse.getMessage());
    }
}
