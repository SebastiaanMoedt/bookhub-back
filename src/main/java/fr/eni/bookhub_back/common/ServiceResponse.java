package fr.eni.bookhub_back.common;

public class ServiceResponse<T> {
    public String code;
    public String message;
    public T data;

    public ServiceResponse() {
    }

    public ServiceResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ServiceResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}