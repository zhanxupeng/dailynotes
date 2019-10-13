package com.mr.xue.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhanxp
 * @version 1.0  2019/8/18
 * @Description todo
 */
public class Main {
    private final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static void date() {
        //创建一个代表系统当前日期的Date对象
        Date date = new Date();

        //把日期格式化为yyyy-MM-dd HH:mm:ss显示
        System.out.println("当前时间为：" + dateFormat.format(date));

        //创建一个我们制定的时间的Date对象，这里具体可以去看Date的构造方法
        //年份为到1900年的差额，月份从0开始，目前已经不推荐使用，下面日期为2019-9-8
        //todo 有删除线的代表现在不推荐使用
        date = new Date(2019 - 1900, 9 - 1, 8, 11, 34, 56);
        System.out.println("设置的时间为：" + dateFormat.format(date));

        //获取年份
        int year = date.getYear() + 1900;
        System.out.println("年份为：" + year);

        //获取月份
        int month = date.getMonth() + 1;
        System.out.println("月份为：" + month);

        //获取日期
        int day = date.getDate();
        System.out.println("一个月份中的天数为：" + day);

        //获取小时
        int hour = date.getHours();
        System.out.println("一天中的小时为：" + hour);

        //获取分钟
        int minute = date.getMinutes();
        System.out.println("分钟为：" + minute);

        //获得秒
        int second = date.getSeconds();
        System.out.println("秒数为：" + second);

        //获取距离1970年的毫秒数
        long time = date.getTime();
        System.out.println("距离1900年的毫秒数为：" + time);
    }

    private static void dateFormat() {
        //==================日期转字符串======================

        //默认的日期格式显示的信息不方便阅读，通常我们需要格式化显示成可以阅读的信息
        Date date = new Date();

        //默认的格式化显示
        System.out.println("默认日期显示：" + date);

        //DateFormat有一个实现类SimpleDateFormat,我们通常用这个工具类格式化
        //y代表年份，M代表月份，d代表日期 H小时 m分钟 s秒 S毫秒，可以随便自己组合
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

        System.out.println("格式化后的信息:" + dateFormat.format(date));

        dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        System.out.println("格式化后的信息:" + dateFormat.format(date));

        dateFormat = new SimpleDateFormat("HH:mm:ss");
        System.out.println("格式化后的信息:" + dateFormat.format(date));

        dateFormat = new SimpleDateFormat("HH时mm分");
        System.out.println("格式化后的信息:" + dateFormat.format(date));

        //================字符串转换为日期====================

        //利用DateFormat还可以把字符串转化为日期,约定字符串的格式为yyyy-MM-dd,约定格式随便写，只要和你输入的格式一致就可以了
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            //因为有些字符串输入格式不正确，所以需要捕获异常
            date = dateFormat.parse("2019-12-13");

            //再转化成字符串输出
            System.out.println("我输入的时间为：" + dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void calendar() {
        // Calendar类的功能要比Date类强大很多,主要用于一些对于日期的操作

        //创建一个代表系统当前日期的Calendar对象
        Calendar calendar = Calendar.getInstance();
        //获取date
        Date date = calendar.getTime();
        System.out.println("1.时间为:" + dateFormat.format(date));

        //可以通过设置日期，指定日期
        calendar.set(2019, 9 - 1, 8);
        System.out.println("2.时间为:" + dateFormat.format(calendar.getTime()));

        //也可以只设置一个值，比如只设置年份
        calendar.set(Calendar.YEAR, 2018);
        System.out.println("3.时间为:" + dateFormat.format(calendar.getTime()));

//        Calendar.YEAR——年份
//        Calendar.MONTH——月份
//        Calendar.DATE——日期
//        Calendar.DAY_OF_MONTH——日期，和上面的字段意义相同
//        Calendar.HOUR——12小时制的小时
//        Calendar.HOUR_OF_DAY——24小时制的小时
//        Calendar.MINUTE——分钟
//        Calendar.SECOND——秒
//        Calendar.DAY_OF_WEEK——星期几

        //可以在创建的时间对象上前移或者后退一段时间

        //10天后的时间
        calendar.add(Calendar.DATE, 10);
        System.out.println("4.时间为:" + dateFormat.format(calendar.getTime()));

        //10天前的时间
        calendar.add(Calendar.DATE, -10);
        System.out.println("5.时间为:" + dateFormat.format(calendar.getTime()));

        //获取时间
        //获得年份
        int year = calendar.get(Calendar.YEAR);
        System.out.println("年份为：" + year);

        //获得月份
        int month = calendar.get(Calendar.MONTH) + 1;
        System.out.println("月份为:" + month);

        //获取日期
        int day = calendar.get(Calendar.DATE);
        System.out.println("一个月中的当前天数为：" + day);

        //获得星期几,1代表星期日、2代表星期1、3代表星期二，
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println("一个星期中的星期为：" + dayOfWeek);
    }

    public static void main(String[] args) {
//        date();
//        dateFormat();
        calendar();
    }
}
