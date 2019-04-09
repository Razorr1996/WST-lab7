package ru.basa62.wst.lab7.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class TestService {
    @WebMethod
    public String hello() {
        return "Testing standalone service";
    }
}
