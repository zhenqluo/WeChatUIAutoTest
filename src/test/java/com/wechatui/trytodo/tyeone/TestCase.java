package com.wechatui.trytodo.tyeone;

import com.wechatui.trytodo.tyeone.one.CaseBase;
import com.wechatui.trytodo.tyeone.three.TempClzz;
import org.junit.jupiter.api.Test;

/**
 * @author law
 * @create 2022-05-2022/5/27 17:10
 */
public class TestCase extends CaseBase {
    @Test
    void search(){
        System.out.println("ContactsPageTest...........");
        System.out.println(driver);
        System.out.println(driver.getTitle());
        new TempClzz(driver).search();
    }
}
