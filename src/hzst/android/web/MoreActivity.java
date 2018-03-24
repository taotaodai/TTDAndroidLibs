package hzst.android.web;

import android.os.Bundle;
import android.widget.LinearLayout;

import hzst.android.R;
import hzst.android.view.TopNavigationBar;

/**
 * Created by Administrator on 2016/6/12.
 */
public class MoreActivity extends WebActivity{
    private TopNavigationBar tnbMore;
    private LinearLayout llayContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_more);

        llayContent = (LinearLayout) findViewById(R.id.llay_content);
        tnbMore = (TopNavigationBar) findViewById(R.id.tnb_more);
        tnbMore.setTitle(getIntent().getStringExtra("title"));
        String url = getIntent().getStringExtra("url");
        llayContent.addView(webView);
        webView.loadUrl(url);

    }
}
