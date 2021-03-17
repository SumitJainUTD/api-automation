package com.exmple.utils.validators;

import com.exmple.utils.logs.PrintLogs;
import org.apache.commons.lang.builder.EqualsBuilder;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ObjectValidator {

    PrintLogs printLogs = new PrintLogs(getClass());

    // This function is similar to validateObjects except for calculating the diff percentage
    // for specialFields
    public <T> boolean validate (T object1, T object2, ArrayList<String> specialFields, Float percentError) {

        if (object1 == object2 ) {
            return true;
        }

        if(object1 instanceof String || object1 instanceof Integer || object1 instanceof Double
                || object1 instanceof Float || object1 instanceof Character || object1 instanceof Long || object2 instanceof Byte
                || object2 instanceof String || object2 instanceof Integer || object2 instanceof Double
                || object2 instanceof Float || object2 instanceof Character || object2 instanceof Byte || object2 instanceof Long){
            if(object1.equals(object2))
                return true;
            else
                return false;
        }

        if(!object1.getClass().getName().equalsIgnoreCase(object2.getClass().getName())) {
            printLogs.error("Classes to both classes does not match - " +object1.getClass().getName() +" : " + object2.getClass().getName());
            return false;
        }

        if(object1 instanceof List || object2 instanceof List){
            if((object1 instanceof List && object2 instanceof List==false)
                    || (object1 instanceof List==false && object2 instanceof List)){
                printLogs.error("One of the object is List and other is not ");
                return false;
            }
            List list1 = (List) object1;
            List list2 = (List) object2;
            if(list1.size()!=list2.size()) {
                printLogs.warn(Arrays.toString(list1.toArray()));
                printLogs.warn(Arrays.toString(list2.toArray()));
                printLogs.error("Size of both the Lists does not match");
                return false;
            }
            list1 = sortAnyList(list1);
            list2 = sortAnyList(list2);

            boolean result = true;
            for (int i = 0; i <list1.size() ; i++) {
                result &= validate(list1.get(i), list2.get(i), specialFields, percentError);
            }
            return result;
        }

        if(object1.getClass().isArray() || object2.getClass().isArray()) {
            if ((object1.getClass().isArray() && object2.getClass().isArray() == false)
                    || (object1.getClass().isArray() == false && object2.getClass().isArray())) {
                printLogs.error("One of the object is Array and other is not ");
                return false;
            }

            List list1 = Arrays.asList((Object[]) object1);
            List list2 = Arrays.asList((Object[]) object2);

            if(list1.size()!=list2.size()) {
                printLogs.warn(Arrays.toString(list1.toArray()));
                printLogs.warn(Arrays.toString(list2.toArray()));
                printLogs.error("Size of both the Lists does not match");
                return false;
            }

            list1 = sortAnyList(list1);
            list2 = sortAnyList(list2);

            boolean result = true;
            printLogs.info("Validating lists : " + Arrays.toString(list1.toArray()) + " and " + Arrays.toString(list2.toArray()));
            for (int i = 0; i <list1.size() ; i++) {
                result &= validate(list1.get(i), list2.get(i), specialFields, percentError);
                if(result)
                    printLogs.info(list1.get(i) + " : " + list2.get(i));
                else
                    printLogs.warn(list1.get(i) + " : " + list2.get(i));
            }
            return result;
        }

        if(object1 instanceof Map || object2 instanceof  Map){
            if((object1 instanceof Map && object2 instanceof Map==false)
                    || (object1 instanceof Map==false && object2 instanceof Map)){
                printLogs.error("One of the object is Map and other is not ");
                return false;
            }
            boolean result = true;
            Map map1 = (Map) object1;
            Map map2 = (Map) object2;
            if(map1.size()!=map2.size()) {
                printLogs.warn("FAILED.......Size of both the Maps, does not match");
                printLogs.info("Map 1: ");
                Set set = map1.keySet();
                Iterator iterator = set.iterator();
                while(iterator.hasNext()){
                    Object key = iterator.next();
                    Object objectVal1 = map1.get(key);
                    printLogs.info("Map Key : " +  key + ", Value : " + objectVal1);
                }

                printLogs.info("Map 2: ");
                Set set2 = map2.keySet();
                Iterator iterator2 = set2.iterator();
                while(iterator2.hasNext()){
                    Object key = iterator2.next();
                    Object objectVal1 = map2.get(key);
                    printLogs.info("Map Key : " +  key + ", Value : " + objectVal1);
                }
                return false;
            }


            Set set = (map2.size()>map1.size())?map2.keySet():map1.keySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()){
                Object key = iterator.next();
                Object objectVal1 = map1.get(key);
                Object objectVal2= map2.get(key);
                if(specialFields==null || !specialFields.contains(key) ){
                    if (validate(objectVal1, objectVal2, specialFields, percentError)) {
                        printLogs.info("Map Key : " + key + ", Values : " + objectVal1 + " : " + objectVal2);
                        result &= true;
                    } else {
                        printLogs.warn("FAILED.......Map Key : " + key + ", Values : " + objectVal1 + " : " + objectVal2);
                        result &= false;
                    }
                }else{
                    printLogs.info("Ignoring Field " +key + " from validation");
                }
            }
            return result;
        }

        EqualsBuilder eq = new EqualsBuilder();
        //Field[] fields = object1.getClass().getDeclaredFields();
        ArrayList<Field> fields = (ArrayList<Field>) getAllFields(new ArrayList<>(),object1.getClass());
        for(Field f : fields) {
            try {
                if(!f.getName().equalsIgnoreCase("printLogs")){
                    Method getterMethod = findGetterMethod(object1,f.getName());
                    if (getterMethod != null) {
                        if (getterMethod.invoke(object1) != null && getterMethod.invoke(object2) != null) {

                            Object objectInvoke1 = getterMethod.invoke(object1);
                            Object objectInvoke2 = getterMethod.invoke(object2);
                            boolean result = false;
                            boolean specialFieldsPresent = false;
                            if(specialFields!=null && specialFields.contains(f.getName())){
                                result = checkPercentageDiff((Integer) objectInvoke1, (Integer) objectInvoke2, percentError);
                                specialFieldsPresent = true;
                            }
                            else {
                                result = validate(objectInvoke1, objectInvoke2, specialFields, percentError);
                            }
                            eq.append(true, result);

                            if (result) {
                                if (specialFieldsPresent) {
                                    float diff = getPercentageDiff((Integer)objectInvoke1, (Integer)objectInvoke2);
                                    printLogs.info(f.getName() + " : Percent diff : " + Float.toString(diff) + " : " + objectInvoke1 + " : " + objectInvoke2);
                                }
                                else
                                    printLogs.info(f.getName() + " : " + objectInvoke1 + " : " + objectInvoke2);
                            }
                            else{
                                if (specialFieldsPresent) {
                                    float diff = getPercentageDiff((Integer)objectInvoke1, (Integer)objectInvoke2);
                                    printLogs.info("FAILED............" + f.getName() + " : Percent diff : " + Float.toString(diff) + " : " + objectInvoke1 + " : " + objectInvoke2);
                                }
                                else
                                    printLogs.info("FAILED............" + f.getName() + " : " + objectInvoke1 + " : " + objectInvoke2);
                            }
                        } else if (getterMethod.invoke(object1) != null || getterMethod.invoke(object2) != null) {
                            printLogs.info(f.getName() + " : "+getterMethod.invoke(object1) + " : "+getterMethod.invoke(object2));
                            return false;
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
        return eq.isEquals();
    }

    public <T> boolean validateObjects (T object1, T object2, ArrayList<String> ignoredFields) {

        if (object1 == object2 ) {
            return true;
        }

        if(object1 instanceof String || object1 instanceof Integer || object1 instanceof Double
                || object1 instanceof Float || object1 instanceof Character || object1 instanceof Long || object2 instanceof Byte
                || object2 instanceof String || object2 instanceof Integer || object2 instanceof Double
                || object2 instanceof Float || object2 instanceof Character || object2 instanceof Byte || object2 instanceof Long){
            if(object1.equals(object2))
                return true;
            else
                return false;
        }

        if(!object1.getClass().getName().equalsIgnoreCase(object2.getClass().getName())) {
            printLogs.error("Classes to both classes does not match - " +object1.getClass().getName() +" : " + object2.getClass().getName());
            return false;
        }

        if(object1 instanceof List || object2 instanceof  List){
            if((object1 instanceof List && object2 instanceof List==false)
                    || (object1 instanceof List==false && object2 instanceof List)){
                printLogs.error("One of the object is List and other is not ");
                return false;
            }
            List list1 = (List) object1;
            List list2 = (List) object2;
            if(list1.size()!=list2.size()) {
                printLogs.warn(Arrays.toString(list1.toArray()));
                printLogs.warn(Arrays.toString(list2.toArray()));
                printLogs.error("Size of both the Lists does not match");
                return false;
            }
            list1 = sortAnyList(list1);
            list2 = sortAnyList(list2);

            boolean result = true;
            for (int i = 0; i <list1.size() ; i++) {
                result &= validateObjects(list1.get(i), list2.get(i), ignoredFields);
            }
            return result;
        }

        if(object1.getClass().isArray() || object2.getClass().isArray()) {
            if ((object1.getClass().isArray() && object2.getClass().isArray() == false)
                    || (object1.getClass().isArray() == false && object2.getClass().isArray())) {
                printLogs.error("One of the object is Array and other is not ");
                return false;
            }

            List list1 = Arrays.asList((Object[]) object1);
            List list2 = Arrays.asList((Object[]) object2);

            if(list1.size()!=list2.size()) {
                printLogs.warn(Arrays.toString(list1.toArray()));
                printLogs.warn(Arrays.toString(list2.toArray()));
                printLogs.error("Size of both the Lists does not match");
                return false;
            }

            list1 = sortAnyList(list1);
            list2 = sortAnyList(list2);

            boolean result = true;
            printLogs.info("Validating lists : " + Arrays.toString(list1.toArray()) + " and " + Arrays.toString(list2.toArray()));
            for (int i = 0; i <list1.size() ; i++) {
                result &= validateObjects(list1.get(i), list2.get(i), ignoredFields);
                if(result)
                    printLogs.info(list1.get(i) + " : " + list2.get(i));
                else
                    printLogs.warn(list1.get(i) + " : " + list2.get(i));
            }
            return result;
        }

        if(object1 instanceof Map || object2 instanceof  Map){
            if((object1 instanceof Map && object2 instanceof Map==false)
                    || (object1 instanceof Map==false && object2 instanceof Map)){
                printLogs.error("One of the object is Map and other is not ");
                return false;
            }
            boolean result = true;
            Map map1 = (Map) object1;
            Map map2 = (Map) object2;
            if(map1.size()!=map2.size()) {
                printLogs.warn("FAILED.......Size of both the Maps, does not match");
                printLogs.info("Map 1: ");
                Set set = map1.keySet();
                Iterator iterator = set.iterator();
                while(iterator.hasNext()){
                    Object key = iterator.next();
                    Object objectVal1 = map1.get(key);
                    printLogs.info("Map Key : " +  key + ", Value : " + objectVal1);
                }

                printLogs.info("Map 2: ");
                Set set2 = map2.keySet();
                Iterator iterator2 = set2.iterator();
                while(iterator2.hasNext()){
                    Object key = iterator2.next();
                    Object objectVal1 = map2.get(key);
                    printLogs.info("Map Key : " +  key + ", Value : " + objectVal1);
                }
                return false;
            }


            Set set = (map2.size()>map1.size())?map2.keySet():map1.keySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()){
                Object key = iterator.next();
                Object objectVal1 = map1.get(key);
                Object objectVal2= map2.get(key);
                if(ignoredFields==null || !ignoredFields.contains(key) ){
                    if (validateObjects(objectVal1, objectVal2, ignoredFields)) {
                        printLogs.info("Map Key : " + key + ", Values : " + objectVal1 + " : " + objectVal2);
                        result &= true;
                    } else {
                        printLogs.warn("FAILED.......Map Key : " + key + ", Values : " + objectVal1 + " : " + objectVal2);
                        result &= false;
                    }
                }else{
                    printLogs.info("Ignoring Field " +key + " from validation");
                }
            }
            return result;
        }

        EqualsBuilder eq = new EqualsBuilder();
//        Field[] fields = object1.getClass().getDeclaredFields();
        ArrayList<Field> fields = (ArrayList<Field>) getAllFields(new ArrayList<>(),object1.getClass());
        for(Field f : fields) {
            try {
                if(!f.getName().equalsIgnoreCase("printLogs")){
                    if(ignoredFields!=null && ignoredFields.contains(f.getName())) {
                        printLogs.info("Ignoring Field " + f.getName() + " from validation");
                        continue;
                    }
                    Method getterMethod = findGetterMethod(object1,f.getName());
                    if (getterMethod != null) {
                        if (getterMethod.invoke(object1) != null && getterMethod.invoke(object2) != null) {

                            Object objectInvoke1 = getterMethod.invoke(object1);
                            Object objectInvoke2 = getterMethod.invoke(object2);
                            boolean result = validateObjects(getterMethod.invoke(object1), getterMethod.invoke(object2), ignoredFields);
                            eq.append(true, result);
                            if (result) {
                                printLogs.info(f.getName() + " : " + objectInvoke1 + " : " + objectInvoke2);
                            }
                            else
                                printLogs.warn("FAILED............."+f.getName() + " : " + objectInvoke1 + " : " + objectInvoke2);
                        } else if (getterMethod.invoke(object1) != null || getterMethod.invoke(object2) != null) {
                            printLogs.info(f.getName() + " : "+getterMethod.invoke(object1) + " : "+getterMethod.invoke(object2));
                            return false;
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
        return eq.isEquals();
    }

    public boolean checkPercentageDiff(Integer actual, Integer expected, Float percentError){
        return getPercentageDiff(actual, expected) <= percentError;
    }

    public float getPercentageDiff(Integer actual, Integer expected){
        if (expected!=0) {
            float percent = 100.0F * ((float)(actual-expected))/(float)expected;
            return percent < 0 ? -percent : percent;
        }
        return 0.0F;
    }

    public <T> Method findGetterMethod(T object, String fieldName) {
        Method getter=null;
        try {
            BeanInfo info = Introspector.getBeanInfo(object.getClass(), Object.class);
            PropertyDescriptor[] props = info.getPropertyDescriptors();
            for (PropertyDescriptor pd : props) {
                if(pd.getName().equals(fieldName))
                    getter = pd.getReadMethod();
            }

        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return getter;
    }


    public String fieldBeUsedForCompare(Object o1) {

        Field [] fields = o1.getClass().getDeclaredFields();
        for (int i = 0; i <fields.length ; i++) {
            Field field =fields[i];
            if(Long.class.isAssignableFrom(field.getType()) || Integer.class.isAssignableFrom(field.getType())
                    || Number.class.isAssignableFrom(field.getType()) || String.class.isAssignableFrom(field.getType())){
                return field.getName();
            }
        }
        return null;
    }

    public List<Method> getMethodsIgnoreCase
            (Class<?> clazz, String methodName) {
        methodName = "get"+methodName.replace("_","");
        String finalMethodName = methodName;
        return Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().toLowerCase().contains(finalMethodName.toLowerCase()))
//                .filter(m -> m.getParameterTypes().length ==  1)
//                .filter(m -> m.getParameterTypes()[0].equals(paramType))
                .collect(Collectors.toList());
    }


    public List sortAnyList(List list){
        try {
            Collections.sort(list);
        }catch (Exception ex){
            //if here means, the list is object list, ignore it
            Collections.sort(list, (o1, o2) -> {
                // Write your logic here.
                String field = fieldBeUsedForCompare(o1);
                if(field==null){
                    return 0;
                }
                List<Method> methods = getMethodsIgnoreCase(o1.getClass(), field);
                try {
                    Object x = methods.get(0).invoke(o1);
                    Object y = methods.get(0).invoke(o2);
                    if(x  instanceof String){
                        return ((String) x).compareTo(String.valueOf(y));
                    }else if( x instanceof Long){
                        return (int) ((Long) x - (Long)y);
                    }else if( x instanceof Integer){
                        return ((Integer) x - (Integer) y);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return 0;
            });
        }
        return list;
    }

    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != Object.class && type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    public static void main(String[] args) {
        HashMap<Integer, Integer> map1 = new HashMap<>();
        HashMap<Integer, Integer> map2 = new HashMap<>();

        map1.put(1,3);
        map1.put(2,13);
        map1.put(11,31);

        map2.put(1,3);
        map2.put(11,31);
        map2.put(2,14);

        System.out.println(new ObjectValidator().validateObjects(map1, map2, null));
    }
}