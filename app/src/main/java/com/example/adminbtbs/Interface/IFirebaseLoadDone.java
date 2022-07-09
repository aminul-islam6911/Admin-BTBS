package com.example.adminbtbs.Interface;

import com.example.adminbtbs.Model.IDs;

import java.util.List;

public interface IFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<IDs> LocationList);

    void onFirebaseLoadFailed(String Message);
}