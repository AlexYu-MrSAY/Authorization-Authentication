package com.company.syugai.controller;

import io.javalin.http.Context;

public interface Controller<T> {
    void getAll(Context context);
    void getOne(Context context, int id);
    void post(Context context);
    void patch(Context context, int id);
    void delete(Context context, int id);
}
