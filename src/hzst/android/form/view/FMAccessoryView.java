package hzst.android.form.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hzst.android.Constants;
import hzst.android.R;
import hzst.android.adapter.CommonAdapter;
import hzst.android.entity.Accessory;
import hzst.android.form.EventsManager;
import hzst.android.form.EventsManager.OnActivityResultListener;
import hzst.android.form.event.OnAccessoryUploadListener;
import hzst.android.form.info.AccessoryViewInfo;
import hzst.android.util.AFinalUtil;
import hzst.android.util.FileUtil;
import hzst.android.util.L;
import hzst.android.util.MediaUtil;
import hzst.android.util.SysIntentUtil;
import hzst.android.view.viewdata.GridViewForScrollView;

/**
 * 附件选择控件。
 * 展示附件列表，点击打开附件；选择添加附件(图库、照相机)；保存附件信息，用于后续上传、删除等操作。
 *
 * @author wt
 */
public class FMAccessoryView extends FMBaseView implements OnActivityResultListener {

    private List<Accessory> accessoryList = new ArrayList<>();
    private List<Accessory> netAccessories = new ArrayList<>();
    private List<Accessory> localAccessories = new ArrayList<>();
    private LinearLayout llayOptions;
    private RelativeLayout rlayChoose;
    private TextView tvTitle;
    private TextView tvCamera;
    //    private ListViewForScrollView lvAccessory;
    private View vDivider;
    private GridViewForScrollView gvAccessory;

    private Dialog dialogOperation;
    private CommonAdapter<Accessory> adapter;
    private Context context;

    private boolean choiceEnable = true;
    private boolean enable = true;
    private String savePath = Environment.getExternalStorageDirectory() + File.separator + "DownLoad" + File.separator;
    private OnTakePhotoListener onTakePhotoListener;
    private OnAccessoryDeleteListener onAccessoryDeleteListener;
    private OnAccessoryUploadListener onAccessoryUploadListener;

    private static final int TYPE_NET = 1;
    private static final int TYPE_LOCAL = 2;

    private static final int HANDLER_UPLOAD = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (onAccessoryUploadListener != null) {
                /**
                 * 通知{@link EventsManager}附件上传成功
                 */
                onAccessoryUploadListener.uploadComplete();
            }
        }
    };

    public FMAccessoryView(Context context) {
        super(context);
        this.context = context;
    }

    public FMAccessoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setOnAccessoryUploadListener(OnAccessoryUploadListener onAccessoryUploadListener) {
        this.onAccessoryUploadListener = onAccessoryUploadListener;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public List<String> getUploadAccessories() {
        List<String> list = new ArrayList<>();
        for (Accessory accessory : localAccessories) {
            if (!accessory.isUploaded()) {
                list.add(accessory.getAttachmentUrl());
            }
        }
        return list;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        if (enable) {
            rlayChoose.setVisibility(View.VISIBLE);
        } else {
            rlayChoose.setVisibility(View.GONE);
        }
    }

    public boolean isChoiceEnable() {
        return choiceEnable;
    }

    public void setChoiceEnable(boolean choiceEnable) {
        this.choiceEnable = choiceEnable;
        if (choiceEnable) {
            llayOptions.setVisibility(View.VISIBLE);
        } else {
            llayOptions.setVisibility(View.GONE);
        }
    }

    public OnTakePhotoListener getOnTakePhotoListener() {
        return onTakePhotoListener;
    }

    public void setOnTakePhotoListener(OnTakePhotoListener onTakePhotoListener) {
        this.onTakePhotoListener = onTakePhotoListener;
    }

    public OnAccessoryDeleteListener getOnAccessoryDeleteListener() {
        return onAccessoryDeleteListener;
    }

    public void setOnAccessoryDeleteListener(
            OnAccessoryDeleteListener onAccessoryDeleteListener) {
        this.onAccessoryDeleteListener = onAccessoryDeleteListener;
    }

    public List<Accessory> getAccessoryList() {
        return accessoryList;
    }

    public void setAccessoryList(List<Accessory> accessoryList) {
        this.accessoryList = accessoryList;
    }

    public List<Accessory> getNetAccessories() {
        return netAccessories;
    }

    public void setNetAccessories(List<Accessory> netAccessories) {
        if (this.netAccessories.size() > 0) {
            for (int i = 0; i < this.netAccessories.size(); i++) {
                accessoryList.remove(i);
            }
            for (int i = 0; i < netAccessories.size(); i++) {
                accessoryList.set(i, netAccessories.get(i));
            }
        } else {
            accessoryList.addAll(netAccessories);
        }
        this.netAccessories = netAccessories;
        refreshList();
    }

    public List<Accessory> getLocalAccessories() {
        return localAccessories;
    }

    //	public void setLocalAccessories(List<String> localAccessories) {
//		this.localAccessories = localAccessories;
//	}
    protected void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_accessory, this);
        llayOptions = (LinearLayout) view.findViewById(R.id.llay_options);
        vDivider = view.findViewById(R.id.v_divider);
        gvAccessory = (GridViewForScrollView) view.findViewById(R.id.gv_accessory);

//        lvAccessory = (ListViewForScrollView) view.findViewById(R.id.lv_accessory);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText(viewCreator.spacingTitle(info.getName()));
        tvTitle.setTextColor(viewCreator.titleTextColor);

        rlayChoose = (RelativeLayout) view.findViewById(R.id.rlay_choose);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, viewCreator.titlePaddingX, 0);
        rlayChoose.setLayoutParams(params);
        tvCamera = (TextView) view.findViewById(R.id.tv_camera);

        setAcVisible();
    }

    private void initEvents() {
        rlayChoose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SysIntentUtil intentUtil = new SysIntentUtil(context);
                intentUtil.openFileManager();
            }
        });

        tvCamera.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SysIntentUtil intentUtil = new SysIntentUtil(context);
                intentUtil.takePhoto();
            }
        });


        gvAccessory.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position >= netAccessories.size()) {
                    FileUtil fileUtils = new FileUtil(context);
                    fileUtils.openFile(new File(accessoryList.get(position).getAttachmentUrl()));
                } else {
                    AFinalUtil aFinalHelper = new AFinalUtil(context);
                    String filePath = netAccessories.get(position).getAttachmentUrl();
                    try {
                        aFinalHelper.openNetRes(filePath, savePath + filePath.substring(filePath.lastIndexOf("/") + 1));
                    } catch (NullPointerException e) {
                        L.showLogInfo(L.TAG_EXCEPTION, "请设置附件保存路径setSavePath");
                    }
                }
            }
        });
    }

    private void createOperationDialog(final int position, final int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(new String[]{"删除"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (type) {
                    case TYPE_NET:
                        if (onAccessoryDeleteListener != null) {
                            onAccessoryDeleteListener.deleteNetAccessory(netAccessories.get(position));
                        }
                        break;
                    case TYPE_LOCAL:
                        accessoryList.remove(position);
                        localAccessories.remove(position - netAccessories.size());
                        refreshList();
                        break;
                }
            }
        });
        dialogOperation = builder.create();
    }

    /**
     * 添加附件，由页面主动调用
     *
     * @param uri
     */
    public void addAccessory(Uri uri) {
        String path = MediaUtil.getPath(context, uri);

        accessoryList.add(new Accessory(uri, path, true));
        localAccessories.add(new Accessory(uri, path, true));
        refreshList();
    }

    public void addAccessory(String filePath) {
        accessoryList.add(new Accessory(filePath, false));
        localAccessories.add(new Accessory(filePath, false));
    }
//	public void addAccessory(List<String> pathList){
//		accessoryList.addAll(pathList);
//		refreshList();
//	}

    /**
     * 附件是否重复
     */
    public boolean isRepeated(String filePath) {
        for (Accessory accessory : localAccessories) {
            if (accessory.getAttachmentUrl().equals(filePath)) {
                Toast.makeText(context, "已经有这个文件了", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return true;
    }

    public void uploadCompleted() {
        for (Accessory accessory : localAccessories) {
            accessory.setUploaded(true);
        }
    }

    public void deleteNetAccessory(Accessory accessory) {
        netAccessories.remove(accessory);
        accessoryList.remove(accessory);
        refreshList();
    }

    private int contentViewSize;
    private int imageSize;
    private ImageLoader imageLoader;
    private int column = 3;

    public void refreshList() {
        if (adapter == null) {
//            int column = gvAccessory.getNumColumns();
            imageLoader = ImageLoader.getInstance();
            final DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_ac_empty) // 设置图片下载期间显示的图片
                    .showImageForEmptyUri(R.drawable.ic_ac_empty) // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.drawable.ic_ac_empty) // 设置图片加载或解码过程中发生错误显示的图片
                    .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                    .delayBeforeLoading(1000)  // 下载前的延迟时间
                    .cacheInMemory(false) // default  设置下载的图片是否缓存在内存中
                    .cacheOnDisk(false) // default  设置下载的图片是否缓存在SD卡中
                    .considerExifParams(false) // default
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default 设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.ARGB_8888) // default 设置图片的解码类型
                    .displayer(new SimpleBitmapDisplayer()) // default  还可以设置圆角图片new RoundedBitmapDisplayer(20)
                    .handler(new Handler()) // default
                    .build();


            contentViewSize = (int) ((phoneUtil.getWidth() - (column + 1) * density * 10) / column);
            imageSize = (int) (contentViewSize - 10 * density);
            final AbsListView.LayoutParams contentParams = new AbsListView.LayoutParams(contentViewSize, contentViewSize);
            final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(imageSize, imageSize);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);

            adapter = new CommonAdapter<Accessory>(context, accessoryList, R.layout.adapter_accessory) {

                @Override
                public void convert(ViewHolder helper, Accessory item, final int position) {
                    helper.getConvertView().setLayoutParams(contentParams);
                    ImageView iv = helper.getView(R.id.iv_accessory);
                    ImageView ivDelete = helper.getView(R.id.iv_delete);
                    if (info.isReadOnly()) {
                        ivDelete.setVisibility(View.GONE);
                    } else {
                        ivDelete.setVisibility(View.VISIBLE);
                        ivDelete.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                accessoryList.remove(position);
//                                localAccessories.remove(position - netAccessories.size());
                                refreshList();
                            }
                        });
                    }
                    iv.setLayoutParams(params);

                    if (item.isLocalFile()) {
                        imageLoader.displayImage(item.getUri().toString(), iv, options);
//                        ivDelete.setVisibility(View.VISIBLE);

//                        iv.setImageBitmap(MediaUtil.getImageThumbnail(item.getAttachmentUrl(), imageSize, imageSize));
                    } else {
                        imageLoader.displayImage(item.getAttachmentUrl(), iv, options);
//                        ivDelete.setVisibility(View.GONE);
                    }
                }

            };
            gvAccessory.setAdapter(adapter);
        } else {
            adapter.setData(accessoryList);
            adapter.notifyDataSetChanged();
        }

        setAcVisible();
//		setListViewHeightBasedOnChildren(lvAccessory);
//		lvAccessory.setSelection(lvAccessory.getChildCount() - 1);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.width = gvAccessory.getWidth();
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    private void setAcVisible() {
        if (accessoryList.size() > 0) {
            vDivider.setVisibility(View.VISIBLE);
            gvAccessory.setVisibility(View.VISIBLE);
        } else {
            vDivider.setVisibility(View.GONE);
            gvAccessory.setVisibility(View.GONE);
        }
    }

    @Override
    public void showView() {
        initView();
        initEvents();

        netAccessories = ((AccessoryViewInfo) info).getAccessories();
        for (Accessory accessory : netAccessories) {
            addAccessory(accessory.getAttachmentUrl());
        }
        if (accessoryList.size() > 0) {
            refreshList();
        }
    }

    /**
     * 选择文件、拍照时回调该方法。
     * 添加附件。
     */
    @Override
    public void activityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle;
        switch (requestCode) {
            case Constants.REQUEST_CODE_LOCALFILE:
                if (data != null) {
                    addAccessory(data.getData());
                }
//				moveToBottom();
                break;
            case Constants.REQUEST_CODE_CAMERA:
//				Bitmap photo = null;
//				Uri uri = data.getData();
//				if (uri != null) {
//					photo = BitmapFactory.decodeFile(uri.getPath());
//				}
//				if (photo == null) {
//					bundle = data.getExtras();
//					if (bundle != null) {
//						photo = (Bitmap) bundle.get("data");
//					} else {
//						Toast.makeText(context, "拍照失败", Toast.LENGTH_LONG).show();
//						return;
//					}
//				}
//				FileOutputStream fileOutputStream = null;
//				try {
//					FileUtil.createDirIfNotExists(Constants.PATH_IMG);
//					File file = new File(Constants.PATH_IMG+DateOrTimeUtil.getCurrentDate(DateOrTimeUtil.DATE_MODE_DETAIL)+".jpg");
//					// 打开文件输出流
//					fileOutputStream = new FileOutputStream(file);
//					// 生成图片文件
//					photo.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
//					FileUtil fileUtils = new FileUtil(context);
//					fileUtils.scanFileAsync(file.getPath());
//					// 相片的完整路径
//					addAccessory(file.getPath());
////					moveToBottom();
//				} catch (Exception e) {
//					L.showLogInfo(L.TAG_EXCEPTION, e.toString());
//				} finally {
//					if (fileOutputStream != null) {
//						try {
//							fileOutputStream.close();
//						} catch (Exception e) {
//							L.showLogInfo(L.TAG_EXCEPTION, e.toString());
//						}
//					}
//				}
                break;
        }
    }


    @Deprecated
    public interface OnTakePhotoListener {
        File createFile();
    }

    public interface OnAccessoryDeleteListener {
        void deleteNetAccessory(Accessory accessory);
    }


}
