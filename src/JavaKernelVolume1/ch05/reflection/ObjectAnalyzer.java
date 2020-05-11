package JavaKernelVolume1.ch05.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/* 在运行时使用反射分析对象 */
public class ObjectAnalyzer {
    private ArrayList<Object> visited = new ArrayList<>(); // 保存已访问对象

    /**
     * 可供任意类使用的通用toString 方法，返回对象的所有域及其值
     *
     * @param obj
     * @return
     */
    public String toString(Object obj) {
        if (obj == null)
            return "null";
        if (visited.contains(obj))
            return "...";
        visited.add(obj);

        Class cl = obj.getClass();
        if (cl == String.class)
            return (String) obj;

        // 为了能够査看数组内部，需要采用一种不同的方式
        if (cl.isArray()) {
            String r = cl.getComponentType() + "[]{"; // 数组类型
            for (int i = 0; i < Array.getLength(obj); i++) {
                if (i > 0)
                    r += ",";
                Object val = Array.get(obj, i); // 获取数组元素
                if (cl.getComponentType().isPrimitive()) // 基本数据类型
                    r += val;
                else
                    r += toString(val); // 非基础类型，递归调用
            }
            return r + "}";
        }

        String r = cl.getName(); // 类名
        do {
            r += "[";
            Field[] fields = cl.getDeclaredFields(); // 全部域（不包括从父类继承的成员）
            // 单个域修改访问控制： f.setAccessible(true);
            AccessibleObject.setAccessible(fields, true); // 将所有域设置可访问、可修改
            for (Field f : fields) {
                if (!Modifier.isStatic(f.getModifiers())) { // 排除类域
                    if (!r.endsWith("["))
                        r += ",";
                    r += f.getName() + "="; // 域名

                    try {
                        // 分开处理基础域和对象域
                        Object val = f.get(obj);
                        Class t = f.getType();
                        if (t.isPrimitive())
                            r += val;
                        else r += toString(val);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            r += "]";
            cl = cl.getSuperclass();
        } while (cl != null);
        return r;
    }
}
