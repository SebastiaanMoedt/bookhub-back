package fr.eni.bookhub_back.common;

import fr.eni.bookhub_back.locale.LocaleHelper;

public class ServiceHelper {

    public static <T> ServiceResponse<T> response(LocaleHelper lH, String code, String key, T data){
        return new ServiceResponse<T>(code, lH.i18n(key), data);
    }
}
