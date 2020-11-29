package com.example.capstoneproject.data;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.capstoneproject.api.ApiClient;
import com.example.capstoneproject.model.MovieResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.capstoneproject.constant.Constant.APIKEY;
import static com.example.capstoneproject.constant.Constant.LANGUAGE;
import static com.example.capstoneproject.constant.Constant.PAGE_ONE;
import static com.example.capstoneproject.constant.Constant.PREVIOUS_PAGE_KEY_ONE;
import static com.example.capstoneproject.constant.Constant.PREVIOUS_PAGE_KEY_TWO;

public class MovieDataSource extends PageKeyedDataSource {

    private String sort_criteria;

    public MovieDataSource(String sort_criteria) {
        this.sort_criteria = sort_criteria;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull final LoadInitialCallback callback) {
        ApiClient.getInstance().getApiService().getAllMovie(sort_criteria,APIKEY,LANGUAGE,PAGE_ONE)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()){
                            callback.onResult(response.body().getResults(),
                                    PREVIOUS_PAGE_KEY_ONE,PREVIOUS_PAGE_KEY_TWO);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams params, @NonNull LoadCallback callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams params, @NonNull final LoadCallback callback) {

        final int currentPage = (int) params.key;
        ApiClient.getInstance().getApiService().getAllMovie(sort_criteria,APIKEY,LANGUAGE,currentPage)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()){
                            int nextPage = currentPage+1;

                            callback.onResult(response.body().getResults(),nextPage);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
    }
}
