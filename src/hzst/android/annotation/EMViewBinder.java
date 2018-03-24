package hzst.android.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 初始化控件
 * 	@EMViewBinder(R.id.pb_loading)
	private ProgressBar pbLoading;
 * @author wt
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EMViewBinder {
    int value();
}