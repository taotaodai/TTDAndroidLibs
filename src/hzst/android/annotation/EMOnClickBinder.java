package hzst.android.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定点击事件。
 * 请在自定义的方法中使用。
 * 	@EMOnClickBinder({R.id.tv_cancel,R.id.tv_confirm})
	public void myOnClick(View v) {
	}
 * @author wt
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EMOnClickBinder {
    int[] value();
}
