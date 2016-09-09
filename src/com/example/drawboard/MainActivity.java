package com.example.drawboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * @author fzzjj2008
 * @version 1.2
 */
public class MainActivity extends Activity {
	// 记录当前绘图状态（用于颜色选择对话框和粗细选择对话框）
	// 默认为红色、中等粗细
	private int pColorIndex = 0;
	private int pSizeIndex = 2;
	// 画笔粗细资源
	private float pSizeIds[] = new float[] { 1, 3, 5, 7, 9 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.isCheckable()) {
			item.setChecked(true);
		}
		switch(item.getItemId()) {
		case R.id.paint_color:
			setColor();
			break;
		case R.id.paint_size:
			setSize();
			break;
		case R.id.clear_screen:
			clearScreen();
			break;
		case R.id.save_picture:
			savePicture();
			break;
		case R.id.change_orientation:
			changeOrientation();
			break;
		case R.id.about:
			aboutDrawView();
			break;
		}
		return true;
	}

	// 改变颜色
	public void setColor() {
		final DrawView board = (DrawView) findViewById (R.id.draw_board);
		// 改变颜色的对话框
		new AlertDialog.Builder(this)
			.setTitle("画笔颜色")
			.setIcon(R.drawable.paint)
			.setSingleChoiceItems(R.array.color_name_arr, pColorIndex, new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					int color = getResources().getIntArray(R.array.color_id_arr)[which];
					board.setColor(color);
					pColorIndex = which;
				}
			})
			.setPositiveButton("确定", null)
			.create().show();
	}
	
	// 改变画笔粗细
	public void setSize() {
		final DrawView board = (DrawView) findViewById (R.id.draw_board);
		// 改变画笔粗细的对话框
		new AlertDialog.Builder(this)
			.setTitle("画笔粗细")
			.setIcon(R.drawable.paint)
			.setSingleChoiceItems(R.array.size_name_arr, pSizeIndex, new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					float size = pSizeIds[which];
					board.setSize(size);
					pSizeIndex = which;
				}
			})
			.setPositiveButton("确定", null)
			.create().show();
	}
	
	// 清屏
	public void clearScreen() {
		new AlertDialog.Builder(this)
			.setTitle("清屏")
			.setIcon(R.drawable.clear)
			.setMessage("是否要进行清屏?")
			.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					DrawView board = (DrawView) findViewById (R.id.draw_board);
					board.clearScreen();
				}
			})
			.setNegativeButton("取消", null)
			.create().show();
	}
	
	// 保存图片
	public void savePicture() {
		DrawView board = (DrawView) findViewById (R.id.draw_board);
		boolean picSaved = board.saveBitmap();
		if (picSaved == true) {
			Toast.makeText(this, "图片已保存至/mnt/sdcard/Pictures/根目录下",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "无法将图片写入SD卡", Toast.LENGTH_SHORT).show();			
		}
	}
	
	// 旋转屏幕
	public void changeOrientation() {
		Configuration config = getResources().getConfiguration();
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			Toast.makeText(this, "设为横屏", Toast.LENGTH_SHORT).show();
		} else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			Toast.makeText(this, "设为竖屏", Toast.LENGTH_SHORT).show();
		}
	}
	
	// 关于
	public void aboutDrawView() {
		new AlertDialog.Builder(this)
			.setTitle("关于绘图板")
			.setIcon(R.drawable.ic_launcher)
			.setMessage("作者：fzzjj2008" + '\n' + "版本：1.2")
			.setPositiveButton("确定", null)
			.create().show();
	}

}
