package org.eu.hanana.reimu.app.mod.enchantment.core;

import lombok.SneakyThrows;
import org.eu.hanana.reimu.app.mod.enchantment.event.CommandEvent;
import org.eu.hanana.reimu.lib.satori.v1.protocol.eventtype.MessageEvent;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static CommandEvent parseCommand(String input, MessageEvent messageEvent){
        if (!input.startsWith("/")) input="/"+input;
        List<String> result = new ArrayList<>();

        // 使用正则表达式处理空格和引号
        String regex = "\"([^\"]*)\"|(\\S+)";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            // 获取匹配到的非空格内容或引号中的内容
            if (matcher.group(1) != null) {
                result.add(matcher.group(1));  // 引号中的内容
            } else if (matcher.group(2) != null) {
                result.add(matcher.group(2));  // 普通的非空格内容
            }
        }
        return new CommandEvent(result.getFirst().substring(1), result.subList(1,result.size()),messageEvent);
    }
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) { // 遍历父类
            for (Field field : clazz.getDeclaredFields()) {
                fields.add(field);
            }
            clazz = clazz.getSuperclass(); // 获取父类
        }
        return fields;
    }
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public static <T>T copyObj(T o){
        Class<?> aClass = o.getClass();
        Unsafe unsafe;
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.trySetAccessible();
        unsafe= (Unsafe) theUnsafe.get(null);
        Object o1 = unsafe.allocateInstance(aClass);
        Field[] declaredFields = getAllFields(aClass).toArray(new Field[0]);
        for (Field declaredField : declaredFields) {
            if (!declaredField.trySetAccessible()) continue;
            declaredField.set(o1,declaredField.get(o));
        }
        return (T) o1;
    }
}
