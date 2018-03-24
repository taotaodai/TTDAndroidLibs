package hzst.android.form.view;

import hzst.android.form.Field;
import hzst.android.util.L;
import hzst.android.util.XmlHandler;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class FMOpinionView extends FMBaseView implements Field{

	public FMOpinionView(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void initView() {
		
	}
	
	@Override
	public void showView() {
		final List<Map<String, String>> opinions;
		String xml = info.getSubmitValues().get(0).getValue();
		if("".equals(xml)){
			addView(new View(context));
		}else{
	         //1.实例化SAXParserFactory对象 
	        SAXParserFactory factory = SAXParserFactory.newInstance();
	        // 2.创建解析器 
	        SAXParser parser;
			try {
				parser = factory.newSAXParser();
		        final XmlHandler dh = new XmlHandler("list");
		        
		        parser.parse(new ByteArrayInputStream(xml.getBytes()), dh);
		        opinions = dh.getList();
		        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				setOrientation(LinearLayout.VERTICAL);
				setLayoutParams(params);
				for (int j = 0; j < opinions.size(); j++) {
					final Map<String, String> map = opinions.get(j);
					final EditText et = new EditText(context);
					et.setLayoutParams(params);
					/*
					 * 当前任务id和流程id相同时，可以填写意见
					 */
					if(viewCreator.getParameters().get(APPROVE_TASKID).equals(map.get("TaskID")) && !info.isReadOnly()){
						et.setText(map.get("Content"));
					}else{
						
						et.setText("◆"+map.get("Content")+"\n");
						et.append(map.get("Name") + "/" + map.get("Date"));
						et.setEnabled(false);
					}
					et.addTextChangedListener(new TextWatcher() {
						
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {}
						
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count,
								int after) {}
						
						@Override
						public void afterTextChanged(Editable s) {
							et.getText();
							map.put("Content", s.toString());
							String xml = dh.createXml(opinions,"Opinion","list");
							
							viewCreator.setSubmitValue(xml, info);
//							viewCreator.updateViewInfo(info, true);
						}
					});
					addView(et);
				}
			} catch (Exception e) {
				L.showLogInfo(L.TAG_EXCEPTION, e.toString());
			}
		}
	}

}
