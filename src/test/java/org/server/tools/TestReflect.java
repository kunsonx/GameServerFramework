package org.server.tools;

//import static org.junit.Assert.*;
//import static org.objectweb.asm.Opcodes.*;
//
//import java.lang.invoke.CallSite;
//import java.lang.invoke.ConstantCallSite;
//import java.lang.invoke.LambdaMetafactory;
//import java.lang.invoke.MethodHandle;
//import java.lang.invoke.MethodHandles;
//import java.lang.invoke.MethodType;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.function.Function;
//
//import org.junit.Test;
//import org.junit.internal.runners.TestMethod;
//import org.objectweb.asm.ClassVisitor;
//import org.objectweb.asm.ClassWriter;
//import org.objectweb.asm.Handle;
//import org.objectweb.asm.Type;
//import org.objectweb.asm.commons.GeneratorAdapter;
//
//import com.esotericsoftware.reflectasm.MethodAccess;

public class TestReflect {
//
//	int TestMethod(String c, int b) throws Exception {
//		int a = 1;
//		return ++a;
//	}
//
//	@Test
//	public void test() throws Exception {
//
//		long startTime = System.currentTimeMillis();
//
//		int val = 0;
//		for (int i = 0; i < 1000000000; i++)
//			val += TestMethod("a", i);
//
//		System.out.println("A:" + (System.currentTimeMillis() - startTime));
//		System.out.println(val);
//	}
//
//	@Test
//	public void testC() throws NoSuchMethodException, SecurityException,
//			IllegalAccessException, IllegalArgumentException,
//			InvocationTargetException {
//
//		MethodAccess methodAccess = MethodAccess.get(TestReflect.class);
//		int index = methodAccess.getIndex("TestMethod");
//
//		long startTime = System.currentTimeMillis();
//
//		int val = 0;
//		try {
//
//			for (int i = 0; i < 1000000000; i++)
//				val += (int) methodAccess.invoke(this, index, "", i);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println(e);
//		}
//
//		System.out.println("C:" + (System.currentTimeMillis() - startTime));
//		System.out.println(val);
//	}
//
//	@Test
//	public void testD() throws Throwable {
//
//		MethodType methodType = MethodType.methodType(int.class, String.class,
//				int.class);
//		MethodHandle methodHandle = MethodHandles.lookup().findVirtual(
//				TestReflect.class, "TestMethod", methodType);
//
//		methodHandle = methodHandle.bindTo(this);
//
//		long startTime = System.currentTimeMillis();
//
//		int val = 0;
//		try {
//
//			for (int i = 0; i < 1000000000; i++)
//				val += (int) methodHandle.invoke("", i);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println(e);
//		}
//
//		System.out.println("C:" + (System.currentTimeMillis() - startTime));
//		System.out.println(val);
//	}
//
//	@FunctionalInterface
//	static interface FFSAFSA {
//
//		int aa(String a, int b) throws Exception;
//	}
//
//	@Test
//	public void testE() throws Throwable {
//		FFSAFSA a = this::TestMethod;
//
//		long startTime = System.currentTimeMillis();
//
//		int val = 0;
//		try {
//
//			for (int i = 0; i < 1000000000; i++)
//				val += a.aa("", i);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println(e);
//		}
//
//		System.out.println("E:" + (System.currentTimeMillis() - startTime));
//		System.out.println(val);
//	}
//
//
//	public static void testMethod(String s) {
//		int a = 1;
//		a++;
//		// System.out.println("hello String:" + s);
//	}
//
//	public static CallSite BootstrapMethod(MethodHandles.Lookup lookup,
//			String name, MethodType mt) throws Throwable {
//		return new ConstantCallSite(lookup.findStatic(TestReflect.class, name,
//				mt));
//	}
//
//	private static MethodType MT_BootstrapMethod() {
//		return MethodType
//				.fromMethodDescriptorString(
//						"(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;",
//						null);
//	}
//
//	private static MethodHandle MH_BootstrapMethod() throws Throwable {
//		return MethodHandles.lookup().findStatic(TestReflect.class,
//				"BootstrapMethod", MT_BootstrapMethod());
//	}
//
//	private static MethodHandle INDY_BootstrapMethod() throws Throwable {
//		CallSite cs = (CallSite) MH_BootstrapMethod().invokeWithArguments(
//				MethodHandles.lookup(),
//				"testMethod",
//				MethodType.fromMethodDescriptorString("(Ljava/lang/String;)V",
//						null));
//		return cs.dynamicInvoker();
//	}

}
