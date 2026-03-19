package edu.actorrelationship;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ActorGraphXmlIO {

    public static void save(String filePath, ActorGraph graph) throws IOException {
        // 1. 初始化资源集合与注册工厂
        ResourceSet resSet = new ResourceSetImpl();
        resSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xml", new XMIResourceFactoryImpl());

        // 2. 创建资源并添加根对象
        Resource resource = resSet.createResource(URI.createFileURI(filePath));
        resource.getContents().add(graph);

        // 3. 保存
        resource.save(Collections.EMPTY_MAP);
    }

    public static ActorGraph load(String filePath) throws IOException {
        // 1. 注册 Package（确保元模型能被识别）
        ActorrelationshipPackage.eINSTANCE.eClass();

        ResourceSet resSet = new ResourceSetImpl();
        resSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xml", new XMIResourceFactoryImpl());

        // 2. 加载资源
        Resource resource = resSet.getResource(URI.createFileURI(filePath), true);

        // 3. 获取根对象
        return (ActorGraph) resource.getContents().get(0);
    }
}
