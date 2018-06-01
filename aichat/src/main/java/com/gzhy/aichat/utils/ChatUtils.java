package com.gzhy.aichat.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatUtils {

	public static void showThankDialog(final Activity act,Handler handler, final boolean isFinish) {

		ToastUtil.showToast(act.getApplicationContext(),act.getResources().getString(
				ResourceUtils.getIdByName(act, "string", "sobot_thank_dialog_hint")));
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(!act.isFinishing()){
					if(isFinish){
						act.finish();
					}
				}
			}
		},2000);
	}

	/**
	 * activity打开选择图片界面
	 * @param act
	 */
	public static void openSelectPic(Activity act) {
		openSelectPic(act,null);
	}

	/**
	 * Fragment打开选择图片界面
	 * @param act
	 */
	public static void openSelectPic(Activity act,Fragment childFragment) {
		if(act == null){
			return;
		}
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
		} else {
			intent = new Intent(Intent.ACTION_PICK,
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		try {
			if (childFragment != null) {
				childFragment.startActivityForResult(intent, HyAiConstant.REQUEST_CODE_picture);
			} else {
				act.startActivityForResult(intent, HyAiConstant.REQUEST_CODE_picture);
			}
		}catch (Exception e){
			ToastUtil.showToast(act.getApplicationContext(),"无法打开相册，请检查相册是否开启");
		}
	}

	/**
	 * activity打开相机
	 * @param act
	 * @return
	 */
	public static File openCamera(Activity act) {
		return openCamera(act, null);
	}

	/**
	 * Fragment打开相机
	 * @param act
	 * @param childFragment 打开相机的fragment
	 * @return
     */
	public static File openCamera(Activity act, Fragment childFragment) {
		String path = CommonUtil.getSDCardRootPath() + "/" +
				CommonUtil.getApplicationName(act.getApplicationContext()) + "/" + System.currentTimeMillis() + ".jpg";
		// 创建图片文件存放的位置
		File cameraFile = new File(path);
		boolean mkdirs = cameraFile.getParentFile().mkdirs();
		LogUtils.i("cameraPath:" + path);
		Uri uri;
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
			ContentValues contentValues = new ContentValues(1);
			contentValues.put(MediaStore.Images.Media.DATA, cameraFile.getAbsolutePath());
			uri = act.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					contentValues);
		} else {
			uri = Uri.fromFile(cameraFile);
		}
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore
				.EXTRA_OUTPUT, uri);
		if (childFragment != null) {
			childFragment.startActivityForResult(intent, HyAiConstant.REQUEST_CODE_makePictureFromCamera);
		} else {
			act.startActivityForResult(intent, HyAiConstant.REQUEST_CODE_makePictureFromCamera);
		}
		return cameraFile;
	}

	public static int getResId(Context context,String name) {
		return ResourceUtils.getIdByName(context, "id", name);
	}

	public static int getResDrawableId(Context context,String name) {
		return ResourceUtils.getIdByName(context, "drawable", name);
	}

	public static int getResLayoutId(Context context,String name) {
		return ResourceUtils.getIdByName(context, "layout", name);
	}

	public static int getResStringId(Context context,String name) {
		return ResourceUtils.getIdByName(context, "string", name);
	}

	public static String getResString(Context context,String name){
		return context.getResources().getString(ChatUtils.getResStringId(context,name));
	}


    /**
     * 判断机器人引导转人工是否勾选
     * @param manualType 机器人引导转人工 勾选为1，默认为0 固定位置，比如1,1,1,1=直接回答勾选，理解回答勾选，引导回答勾选，未知回答勾选
     * @param answerType
     * @return true表示勾选上了
     */
	public static boolean checkManualType(String manualType,String answerType){
        if(TextUtils.isEmpty(manualType) || TextUtils.isEmpty(answerType)){
            return false;
        }
        try {
            Integer type = Integer.valueOf(answerType);
            String[] mulArr = manualType.split(",");
            if((type == 1 && "1".equals(mulArr[0])) || (type == 2 && "1".equals(mulArr[1]))
                    || (type == 4 && "1".equals(mulArr[2])) || (type == 3 && "1".equals(mulArr[3]))) {
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

	public static void sendPicByFilePath(Context context,String filePath,SobotSendFileListener listener) {

		Bitmap bitmap = SobotBitmapUtil.compress(filePath,context);
		if(bitmap!=null){
			int degree = ImageUtil.readPictureDegree(filePath);
			bitmap = ImageUtil.rotateBitmap(bitmap, degree);
			if (!(filePath.endsWith(".gif") || filePath.endsWith(".GIF"))) {
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(filePath);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			listener.onSuccess(filePath);
		}else{
			ToastUtil.showToast(context,"图片格式错误");
			listener.onError();
		}
	}

	public static void sendPicByUriPost(Context context,Uri selectedImage,SobotSendFileListener listener){
		String picturePath = ImageUtil.getPath(context, selectedImage);
		if (!TextUtils.isEmpty(picturePath)) {
			sendPicByFilePath(context,picturePath, listener);
		} else {
			File file = new File(selectedImage.getPath());
			if (!file.exists()) {
				ToastUtil.showToast(context,"找不到图片");
				return;
			}
			sendPicByFilePath(context,picturePath, listener);
		}
	}

	public interface SobotSendFileListener{
		void onSuccess(String filePath);
		void onError();
	}

}