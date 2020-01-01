package com.leadroyal.breaker;

import com.leadroyal.breaker.ClassResourceEnumerator.ClassResource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        ClassLoader classLoader;
        if (false) {
            //加载war文件
            Path path = Paths.get("");
            //实现为URLClassLoader，加载war包下的WEB-INF/lib和WEB-INF/classes
            classLoader = Util.getWarClassLoader(path);
        } else {
            //加载jar文件，可配置多个
            final Path[] jarPaths = new Path[]{Paths.get("/Users/xuanyh/IdeaProjects/ctf/target/ctf-1.0-SNAPSHOT-jar-with-dependencies.jar")};
            //实现为URLClassLoader，加载所有指定的jar
            classLoader = Util.getJarClassLoader(jarPaths);
        }
        ClassResourceEnumerator classResourceEnumerator = new ClassResourceEnumerator(classLoader);
        Collection<ClassResource> classResources = classResourceEnumerator.getAllClasses();
        List<String> classes = classResources.stream().map(classResource -> classResource.getName().replace("/",".")).collect(
            Collectors.toList());
        List<BlackInfo> blackInfo = BreakerUtils.getDatabase(1261);
        List<String> allBanned = blackInfo.stream().flatMap(k -> k.known.stream()).map(k -> k.banned).collect(
            Collectors.toList());
        for (String banned : allBanned) {
            for (String c : classes) {
                if (c.startsWith(banned)) {
                    System.out.println(c);
                }
            }
        }


    }
}
