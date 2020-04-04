package com.example.michalwolowiec.mechapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class User {

    @NonNull
    private String userId;
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    private String bio;
    @NonNull
    private int phoneNumber;
    @NonNull
    private String sex;
    @NonNull
    private int age;
}
