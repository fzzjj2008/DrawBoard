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
	// ��¼��ǰ��ͼ״̬��������ɫѡ��Ի���ʹ�ϸѡ��Ի���
	// Ĭ��Ϊ��ɫ���еȴ�ϸ
	private int pColorIndex = 0;
	private int pSizeIndex = 2;
	// ���ʴ�ϸ��Դ
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

	// �ı���ɫ
	public void setColor() {
		final DrawView board = (DrawView) findViewById (R.id.draw_board);
		// �ı���ɫ�ĶԻ���
		new AlertDialog.Builder(this)
			.setTitle("������ɫ")
			.setIcon(R.drawable.paint)
			.setSingleChoiceItems(R.array.color_name_arr, pColorIndex, new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					int color = getResources().getIntArray(R.array.color_id_arr)[which];
					board.setColor(color);
					pColorIndex = which;
				}
			})
			.setPositiveButton("ȷ��", null)
			.create().show();
	}
	
	// �ı仭�ʴ�ϸ
	public void setSize() {
		final DrawView board = (DrawView) findViewById (R.id.draw_board);
		// �ı仭�ʴ�ϸ�ĶԻ���
		new AlertDialog.Builder(this)
			.setTitle("���ʴ�ϸ")
			.setIcon(R.drawable.paint)
			.setSingleChoiceItems(R.array.size_name_arr, pSizeIndex, new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					float size = pSizeIds[which];
					board.setSize(size);
					pSizeIndex = which;
				}
			})
			.setPositiveButton("ȷ��", null)
			.create().show();
	}
	
	// ����
	public void clearScreen() {
		new AlertDialog.Builder(this)
			.setTitle("����")
			.setIcon(R.drawable.clear)
			.setMessage("�Ƿ�Ҫ��������?")
			.setPositiveButton("ȷ��", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					DrawView board = (DrawView) findViewById (R.id.draw_board);
					board.clearScreen();
				}
			})
			.setNegativeButton("ȡ��", null)
			.create().show();
	}
	
	// ����ͼƬ
	public void savePicture() {
		DrawView board = (DrawView) findViewById (R.id.draw_board);
		boolean picSaved = board.saveBitmap();
		if (picSaved == true) {
			Toast.makeText(this, "ͼƬ�ѱ�����/mnt/sdcard/Pictures/��Ŀ¼��",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "�޷���ͼƬд��SD��", Toast.LENGTH_SHORT).show();			
		}
	}
	
	// ��ת��Ļ
	public void changeOrientation() {
		Configuration config = getResources().getConfiguration();
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			Toast.makeText(this, "��Ϊ����", Toast.LENGTH_SHORT).show();
		} else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			Toast.makeText(this, "��Ϊ����", Toast.LENGTH_SHORT).show();
		}
	}
	
	// ����
	public void aboutDrawView() {
		new AlertDialog.Builder(this)
			.setTitle("���ڻ�ͼ��")
			.setIcon(R.drawable.ic_launcher)
			.setMessage("���ߣ�fzzjj2008" + '\n' + "�汾��1.2")
			.setPositiveButton("ȷ��", null)
			.create().show();
	}

}
