package com.kingdom.system.util.excel;

import java.lang.annotation.*;

/*
 * 元注解@Target,@Retention,@Documented,@Inherited
 * 
 *     @Target 表示该注解用于什么地方，可能的 ElemenetType 参数包括：
 *         ElemenetType.CONSTRUCTOR 构造器声明
 *         ElemenetType.FIELD 域声明（包括 enum 实例）
 *         ElemenetType.LOCAL_VARIABLE 局部变量声明
 *         ElemenetType.METHOD 方法声明
 *         ElemenetType.PACKAGE 包声明
 *         ElemenetType.PARAMETER 参数声明
 *         ElemenetType.TYPE 类，接口（包括注解类型）或enum声明
 *         
 *     @Retention 表示在什么级别保存该注解信息。可选的 RetentionPolicy 参数包括：
 *         RetentionPolicy.SOURCE 注解将被编译器丢弃
 *         RetentionPolicy.CLASS 注解在class文件中可用，但会被VM丢弃
 *         RetentionPolicy.RUNTIME VM将在运行期也保留注释，因此可以通过反射机制读取注解的信息。
 *         
 *     @Documented 将此注解包含在 javadoc 中
 *     
 *     @Inherited 允许子类继承父类中的注解
 *   
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExcelExport {

    /**
     * 在Excel要显示的字段名
     * @return
     */
    String name() default "列";

    /**
     * 宽
     * @return
     */
    int width() default 0;

    /**
     * 在Excel分组,与校验分组类似
     * @return
     */
    String[] group() default "";

    /**
     * 该字段是否锁定不允许修改
     * @return
     */
    boolean lockBoolean() default false;

    ExcelDataEnums format() default ExcelDataEnums.DEFAULT;

    /**
     * 当format=ExcelDataEnums.SELECT时，常量值
     * @return
     */
    String[] constant() default {};

    /**
     * 当format=ExcelDataEnums.SELECT时，依赖的下拉框值
     * @return
     */
    String parentName() default "";

    /**
     * 当format=ExcelDataEnums.SELECT时，提示语
     * @return
     */
    String pointOut() default "";

    /**
     * 列标，即Excel的A列、B列、C列、D列...
     */
    String columnMark() default "";

    /**
     * 当format=ExcelDataEnums.SELECT时，当constant为空时取该属性，值为ExcelSelectAbstractClass的子类，必须实现returnSelectData方法
     * @return
     */
    Class<? extends ExcelSelectInterface> returnSelectDataClass() default ExcelSelectInterface.class;

    /**
     * 当format=ExcelDataEnums.SELECT时，当constant为空时取该属性，ExcelSelectMapInterface，必须实现returnSelectMapData方法
     * @return
     */
    Class<? extends ExcelSelectMapInterface> returnSelectMapClass() default ExcelSelectMapInterface.class;
}