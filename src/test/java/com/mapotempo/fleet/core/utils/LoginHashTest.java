package com.mapotempo.fleet.core.utils;

import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.utils.HashHelper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoginHashTest {

    @Test
    @DisplayName("email should be sha-256 hashed")
    void test1() throws CoreException {
        String res = HashHelper.sha256("driver1@mapotempo.com");
        Assertions.assertEquals("5ffbb992f9c44a4e7a50897f785c5f63d38e587130f7cf86a07359d609dc50dd", res);
    }

    @Test
    @DisplayName("email should be sha-256 hashed in lower case")
    void test2() throws CoreException {
        String res = HashHelper.sha256("driver1@mapotempo.com");
        Assertions.assertNotEquals("5FFBB992F9C44A4E7A50897F785C5F63D38E587130F7CF86A07359D609DC50DD", res);
    }
}
