package com.wechatui.model;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author luo
 * @create 2022/4/18 下午11:14
 * 用于把测试用例的YAML数据反序列化为该对象
 */
public class CaseObjectModel {

    private ArrayList<CaseDataObjectModel> data;
    private ArrayList<CaseObjectModel> testCaseList = new ArrayList<>();
    private int index = 0;

    public ArrayList<CaseDataObjectModel> getData() {
        return data;
    }

    public void setData(ArrayList<CaseDataObjectModel> data) {
        this.data = data;
    }

    public ArrayList<CaseObjectModel> getTestCaseList() {
        return testCaseList;
    }

    public void setTestCaseList(ArrayList<CaseObjectModel> testCaseList) {
        this.testCaseList = testCaseList;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * 测试用例裂变，基于数据自动生成多份测试用例
     * @return
     */
    public List<CaseObjectModel> testcaseGenerate(){
        for (int i = 0; i < data.size(); i++) {
            CaseObjectModel newTestCaseObj = new CaseObjectModel();
            newTestCaseObj.data=this.data;
            newTestCaseObj.index=i;
            testCaseList.add(newTestCaseObj);
        }
        return testCaseList;
    }
    /*
     *测试数据中的变量替换
     */
    public void getActualValue(){

        this.data.forEach(caseData->{

            //由于遍历过程中不允许修改集合，所以定义一个HashMap结构对应data->parameters，遍历时把变量替换后的结果放进该HashMap中，到最后才整体替换CaseDataObjectModel的parameters属性
            //而且要替换actual: {isElemExist: ["by.xpath","//span[text()=$(username)]"]}中的$(username)也需要这个hashmap
            HashMap<String,Object> tempParams = new HashMap<>();
            for(String key : caseData.getParameters().keySet()){//通过遍历完成CaseDataObjectModel中parameters属性中出现的$(..)变量替换
                Object value=caseData.getParameters().get(key);
                Object actual = value;
                if (value instanceof String && ((String) value).startsWith("${") && ((String)value).contains("#")){ //对应该结构：$(com.wechatui.utils.FakerUtils#getRandomString#6)
                    String str1 = ((String)value).replace("${","").replace("}","");
                    String[] strArray = str1.split("#");
                    String[] params=null;
                    if (strArray.length==3){  //调用的方法存在参数才执行下面语句，否则报空指针异常
                        params=strArray[2].split(",");
                    }
                    try {
                        Method method=Arrays.stream(Class.forName(strArray[0]).getMethods()).filter(m->m.getName().equals(strArray[1])).findFirst().get();//通过反射获取方法
                        actual = method.invoke(null,(Object[]) params);//方法执行
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }else if (value instanceof String && ((String) value).startsWith("${") && !((String)value).contains("%")){//对应结构：$(username)
                    //需要从tempParams中取值进行替换
                    //按理在用例设计时这种parameters中的变量相互引用的情况比较少，暂不考虑
                }else{//此种情况不做任何变量替换
                }
                //把key-actual键值对放入tempParams中，actual此时可能已被变量替换
                tempParams.put(key,actual);
            }

            //通过遍历完成CaseDataObjectModel中asserts属性中出现的$(..)变量替换 ， 验证代码对应testcase为Test01.java的test_08和test_09
            for (AssertModel assertModel:caseData.getAsserts()){
                HashMap<String,ArrayList<String>> stepMap=assertModel.getActual(); //取得actual值，是HashMap结构

                for (Map.Entry entry : stepMap.entrySet()){  //通过Map.Entry遍历集合
                    //System.out.println(entry.getKey());
                    //System.out.println(entry.getValue());
                    ArrayList<String> methodParams = (ArrayList<String>)entry.getValue();//取得该Entry的值，该值是一个List集合
                    ArrayList<String> arrayList = new ArrayList<>();  //用于存放变量替换后的结果，数据结构与Entry.getValue()一致
                    //正则表达式匹配${...}
                    String varRegEx = "\\$\\{.*?\\}";
                    Pattern pattern = Pattern.compile(varRegEx);
                    Matcher matcher=null;
                    for (String param : methodParams){  //集合变量
                        //判断是否包含${}，若包含则取出${...}中的内容，最后整体替换掉${...}
                        String tempStr = param;
                        if (matcher==null){
                            matcher=pattern.matcher(param);
                        }else {
                            matcher.reset(param);
                        }
                        while (matcher.find()){  //find()返回目标字符串中是否包含与Pattern匹配的子串。
                            String temp=matcher.group();  //group()返回上一次与Pattern匹配的子串
                            String v = temp.substring(2,temp.length() -1);  //取变量名，即${a}中的a
                            tempStr=tempStr.replace(temp,(String)tempParams.get(v));  //变量替换
                        }
                        arrayList.add(tempStr);
                    }
                    entry.setValue(arrayList); //重新设置该entry的值，该List存放了当前变量替换后的结果
                }
            }
            caseData.setParameters(tempParams);
        });
        //return null;
    }

    //重写toString方法读取用例标题caseTitle在用例中进行展示
    @Override
    public String toString(){
        String title = this.getData().get(index).getCaseTitle();
        if ( title != null){
            return title;
        }else {
            return super.toString();
        }
    }
}
