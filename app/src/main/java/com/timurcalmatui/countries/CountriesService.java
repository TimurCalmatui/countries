package com.timurcalmatui.countries;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

interface CountriesService {
    @GET("names.json")
    Call<Map<String, String>> listCountryNames();

    @GET("capital.json")
    Call<Map<String, String>> listCountryCapitals();

    @GET("currency.json")
    Call<Map<String, String>> listCountryCurrencies();
}
