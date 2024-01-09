package com.seg2105group6project;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SignupUnitTest {
    @Test
    public void firstNameValidity_AssertTrue() {
        assertTrue(CreateParticipantAccount.firstNameValidity("Tester"));
    }
    @Test
    public void firstNameValidity_AssertFalse() {
        assertFalse(CreateParticipantAccount.firstNameValidity("Tester123@gmail.com"));
    }
}
