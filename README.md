# androidFrame  Fresco-OkHttp-gson-buterKnif-GreenDAO整合

## 直接上图

Screenshots
-------------

<img src="screenshots/1.png" height="400" alt="Screenshot"/> <img src="screenshots/2.png" height="400" alt="Screenshot"/> 
<img src="screenshots/3.png" height="400" alt="Screenshot"/> <img src="screenshots/4.png" height="400" alt="Screenshot"/> 
<img src="screenshots/5.png" height="400" alt="Screenshot"/> 

---------------

# Fresco
							  1.facebook 是一个强大的图片加载组件。 相比于ImageLoader 它的流畅性更换，因为
							   它直接是一个自定义的控件。
							   详细用法： http://www.fresco-cn.org/
							   直接上代码
							  2. jar包导入 ，仅限于androidStudio ，eclipse需自行复制工程，比较麻烦，建议开发转studio

							    compile 'com.android.support:appcompat-v7:23.1.1'
							    compile project(':mylibrary')
							    compile 'com.zhy:okhttputils:2.3.8'
							    compile 'com.facebook.fresco:fresco:0.8.1'
							    compile 'com.jakewharton:butterknife:7.0.1'
							    compile 'com.google.code.gson:gson:2.3.1'


							  3.在Application 
							  public class BaseApplication extends Application {


							    @Override
							    public void onCreate() {
								super.onCreate();
								//初始化Fresco
								Fresco.initialize(getApplicationContext());
								//初始化化OkhttpUtis
								OkHttpUtils.getInstance().debug("OkHttpUtils").setConnectTimeout(100000, TimeUnit.MILLISECONDS);
								OkHttpUtils.getInstance().setCertificates();
							     }
							   }

							   4. xml 
								
								<com.facebook.drawee.view.SimpleDraweeView
								    android:layout_gravity="center_horizontal"
								    android:id="@+id/user_avator"
								    android:layout_width="150dp"
								    android:layout_height="150dp"
								    fresco:roundAsCircle="true"
								    fresco:actualImageScaleType="focusCrop"
								    fresco:roundedCornerRadius="10dp"
								    android:layout_centerVertical="true"
								    />
							   5.一行代码 
							    Uri uri = Uri.parse("http://pica.nipic.com/2008-03-19/2008319183523380_2.jpg");		
							    userAvator.setImageURI(uri);


# OKhttp  我用zhy的封装好的工具jar ,导包初始化见上面
						 zhy 地址https://github.com/hongyangAndroid/okhttp-utils

						 1.需要在Application中初始化

							OkHttpUtils.getInstance().debug("OkHttpUtils").setConnectTimeout(100000, TimeUnit.MILLISECONDS);
								OkHttpUtils.getInstance().setCertificates();

						2.提交数据

						  String url = URL.PATH + URL.map.get("login");
						OkHttpUtils.post()
							.url(url)
							.params(params)
							.build()
							.execute(new StringCallback() {
							    @Override
							    public void onError(Call call, Exception e) {

								LG.e(TAG,e.getMessage());
							    }
							    @Override
							    public void onResponse(String s) {
								try {
								    JSONObject responseobj = new JSONObject(s);
								    boolean  isSucess = responseobj.getBoolean("success");
								    if (isSucess) {
									JSONObject jsonBody=responseobj.getJSONObject("body");
									JSONObject jsonUser=jsonBody.getJSONObject("User");
									int id=jsonUser.getInt("id");
									String mobile=jsonUser.getString("mobile");
									String password=jsonUser.getString("password");
									String userName=jsonUser.getString("userName");
									SharedPreferences.Editor ed = sp.edit();
									ed.putString(Constants.SP_MOBILE, mobile);
									ed.putString(Constants.SP_PASSWORD, password);
									ed.putInt(Constants.SP_ID, id);
									ed.putString(Constants.SP_USERNAME, userName);
									ed.commit();
									Intent intent=new Intent(LoginActivity.this,MainActivity.class);
								       startActivity(intent);
									Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
								    } else {
									String msg=responseobj.getString("msg");
									T.show(mContext, msg);

								    }
								} catch (Exception e) {
								    Toast.makeText(LoginActivity.this, "登录失败",Toast.LENGTH_SHORT).show();
								    Log.i("--loginFaild--", e.getMessage());

								}
							    }
							});

							3.提交其他数据见zhy的github 写得非常清楚！

#buterKnif Jake的作品
							地址 https://github.com/JakeWharton/butterknife

							用法见其gitHub

							主要讲一下自动生成注解代码的插件

							apply plugin: 'com.android.application'
							 apply plugin: 'com.neenbedankt.android-apt'  //插件名字

							 需要在gradle 
							 buildscript {
						 repositories {
						 jcenter()
				      }
					dependencies {
					classpath 'com.android.tools.build:gradle:1.2.3'
					classpath 'com.neenbedankt.gradle.plugins:android-apt:1.6'

					// NOTE: Do not place your application dependencies here; they belong
					// in the individual module build.gradle files
				    }
				}

				allprojects {
				    repositories {
					jcenter()
				    }
				}

				具体见
				https://segmentfault.com/q/1010000003740558
#Gson

				Gson 是 Google 提供的用来在 Java 对象和 JSON 数据之间进行映射的 Java 类库。可以将一个 JSON 字符串转成一个 Java 对象，或者反过来。

			       jar和源码下载地址: http://code.google.com/p/google-gson/downloads/list

			       public class Student {  
		    private int id;  
		    private String name;  
		    private Date birthDay;  
		  
		    public int getId() {  
			return id;  
		    }  
		  
		    public void setId(int id) {  
			this.id = id;  
		    }  
		  
		    public String getName() {  
			return name;  
		    }  
		  
		    public void setName(String name) {  
			this.name = name;  
		    }  
		  
		    public Date getBirthDay() {  
			return birthDay;  
		    }  
		  
		    public void setBirthDay(Date birthDay) {  
			this.birthDay = birthDay;  
		    }  
		  
		    @Override  
		    public String toString() {  
			return "Student [birthDay=" + birthDay + ", id=" + id + ", name="  
				+ name + "]";  
		    }  
		  
		}  




		import java.util.ArrayList;  
		import java.util.Date;  
		import java.util.List;  
		  
		import com.google.gson.Gson;  
		import com.google.gson.reflect.TypeToken;  
		  
		public class GsonTest1 {  
		  
		    public static void main(String[] args) {  
			Gson gson = new Gson();  
		  
			Student student1 = new Student();  
			student1.setId(1);  
			student1.setName("李坤");  
			student1.setBirthDay(new Date());  
		  
			// //////////////////////////////////////////////////////////  
			System.out.println("----------简单对象之间的转化-------------");  
			// 简单的bean转为json  
			String s1 = gson.toJson(student1);  
			System.out.println("简单Bean转化为Json===" + s1);  
		  
			// json转为简单Bean  
			Student student = gson.fromJson(s1, Student.class);  
			System.out.println("Json转为简单Bean===" + student);  
			// 结果:  
			// 简单Bean转化为Json==={"id":1,"name":"李坤","birthDay":"Jun 22, 2012 8:27:52 AM"}  
			// Json转为简单Bean===Student [birthDay=Fri Jun 22 08:27:52 CST 2012, id=1,  
			// name=李坤]  
			// //////////////////////////////////////////////////////////  
		  
			Student student2 = new Student();  
			student2.setId(2);  
			student2.setName("曹贵生");  
			student2.setBirthDay(new Date());  
		  
			Student student3 = new Student();  
			student3.setId(3);  
			student3.setName("柳波");  
			student3.setBirthDay(new Date());  
		  
			List<Student> list = new ArrayList<Student>();  
			list.add(student1);  
			list.add(student2);  
			list.add(student3);  
		  
			System.out.println("----------带泛型的List之间的转化-------------");  
			// 带泛型的list转化为json  
			String s2 = gson.toJson(list);  
			System.out.println("带泛型的list转化为json==" + s2);  
		  
			// json转为带泛型的list  
			List<Student> retList = gson.fromJson(s2,  
				new TypeToken<List<Student>>() {  
				}.getType());  
			for (Student stu : retList) {  
			    System.out.println(stu);  
			}  
		  
			// 结果:  
			// 带泛型的list转化为json==[{"id":1,"name":"李坤","birthDay":"Jun 22, 2012 8:28:52 AM"},{"id":2,"name":"曹贵生","birthDay":"Jun 22, 2012 8:28:52 AM"},{"id":3,"name":"柳波","birthDay":"Jun 22, 2012 8:28:52 AM"}]  
			// Student [birthDay=Fri Jun 22 08:28:52 CST 2012, id=1, name=李坤]  
			// Student [birthDay=Fri Jun 22 08:28:52 CST 2012, id=2, name=曹贵生]  
			// Student [birthDay=Fri Jun 22 08:28:52 CST 2012, id=3, name=柳波]  
		  
		    }  
		}  
							 




