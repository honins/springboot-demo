package com.hy.demo.component.shiro;
 
 
 import java.lang.reflect.Field;
 
 import org.apache.commons.lang3.reflect.FieldUtils;


/**
 * 反射转换：解决因类加载器不同导致的转换异常
 *
 * @comment
 * @update
 */
public class ShiroReflect {

    private static ShiroReflect instance = new ShiroReflect();

    private ShiroReflect(){
    }

    public static ShiroReflect getInstance(){
        return instance;
    }


    /**
     * 反射转换：解决因类加载器不同导致的转换异常
     * com.hs.web.common.token.AccessToken cannot be cast to com.hs.web.common.token.AccessToken
     *
     */
//     private AccessToken convertAccessToken(Object redisObject){
//         AccessToken at = new AccessToken();
//         at.setToken(ReflectUtils.getFieldValue(redisObject,"token")+"");
//         at.setUserId(ReflectUtils.getFieldValue(redisObject,"userId")+"");
//         return at;
//     }
//
}
 
//  本类私用反射方法
 class ReflectUtils{
     public static Object getFieldValue(Object obj, String fieldName){
         if(obj == null){
             return null ;
         }
         Field targetField = getTargetField(obj.getClass(), fieldName);
 
         try {
             return FieldUtils.readField(targetField, obj, true ) ;
         } catch (IllegalAccessException e) {
             e.printStackTrace();
         }
         return null ;
     }
 
     public static Field getTargetField(Class<?> targetClass, String fieldName) {
         Field field = null;
 
         try {
             if (targetClass == null) {
                 return field;
             }
 
             if (Object.class.equals(targetClass)) {
                 return field;
             }
 
             field = FieldUtils.getDeclaredField(targetClass, fieldName, true);
             if (field == null) {
                 field = getTargetField(targetClass.getSuperclass(), fieldName);
             }
         } catch (Exception e) {
         }
 
         return field;
     }
}
