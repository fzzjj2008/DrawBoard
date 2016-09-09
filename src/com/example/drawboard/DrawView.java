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
	
	// 画笔
	private Paint paint = null;
	// 绘画路径
	private Path path = null;
	// 图片缓冲区（若不定义此，则不能实现多种颜色）
	private Bitmap cacheBitmap = null;
	// 画布缓冲区
	private Canvas cacheCanvas = null;
	// 临时保存的XY坐标，为了实现quadTo
	private float tempX = 0;
	private float tempY = 0;
	//final static String TAG_ERROR = "TAG_ERROR";

	// 三个构造函数，分别代表：代码创建View；xml创建View；通过Theme指定生成
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
	
	// 初始化操作
	public void initialize() {
		// 创建画笔
		paint = new Paint();
		// 画笔颜色
		paint.setColor(Color.RED);
		// 画笔粗细
		paint.setStrokeWidth(5);
		// 抗锯齿
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		// 描边，不填充
        paint.setStyle(Paint.Style.STROKE);
        // 创建path，即绘制路径
		path = new Path();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 创建缓冲区
		//cacheBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		cacheBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		cacheBitmap.eraseColor(Color.WHITE);
        cacheCanvas = new Canvas(cacheBitmap);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 绘制缓存图片
		canvas.drawBitmap(cacheBitmap, 0, 0, paint);
		// 绘制当前路径
		canvas.drawPath(path, paint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float currentX = event.getX();
		float currentY = event.getY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN : {
				// 画线函数，移动到点(currentX, currentY)
				//path.reset();
				path.moveTo(currentX, currentY);
				tempX = currentX;
				tempY = currentY;
				break;
			}
			case MotionEvent.ACTION_MOVE : {
				// 画线函数，连线到点(currentX, currentY)
				// 平滑曲线
				path.quadTo(tempX, tempY, currentX, currentY);
				tempX = currentX;
				tempY = currentY;				
				// 线段
				//path.lineTo(currentX, currentY);
				break;
			}
			case MotionEvent.ACTION_UP : {
				// 将绘制的路径保存到cacheCanvas上，进而保存到cacheBitmap上
				cacheCanvas.drawPath(path, paint);
				path.reset();
				break;
			}
		}
		// 刷新界面，调用onDraw()
		invalidate();
		// true表明该事件已经处理完毕，不能返回super.onTouchEvent(event)
		return true;
	}
	
	// 设置画笔颜色
	public void setColor(int color) {
		paint.setColor(color);
	}
	
	// 设置画笔粗细
	public void setSize(float size) {
		paint.setStrokeWidth(size);
	}
	
	// 清屏
	public void clearScreen() {
		// 位图变白色
		cacheBitmap.eraseColor(Color.WHITE);
		//cacheBitmap.eraseColor(Color.TRANSPARENT);
		// 清空path
		path.reset();
		// 刷新界面
		invalidate();
	}

	// 保存图片
	public boolean saveBitmap() {
		// 获取系统时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
		String picName = new String(sdf.format(new Date()));
		// 保存图片
		try {
			// 如果手机插入了SD卡，而且应用程序具有访问SD的权限
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 获取SD卡的目录
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
