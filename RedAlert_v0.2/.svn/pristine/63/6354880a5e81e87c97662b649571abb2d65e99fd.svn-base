
public class LoveReflect {
//	public static class SubClass implements Serializable {
//		private int id;
//		private String name;
//
//		public SubClass() {
//		}
//
//		public SubClass(Integer id, String name) {
//			this.id = id;
//			this.name = name;
//		}
//
//		public int getId() {
//			return id;
//		}
//
//		public void setId(int id) {
//			this.id = id;
//		}
//
//		public String getName() {
//			return name;
//		}
//
//		public void setName(String name) {
//			this.name = name;
//		}
//		
//		public void subMethod() {
//			System.out.println("hello world");
//		}
//	}
//
//	public static void main(String[] arg) {
//		SubClass subClass = new SubClass();
//
//		//【案例1】通过名字取得类的声明，再取得名字
//		System.out.println(subClass.getClass().getName());
//		
//		Class<?> demo1;
//		try {
//			//【案例2】通过名字取得类的声明
//			demo1 = Class.forName("LoveReflect$SubClass");
//			System.out.println(demo1.getName());
//
//			//【案例3】通过名字找到类的声明，再创建实例
//			SubClass subClass2 = (SubClass) Class.forName("LoveReflect$SubClass").newInstance();
//			subClass2.setId(1);
//			subClass2.setName("aaaa");
//			System.out.println(subClass2.getName());
//
//			//【案例4】拿类的的构造函数new实例
//			Class<?>[] parameterTypes = {Integer.class,String.class};
//			Constructor constructor = Class.forName("LoveReflect$SubClass").getDeclaredConstructor(parameterTypes);
//			SubClass subClass3 = (SubClass)constructor.newInstance(2,"222222222222222222");
//			System.out.println(subClass3.getName());
//			
////			改个属性值
//			Field f = subClass3.getClass().getDeclaredField("name");
//			Class<?> type = f.getType();
//			System.out.println("field type="+type.getName());
//			int mod = f.getModifiers();
//			System.out.println("modify="+Modifier.toString(mod));
//			
//			f.setAccessible(true);
//			f.set(subClass3, "bbbbbbbbbbbbbbb");
//			System.out.println(subClass3.getName());
//			
//			Map<Integer,Method> methods = new HashMap<Integer,Method>();
//			Class<?>[] parameterTypes2 = {};
//			Method method = Class.forName("LoveReflect$SubClass").getDeclaredMethod("subMethod", parameterTypes2);
//			
//			//用实例调方法
//			method.invoke(subClass3, null);
//			
//			//通过反射处理数组
//			SubClass[] subClass_ = {new SubClass(1,"bbbbbbbbbbbbbbb"), subClass, subClass2, subClass3};
//			Class<?> arrayType = subClass_.getClass().getComponentType();
//			System.out.println("array type="+arrayType.getName());
//			System.out.println("array leangth="+Array.getLength(subClass_));
//			System.out.println("first one="+((SubClass)Array.get(subClass_, 0)).getName());
//			Array.set(subClass_, 0, subClass2);
//			System.out.println("first one="+((SubClass)Array.get(subClass_, 0)).getName());
//			
//			//修改数组的大小
//			SubClass[] newTemp=(SubClass[])arrayInc(subClass_,15);
//			System.out.println("array leangth="+Array.getLength(newTemp));
//			print(newTemp);
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchFieldException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//	
//	
//	   /**
//     * 修改数组大小
//     * */
//    public static Object arrayInc(Object obj,int len){
//        Class<?>arr=obj.getClass().getComponentType();
//        Object newArr=Array.newInstance(arr, len);
//        int co=Array.getLength(obj);
//        System.arraycopy(obj, 0, newArr, 0, co);
//        return newArr;
//    }
//    /**
//     * 打印
//     * */
//    public static void print(Object obj){
//        Class<?>c=obj.getClass();
//        if(!c.isArray()){
//            return;
//        }
//        System.out.println("数组长度为： "+Array.getLength(obj));
//        for (int i = 0; i < Array.getLength(obj); i++) {
//            System.out.print(Array.get(obj, i)+" ");
//        }
//    }
}
