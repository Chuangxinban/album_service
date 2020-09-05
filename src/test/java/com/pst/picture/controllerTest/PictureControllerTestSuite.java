package com.pst.picture.controllerTest;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

/**
 * @author RETURN
 * @date 2020/9/5 23:00
 */
@SuiteDisplayName("controller测试")
@SelectClasses({
        UserTest.class,
        AlbumTest.class
})
@RunWith(JUnitPlatform.class)
public class PictureControllerTestSuite {

}
