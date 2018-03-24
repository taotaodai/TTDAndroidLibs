//package hzst.android.zxing;
//
//import hzst.android.R;
//
//import java.io.IOException;
//import java.util.Vector;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.res.AssetFileDescriptor;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.media.MediaPlayer.OnCompletionListener;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Vibrator;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.SurfaceHolder;
//import android.view.SurfaceHolder.Callback;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.Result;
//
//
//public class CaptureActivity extends Activity implements Callback {
//
//    private CaptureActivityHandler handler;
//    private ViewfinderView viewfinderView;
//    private boolean hasSurface;
//    private Vector<BarcodeFormat> decodeFormats;
//    private String characterSet;
//    private InactivityTimer inactivityTimer;
//    private MediaPlayer mediaPlayer;
//    private boolean playBeep;
//    private static final float BEEP_VOLUME = 0.10f;
//    private boolean vibrate;
//
//    private LinearLayout ll;
//    private ImageView iv_more;
//    private PopupWindow popupWindow;
//    private View contentView;
//    private ListView lv_list;
//    private int[] imgs = {R.mipmap.ic_launcher, R.mipmap.ic_launcher};
//    private String[] texts = {"从相册选取二维码", "打开闪光灯"};
//
//    private PopupWindowAdapter adapter;
//
//    private int REQUEST_CODE = 0;
//
//
//    /**
//     * Called when the activity is first created.
//     */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scan_code);
//        // 初始化 CameraManager
//        CameraManager.init(getApplication());
//        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
//        hasSurface = false;
//        inactivityTimer = new InactivityTimer(this);
//
//        initView();
//        initEvents();
//    }
//
//    public void initView() {
//        ll = (LinearLayout) findViewById(R.id.ll);
//        iv_more = (ImageView) findViewById(R.id.iv_more);
//    }
//
//    private void initEvents() {
//        ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        iv_more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopupWindow(v);
//            }
//        });
//
//    }
//
//    public void showPopupWindow(View view) {
//        if (popupWindow == null) {
//            contentView = LayoutInflater.from(CaptureActivity.this).inflate(R.layout.popup, null);
//            lv_list = (ListView) contentView.findViewById(R.id.lv_list);
//            lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    switch (position) {
//                        case 0:
//                            popupWindow.dismiss();
//                            //打开手机中的相册
//                            Intent innerIntent = new Intent(); // "android.intent.action.GET_CONTENT"
//                            if (Build.VERSION.SDK_INT < 19) {
//                                innerIntent.setAction(Intent.ACTION_GET_CONTENT);
//                            } else {
//                                innerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//                            }
//
//                            innerIntent.setType("image/*");
//
//                            Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
//
//                            CaptureActivity.this.startActivityForResult(wrapperIntent, REQUEST_CODE);
//                            break;
//                        case 1:
//                            popupWindow.dismiss();
//                            CameraManager.get().flashHandler();
//                            if ("打开闪光灯".equals(texts[position])){
//                                texts[position] = "关闭闪光灯";
//                            }else {
//                                texts[position] = "打开闪光灯";
//                            }
//                            adapter.notifyDataSetChanged();
//                            break;
//                    }
//                }
//            });
//
//            if (adapter == null) {
//                adapter = new PopupWindowAdapter(CaptureActivity.this, texts, imgs);
//                lv_list.setAdapter(adapter);
//            } else {
//                adapter.notifyDataSetChanged();
//            }
//            popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
//            popupWindow.setBackgroundDrawable(new BitmapDrawable());
//            popupWindow.setFocusable(true);
//            popupWindow.setOutsideTouchable(true);
//        }
//        popupWindow.showAsDropDown(view);
//        //popupWindow.showAsDropDown(view, 0, 0, Gravity.RIGHT);
//        //popupWindow.showAtLocation(ll, Gravity.RIGHT, 0, 0);
//        //popupWindow.update();
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
//        SurfaceHolder surfaceHolder = surfaceView.getHolder();
//        if (hasSurface) {
//            initCamera(surfaceHolder);
//        } else {
//            surfaceHolder.addCallback(this);
//            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        }
//        decodeFormats = null;
//        characterSet = null;
//
//        playBeep = true;
//        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
//        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
//            playBeep = false;
//        }
//        initBeepSound();
//        vibrate = true;
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (handler != null) {
//            handler.quitSynchronously();
//            handler = null;
//        }
//        CameraManager.get().closeDriver();
//    }
//
//    @Override
//    protected void onDestroy() {
//        inactivityTimer.shutdown();
//        super.onDestroy();
//    }
//
//    private void initCamera(SurfaceHolder surfaceHolder) {
//        try {
//            CameraManager.get().openDriver(surfaceHolder);
//        } catch (IOException ioe) {
//            return;
//        } catch (RuntimeException e) {
//            return;
//        }
//        if (handler == null) {
//            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
//        }
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        if (!hasSurface) {
//            hasSurface = true;
//            initCamera(holder);
//        }
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        hasSurface = false;
//
//    }
//
//    public ViewfinderView getViewfinderView() {
//        return viewfinderView;
//    }
//
//    public Handler getHandler() {
//        return handler;
//    }
//
//    public void drawViewfinder() {
//        viewfinderView.drawViewfinder();
//
//    }
//
//    public void handleDecode(final Result obj, Bitmap barcode) {
//        inactivityTimer.onActivity();
//        playBeepSoundAndVibrate();
//        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        if (barcode == null) {
//            dialog.setIcon(null);
//        } else {
//
//            Drawable drawable = new BitmapDrawable(barcode);
//            dialog.setIcon(drawable);
//        }
//        dialog.setTitle("扫描结果");
//        dialog.setMessage(obj.getText());
//        dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //用默认浏览器打开扫描得到的地址
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(obj.getText());
//                intent.setData(content_url);
//                startActivity(intent);
//                finish();
//            }
//        });
//        dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//        dialog.create().show();
//    }
//
//    private void initBeepSound() {
//        if (playBeep && mediaPlayer == null) {
//            // The volume on STREAM_SYSTEM is not adjustable, and users found it
//            // too loud,
//            // so we now play on the music stream.
//            setVolumeControlStream(AudioManager.STREAM_MUSIC);
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayer.setOnCompletionListener(beepListener);
//
//            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
//            try {
//                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
//                file.close();
//                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
//                mediaPlayer.prepare();
//            } catch (IOException e) {
//                mediaPlayer = null;
//            }
//        }
//    }
//
//    private static final long VIBRATE_DURATION = 200L;
//
//    private void playBeepSoundAndVibrate() {
//        if (playBeep && mediaPlayer != null) {
//            mediaPlayer.start();
//        }
//        if (vibrate) {
//            System.out.println("444");
//            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//            System.out.println("4——1");
//            vibrator.vibrate(VIBRATE_DURATION);
//            System.out.println("4——2");
//        }
//    }
//
//    /**
//     * When the beep has finished playing, rewind to queue up another one.
//     */
//    private final OnCompletionListener beepListener = new OnCompletionListener() {
//        public void onCompletion(MediaPlayer mediaPlayer) {
//            mediaPlayer.seekTo(0);
//        }
//    };
//
//}