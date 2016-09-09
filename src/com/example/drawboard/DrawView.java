package com.example.drawboard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author fzzjj2008
 * @version 1.2
 */
public class DrawView extends View {
	
	// ����
	private Paint paint = null;
	// �滭·��
	private Path path = null;
	// ͼƬ����������������ˣ�����ʵ�ֶ�����ɫ��
	private Bitmap cacheBitmap = null;
	// ����������
	private Canvas cacheCanvas = null;
	// ��ʱ�����XY���꣬Ϊ��ʵ��quadTo
	private float tempX = 0;
	private float tempY = 0;
	//final static String TAG_ERROR = "TAG_ERROR";

	// �������캯�����ֱ�������봴��View��xml����View��ͨ��Themeָ������
	public DrawView(Context context) {
		super(context);
		initialize();
	}
	public DrawView(Context context, AttributeSet set) {
		super(context, set);
		initialize();
	}
	public DrawView(Context context, AttributeSet set, int defStyle) {
		super(context, set, defStyle);
		initialize();
	}
	
	// ��ʼ������
	public void initialize() {
		// ��������
		paint = new Paint();
		// ������ɫ
		paint.setColor(Color.RED);
		// ���ʴ�ϸ
		paint.setStrokeWidth(5);
		// �����
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		// ��ߣ������
        paint.setStyle(Paint.Style.STROKE);
        // ����path��������·��
		path = new Path();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// ����������
		//cacheBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		cacheBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		cacheBitmap.eraseColor(Color.WHITE);
        cacheCanvas = new Canvas(cacheBitmap);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// ���ƻ���ͼƬ
		canvas.drawBitmap(cacheBitmap, 0, 0, paint);
		// ���Ƶ�ǰ·��
		canvas.drawPath(path, paint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float currentX = event.getX();
		float currentY = event.getY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN : {
				// ���ߺ������ƶ�����(currentX, currentY)
				//path.reset();
				path.moveTo(currentX, currentY);
				tempX = currentX;
				tempY = currentY;
				break;
			}
			case MotionEvent.ACTION_MOVE : {
				// ���ߺ��������ߵ���(currentX, currentY)
				// ƽ������
				path.quadTo(tempX, tempY, currentX, currentY);
				tempX = currentX;
				tempY = currentY;				
				// �߶�
				//path.lineTo(currentX, currentY);
				break;
			}
			case MotionEvent.ACTION_UP : {
				// �����Ƶ�·�����浽cacheCanvas�ϣ��������浽cacheBitmap��
				cacheCanvas.drawPath(path, paint);
				path.reset();
				break;
			}
		}
		// ˢ�½��棬����onDraw()
		invalidate();
		// true�������¼��Ѿ�������ϣ����ܷ���super.onTouchEvent(event)
		return true;
	}
	
	// ���û�����ɫ
	public void setColor(int color) {
		paint.setColor(color);
	}
	
	// ���û��ʴ�ϸ
	public void setSize(float size) {
		paint.setStrokeWidth(size);
	}
	
	// ����
	public void clearScreen() {
		// λͼ���ɫ
		cacheBitmap.eraseColor(Color.WHITE);
		//cacheBitmap.eraseColor(Color.TRANSPARENT);
		// ���path
		path.reset();
		// ˢ�½���
		invalidate();
	}

	// ����ͼƬ
	public boolean saveBitmap() {
		// ��ȡϵͳʱ��
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
		String picName = new String(sdf.format(new Date()));
		// ����ͼƬ
		try {
			// ����ֻ�������SD��������Ӧ�ó�����з���SD��Ȩ��
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// ��ȡSD����Ŀ¼
				String sdCardDir = new String(
						Environment.getExternalStorageDirectory().toString()
						+ File.separator + "Pictures" + File.separator);
				File file = new File(sdCardDir + picName + ".png");
				if (file.exists()) {
					file.delete();
				}
		        file.createNewFile();
		        FileOutputStream os = new FileOutputStream(file);
		        cacheBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
				os.flush();
				os.close();
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			//Log.e(TAG_ERROR, e.getMessage(), e);
			//System.out.println(e.toString());
		}
		return false; 
	}

}
