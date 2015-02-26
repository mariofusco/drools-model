package org.drools.model.functions;

import org.drools.model.Variable;
import org.mvel2.asm.ClassReader;
import org.mvel2.asm.ClassVisitor;
import org.mvel2.asm.Handle;
import org.mvel2.asm.MethodVisitor;
import org.mvel2.asm.Opcodes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.Files.createTempDirectory;
import static java.util.UUID.randomUUID;

public class LambdaIntrospector {

    private static final LambdaIntrospector INSTANCE = new LambdaIntrospector();

    private static URLClassLoader lambdaClassLoader;

    public static synchronized void init() {
        if (lambdaClassLoader != null) {
            return;
        }
        final String DUMP_CLASSES_PROP = "jdk.internal.lambda.dumpProxyClasses";
        try {
            String lambdaDir = createTempDirectory("lambda").toString();
            System.setProperty(DUMP_CLASSES_PROP, lambdaDir);
            lambdaClassLoader = new URLClassLoader(new URL[] { new File(lambdaDir).toURI().toURL() });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, OwnerClassVisitor> classVisitors = new HashMap<String, OwnerClassVisitor>();

    public static LambdaIntrospector getInstance() {
        return INSTANCE;
    }

    public static String getLambdaFingerprint(Class lambdaClass, Variable... vars) {
        StringBuilder sb = new StringBuilder();
        sb.append(LambdaIntrospector.getInstance().introspectLambda(lambdaClass));
        for (Variable var : vars) {
            sb.append("Var ").append(System.identityHashCode(var)).append("\n");
        }
        return sb.toString();
    }

    public String introspectLambda(Class<?> lambdaClass) {
        if (lambdaClassLoader == null) {
            // LambdaIntrospection is off
            return randomUUID().toString();
        }
        LambdaClassVisitor lambdaClassVisitor = new LambdaClassVisitor(lambdaClassLoader, lambdaClass);
        String lambdaOwner = lambdaClassVisitor.lambdaClassOwner.replace('/', '.');
        return getOwnerClassVisitor(lambdaOwner).getMethodFingerprint(lambdaClassVisitor.lambdaMethodName);
    }

    private OwnerClassVisitor getOwnerClassVisitor(String lambdaOwner) {
        OwnerClassVisitor ownerClassVisitor = classVisitors.get(lambdaOwner);
        if (ownerClassVisitor == null) {
            ownerClassVisitor = new OwnerClassVisitor(lambdaOwner);
            classVisitors.put(lambdaOwner, ownerClassVisitor);
        }
        return ownerClassVisitor;
    }

    private static class OwnerClassVisitor extends ClassVisitor {

        private final Map<String, String> methodsMap = new HashMap<String, String>();

        private final String lambdaOwner;

        public OwnerClassVisitor(String lambdaOwner) {
            super(Opcodes.ASM5);
            this.lambdaOwner = lambdaOwner;
            introspect();
        }

        private void introspect() {
            try {
                new ClassReader(lambdaOwner).accept(this, 0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private String getMethodFingerprint(String methodName) {
            return methodsMap.get(methodName);
        }

        private void setMethodFingerprint(String methodName, String methodFingerprint) {
            methodsMap.put(methodName, methodFingerprint);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            //System.out.println("visitMethod " + name + ", " + desc + ", " + signature);
            return name.contains("$") ? new LambdaMethodVisitor(this, name) : super.visitMethod(access, name, desc, signature, exceptions);
        }

        public static class LambdaMethodVisitor extends MethodVisitor {

            private final OwnerClassVisitor ownerClassVisitor;
            private final String methodName;
            private final StringBuilder sb = new StringBuilder();

            public LambdaMethodVisitor(OwnerClassVisitor ownerClassVisitor, String methodName) {
                super(Opcodes.ASM5);
                this.ownerClassVisitor = ownerClassVisitor;
                this.methodName = methodName;
            }

            @Override
            public void visitLdcInsn(Object cst) {
                //System.out.println("visitLdcInsn " + cst);
                sb.append("const ").append(cst).append("\n");
            }

            @Override
            public void visitTypeInsn(int opcode, String type) {
                //System.out.println("visitTypeInsn " + opcode + ", " + type);
            }

            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String desc) {
                //System.out.println("visitFieldInsn " + opcode + ", " + owner + ", " + name + ", " + desc);
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                //System.out.println("visitMethodInsn " + opcode + ", " + owner + ", " + name + ", " + desc);
                sb.append(owner).append(".").append(name).append(desc).append("\n");
            }

            @Override
            public void visitEnd() {
                super.visitEnd();
                ownerClassVisitor.setMethodFingerprint(methodName, sb.toString());
            }
        }
    }

    private static class LambdaClassVisitor extends ClassVisitor {
        private final ClassLoader lambdaClassLoader;
        private final Class<?> lambdaClass;

        private String lambdaClassOwner;
        private String lambdaMethodName;

        public LambdaClassVisitor(ClassLoader lambdaClassLoader, Class<?> lambdaClass) {
            super(Opcodes.ASM5);
            this.lambdaClassLoader = lambdaClassLoader;
            this.lambdaClass = lambdaClass;
            introspect();
        }

        private void introspect() {
            String lambdaClassFilePath = lambdaClassFilePath(lambdaClass);
            InputStream classStream = null;
            try {
                classStream = lambdaClassLoader.getResourceAsStream(lambdaClassFilePath);
                ClassReader reader = new ClassReader(classStream);
                reader.accept(this, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            } catch (IOException e) {
                throw new RuntimeException("error parsing class file " + lambdaClass, e);
            } finally {
                if (classStream != null) {
                    try {
                        classStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        private String lambdaClassFilePath(Class<?> lambdaClass) {
            String lambdaClassName = lambdaClass.getName();
            String className = lambdaClassName.substring(0, lambdaClassName.lastIndexOf('/'));
            return classFilePath(className);
        }

        private String classFilePath(String className) {
            return className.replace('.', '/') + ".class";
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            //System.out.println("visitMethod " + name + ", " + desc + ", " + signature);
            return "test".equals(name) ? new PredicateMethodVisitor(this) : super.visitMethod(access, name, desc, signature, exceptions);
        }

        private static class PredicateMethodVisitor extends MethodVisitor {

            private final LambdaClassVisitor lambdaClassVisitor;

            public PredicateMethodVisitor(LambdaClassVisitor lambdaClassVisitor) {
                super(Opcodes.ASM5);
                this.lambdaClassVisitor = lambdaClassVisitor;
            }

            @Override
            public void visitTypeInsn(int opcode, String type) {
                //System.out.println("visitTypeInsn " + opcode + ", " + type);
            }

            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String desc) {
                //System.out.println("visitFieldInsn " + opcode + ", " + owner + ", " + name + ", " + desc);
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                //System.out.println("visitMethodInsn " + opcode + ", " + owner + ", " + name + ", " + desc);
                lambdaClassVisitor.lambdaClassOwner = owner;
                lambdaClassVisitor.lambdaMethodName = name;
            }

            @Override
            public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
                //System.out.println("visitInvokeDynamicInsn " + name + ", " + desc + ", " + bsm);
            }
        }
    }
}
