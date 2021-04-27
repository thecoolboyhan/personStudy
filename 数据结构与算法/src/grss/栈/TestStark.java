package grss.栈;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Stack;

/**
 * 栈的基本使用
 * @author hp
 *
 */
public class TestStark {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
		Wyt wyt = new Wyt();
		wyt.setaaa();
		Class<?> aClass = Class.forName("grss.栈.Wyt$aaa");
		Method toString = aClass.getMethod("toString");
		toString.invoke(wyt.getAaa());
	}
}

class Wyt{
	private aaa aaa;
	public void setaaa(){
		aaa = new aaa();
		aaa.setAbb("222");
	}

	public Wyt.aaa getAaa() {
		return aaa;
	}

	public void setAaa(Wyt.aaa aaa) {
		this.aaa = aaa;
	}

	class aaa{
		private String abb;

		@Override
		public String toString() {
			return "aaa{" +
							"abb='" + abb + '\'' +
							'}';
		}

		public String getAbb() {
			return abb;
		}

		public void setAbb(String abb) {
			this.abb = abb;
		}
	}
}
