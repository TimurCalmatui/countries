package com.timurcalmatui.countries;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Map<String, String> mCountryNames;
    private Map<String, String> mCountryCapitals;
    private Map<String, String> mCountryCurrencies;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://country.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CountriesService service = retrofit.create(CountriesService.class);

        // TODO: show progress
        Call<Map<String, String>> call = service.listCountryNames();
        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                mCountryNames = response.body();
                onRequestComplete(true);
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e(TAG, "Error: " + t);

                // TODO: show an error to the user
                onRequestComplete(false);
            }
        });

        call = service.listCountryCapitals();
        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                mCountryCapitals = response.body();
                onRequestComplete(true);
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e(TAG, "Error: " + t);

                // TODO: show an error to the user
                onRequestComplete(false);
            }
        });

        call = service.listCountryCurrencies();
        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                mCountryCurrencies = response.body();
                onRequestComplete(true);
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e(TAG, "Error: " + t);

                // TODO: show an error to the user
                onRequestComplete(false);
            }
        });
    }

    private void onRequestComplete(boolean success) {

        if (success && mCountryNames != null && mCountryCapitals != null && mCountryCurrencies != null) {
            List<CountryInfo> countries = new ArrayList<>(mCountryNames.size());
            for (Map.Entry<String, String> entry : mCountryNames.entrySet()) {
                CountryInfo info = new CountryInfo();
                info.setName(entry.getValue());
                info.setCapital(mCountryCapitals.get(entry.getKey()));
                info.setCurrency(mCountryCurrencies.get(entry.getKey()));

                countries.add(info);
            }

            mRecyclerView.setAdapter(new RecyclerViewAdapter(countries));
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CountryViewHolder> {
        private List<CountryInfo> items;

        RecyclerViewAdapter(List<CountryInfo> items) {
            this.items = items;
        }

        @Override
        public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.country_list_item, parent, false);

            return new CountryViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CountryViewHolder holder, int position) {
            CountryInfo ci = items.get(position);
            holder.name.setText(ci.getName());
            holder.capital.setText(ci.getCapital());
            holder.currency.setText(ci.getCurrency());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class CountryViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            TextView capital;
            TextView currency;

            CountryViewHolder(View v) {
                super(v);
                name = (TextView) v.findViewById(R.id.country_name);
                capital = (TextView) v.findViewById(R.id.country_capital);
                currency = (TextView) v.findViewById(R.id.country_currency);
            }
        }
    }
}
