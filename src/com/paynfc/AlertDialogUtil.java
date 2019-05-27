package com.paynfc;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogUtil {
	
	public static final String NO_INSERT_DATA = "Please insert your name, e-mail and tel. ";
	public static final String INSERT_LESS_FOUR_CHAR ="Please insert more than 4 characters.";
	public static final String NO_EQUALS_PASSWARD = "Your password and confirmation password do not match." ;
	public static final String NO_COMPLETE_DOG_DATA = "Please insert complete data. ";
	private static final String OK_BTN = "OK";
	
	
	public static <E> void showAlertDialog(Context ctx ,String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(message);
		builder.setNegativeButton(OK_BTN,
				new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(ctx, "Ok is clicked", Toast.LENGTH_LONG).show();

            }
        });
        builder.show();
		
	}
}