package tech.tresearchgroup.palila2html;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tresearchgroup.palila.controller.BasicController;
import tech.tresearchgroup.palila.controller.ReflectionMethods;
import tech.tresearchgroup.palila.model.BaseSettings;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class HTMLProjectGenerator extends BasicController {
    private static final Logger logger = LoggerFactory.getLogger(HTMLProjectGenerator.class);

    public static void main(String[] args) {
        List<String> pages = new LinkedList<>();
        List<String> classNames = ReflectionMethods.getClassNames(
            new String[]{"tech.tresearchgroup.babygalago.view.pages"}
        );
        for (String theClassName : classNames) {
            try {
                String[] classParts = theClassName.split("\\.");
                String noPackage = classParts[classParts.length - 1];
                Class theClass = ReflectionMethods.findClass(
                    noPackage.toLowerCase(),
                    new String[]{"tech.tresearchgroup.babygalago.view.pages"}
                );
                if (theClass != null) {
                    Method getter = theClass.getMethod("render");
                    Object object = ReflectionMethods.getNewInstance(theClass);
                    List rendered = (List) getter.invoke(object);
                    pages.addAll(rendered);
                } else {
                    if(BaseSettings.debug) {
                        logger.info("Null class: " + theClassName);
                    }
                }
            } catch (Exception e) {
                if(BaseSettings.debug) {
                    logger.error(theClassName);
                    e.printStackTrace();
                }
            }
        }
        logger.info(String.valueOf(pages.size()));
    }
}
