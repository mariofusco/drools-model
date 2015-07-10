package org.drools.model.functions;

import org.drools.model.Variable;
import org.mvel2.asm.ClassReader;
import org.mvel2.asm.ClassVisitor;
import org.mvel2.asm.MethodVisitor;
import org.mvel2.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LambdaIntrospector {

    private static final LambdaIntrospector INSTANCE = new LambdaIntrospector();

    private Map<String, Map<String, String>> methodFingerprintsMap = new HashMap<String, Map<String, String>>();

    private LambdaIntrospector() { }

    public static LambdaIntrospector getInstance() {
        return INSTANCE;
    }

    public static String getLambdaFingerprint(Object lambda, Variable... vars) {
        StringBuilder sb = new StringBuilder();
        sb.append( LambdaIntrospector.getInstance().introspectLambda( lambda ) );
        for ( Variable var : vars ) {
            sb.append( "Var " ).append( System.identityHashCode( var ) ).append( "\n" );
        }
        return sb.toString();
    }

    private String introspectLambda(Object lambda) {
        SerializedLambda extracted = SerializedLambda.extractLambda( (Serializable) lambda );
        return getFingerprintsForClass( lambda, extracted ).get( extracted.implMethodName );
    }

    private Map<String, String> getFingerprintsForClass(Object lambda, SerializedLambda extracted) {
        Map<String, String> fingerprints = methodFingerprintsMap.get(extracted.implClass);
        if (fingerprints == null) {
            LambdaClassVisitor visitor = new LambdaClassVisitor(lambda);

            InputStream classStream = null;
            try {
                classStream = lambda.getClass().getClassLoader()
                                    .getResourceAsStream( extracted.implClass.replace( '.', '/' ) + ".class" );
                ClassReader reader = new ClassReader(classStream);
                reader.accept(visitor, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            } catch (Exception e) {
                throw new RuntimeException( e );
            } finally {
                try {
                    classStream.close();
                } catch (IOException e) {
                    throw new RuntimeException( e );
                }
            }
            fingerprints = visitor.getMethodsMap();
            methodFingerprintsMap.put( extracted.implClass, fingerprints );
        }
        return fingerprints;
    }

    private static class LambdaClassVisitor extends ClassVisitor {

        private final Object lambda;

        private final Map<String, String> methodsMap = new HashMap<String, String>();

        public LambdaClassVisitor( Object lambda ) {
            super( Opcodes.ASM5 );
            this.lambda = lambda;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            return name.contains("$") ? new LambdaMethodVisitor(this, name) : super.visitMethod(access, name, desc, signature, exceptions);
        }

        public void setMethodFingerprint( String methodname, String methodFingerprint ) {
            methodsMap.put( methodname, methodFingerprint );
        }

        public Map<String, String> getMethodsMap() {
            return methodsMap;
        }
    }

    public static class LambdaMethodVisitor extends MethodVisitor {

        private final LambdaClassVisitor lambdaClassVisitor;
        private final String methodName;
        private final StringBuilder sb = new StringBuilder();

        public LambdaMethodVisitor(LambdaClassVisitor lambdaClassVisitor, String methodName) {
            super(Opcodes.ASM5);
            this.lambdaClassVisitor = lambdaClassVisitor;
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
            lambdaClassVisitor.setMethodFingerprint(methodName, sb.toString());
        }
    }

}
