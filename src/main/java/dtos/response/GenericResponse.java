package dtos.response;

import java.util.Locale;
import java.util.ResourceBundle;

public class GenericResponse<T> {

    private boolean success;
    private String message;
    private T data;

    public GenericResponse() {
    }

    public GenericResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> GenericResponse<T> success(T data) {
        return success(data, Locale.getDefault());
    }

    public static <T> GenericResponse<T> success(T data, Locale locale) {
        return success("response.success", data, locale);
    }

    public static <T> GenericResponse<T> success(String messageKey, T data) {
        return success(messageKey, data, Locale.getDefault());
    }

    public static <T> GenericResponse<T> success(String messageKey, T data, Locale locale) {
        return new GenericResponse<>(true, resolveMessage(messageKey, locale), data);
    }

    public static <T> GenericResponse<T> error(String messageKey) {
        return error(messageKey, Locale.getDefault());
    }

    public static <T> GenericResponse<T> error(String messageKey, Locale locale) {
        return new GenericResponse<>(false, resolveMessage(messageKey, locale), null);
    }

    private static String resolveMessage(String messageKey, Locale locale) {
        if (messageKey == null || messageKey.isBlank()) {
            return "";
        }

        Locale effectiveLocale = locale != null ? locale : Locale.getDefault();

        try {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", effectiveLocale);
            return bundle.containsKey(messageKey) ? bundle.getString(messageKey) : messageKey;
        } catch (Exception ex) {
            return messageKey;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
