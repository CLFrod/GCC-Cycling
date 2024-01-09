package com.seg2105group6project;
import org.junit.Test;
import static org.junit.Assert.*;

public class Deliverable4Tests {
    @Test
    public void ageEligibilityTest1() {
        assertEquals(1, EventSearch.ageEligible(5, 6));
    }
    @Test
    public void ageEligibilityTest2() {
        assertEquals(2, EventSearch.ageEligible(5, 3));
    }
    @Test
    public void ageEligibilityTest3() {
        assertEquals(3, EventSearch.ageEligible(5, 101));
    }
    @Test
    public void ratingEligibilityTest1() {
        assertEquals(1, ClubSearch.acceptableRating(5));
    }
    @Test
    public void ratingEligibilityTest2() { assertEquals(2, ClubSearch.acceptableRating(6)); }
    @Test
    public void ratingEligibilityTest3() {
        assertEquals(3, ClubSearch.acceptableRating(-1));
    }
    @Test
    public void commentEligibilityTest1() { assertEquals(1, ClubSearch.acceptableComment("They're great people!!!")); }
    @Test
    public void commentEligibilityTest2() { assertEquals(2, ClubSearch.acceptableComment("")); }
    @Test
    public void commentEligibilityTest3() { assertEquals(3, ClubSearch.acceptableComment("Good.")); }
    @Test
    public void commentEligibilityTest4() { assertEquals(4, ClubSearch.acceptableComment("lorem ipsum faucibus vitae aliquet nec ullamcorper sit amet risus nullam eget felis eget nunc lobortis mattis aliquam faucibus purus in massa tempor nec feugiat nisl pretium fusce id velit ut tortor pretium viverra suspendisse potenti nullam ac tortor vitae purus faucibus ornare suspendisse sed nisi lacus sed viverra tellus in hac habitasse platea dictumst vestibulum rhoncus est pellentesque elit ullamcorper dignissim cras tincidunt lobortis feugiat vivamus at augue eget arcu dictum varius duis at consectetur lorem donec massa sapien faucibus et molestie ac feugiat sed lectus vestibulum mattis ullamcorper velit sed ullamcorper morbi tincidunt ornare massa eget egestas purus viverra accumsan in nisl nisi scelerisque eu ultrices vitae auctor eu augue ut lectus arcu bibendum at varius vel pharetra vel turpis nunc eget lorem dolor sed viverra ipsum nunc aliquet bibendum enim facilisis gravida neque convallis a cras semper auctor neque vitae tempus quam pellentesque nec nam aliquam sem et hello there")); }
}
