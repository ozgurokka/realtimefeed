package com.aselsan.tums.realtimefeed;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class main2 {
    public static void main(String[] args) throws IOException {
        FileOutputStream output = new FileOutputStream("D:\\b.txt");

        Person.Builder person = Person.newBuilder();
        person.setId(1);
        person.setName("ozgur");
        person.setEmail("email");
        String number ="123456";
        Person.PhoneNumber.Builder phoneNumber = Person.PhoneNumber.newBuilder().setNumber(number);
        phoneNumber.setType(Person.PhoneType.MOBILE);
        person.addPhones(phoneNumber);


        person.build().writeTo(output);
    }
}
