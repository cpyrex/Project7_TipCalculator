package com.murach.tipcalculator;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tip {


	ArrayList<Tip> tipArrayList = new ArrayList<Tip>();

    private long id;
    private long dateMillis;
    private float billAmount;
    private float tipPercent;
    
    public Tip() {
        setId(0);
        setDateMillis(System.currentTimeMillis());
        setBillAmount(0);
        setTipPercent(.15f);
    }

    public Tip(long id, long dateMillis, float billAmount, float tipPercent) {
        this.setId(id);
        this.setDateMillis(dateMillis);
        this.setBillAmount(billAmount);
        this.setTipPercent(tipPercent);
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDateMillis() {
		return dateMillis;
	}

	@SuppressLint("SimpleDateFormat")
	public String getDateStringFormatted() {
    	// set the date with formatting
    	Date date = new Date(dateMillis);
    	SimpleDateFormat sdf = new SimpleDateFormat("MMM d yyyy HH:mm:ss");
    	return sdf.format(date);
	}

	public void setDateMillis(long dateMillis) {
		this.dateMillis = dateMillis;
	}

	public float getBillAmount() {
		return billAmount;
	}

	public String getBillAmountFormatted() {
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        return currency.format(billAmount);
	}

	public void setBillAmount(float billAmount) {
		this.billAmount = billAmount;
	}

	public float getTipPercent() {
		return tipPercent;
	}
	
	public String getTipPercentFormatted() {
        NumberFormat percent = NumberFormat.getPercentInstance();
        return percent.format(tipPercent);    			
	}

	public void setTipPercent(float tipPercent) {
		this.tipPercent = tipPercent;
	}




	//this is the class that handles the database
	public class MyDBHandler extends SQLiteOpenHelper {

		//database variables
		private static final String DATABASE_NAME = "tips.db";
		private static final int DATABASE_VERSION = 1;

		static final String TABLE_TIPS = "tips";

		private static final String COLUMN_ID = "_id";
		private static final long _id = 0;
		private static final String BILL_DATE = "_billDate";
		static final String BILL_AMOUNT = "_billAmount";
		static final String TIP_PERCENT = "_tipPercent";

		// general setup for database
		public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
			super(context, DATABASE_NAME, factory, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			String query = "CREATE TABLE " + TABLE_TIPS + "(" +
					COLUMN_ID + " INTERGER PRIMARY KEY AUTOINCREMENT, " +
					BILL_DATE + " INTEGER NOT NULL " +
					BILL_AMOUNT + " FLOAT NOT NULL " +
					TIP_PERCENT + "  FLOAT NOT NULL  );";

			db.execSQL(query);

			//String insertQuery = "INSERT INTO tips VALUES ()"

			//db.execSQL(insertQuery);

		}

		public ArrayList<Tip> getTips() {
			ArrayList<Tip> tipArrayList = new ArrayList<Tip>();

			SQLiteDatabase db = getWritableDatabase();
			String query = "SELECT * FROM " + TABLE_TIPS + " WHERE 1";

			//cursor point to a location in your results
			Cursor c = db.rawQuery(query, null);
			//move to the first row in your results
			c.moveToFirst();

			while (!c.isAfterLast()) {
				Tip tips = cursorToTips(c);
				tipArrayList.add(tips);
				c.moveToNext();


			}
			db.close();
			return tipArrayList;
		}

		private Tip cursorToTips(Cursor c) {
			Tip tips = new Tip();
			tips.setId(0);
			tips.setDateMillis(1);
			tips.setBillAmount(2);
			tips.setTipPercent(3);


			return tips;
		}


		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			db.execSQL("DROP IF TABLE EXIST " + TABLE_TIPS);
			onCreate(db);
		}

		//Add two row to the database initially
		public void addInitialTips(Tip tips) {
			//get product info
			ContentValues values = new ContentValues();
			values.put(COLUMN_ID, 0);
			values.put(BILL_DATE, 0);
			values.put(BILL_AMOUNT, 45.28);
			values.put(TIP_PERCENT, .15);

			values.put(COLUMN_ID, 1);
			values.put(BILL_DATE, 0);
			values.put(BILL_AMOUNT, 24.28);
			values.put(TIP_PERCENT, .15);

			SQLiteDatabase db = getWritableDatabase();
			db.insert(TABLE_TIPS, null, values);
			//always close database to save memory consumption
			db.close();
		}

		public void addTip() {
			//get product info
			Tip tips = new Tip();
			ContentValues values = new ContentValues();

			values.put(BILL_AMOUNT, tips.getBillAmount());
			values.put(TIP_PERCENT, tips.getTipPercent());


			SQLiteDatabase db = getWritableDatabase();
			db.insert(TABLE_TIPS, null, values);
			//always close database to save memory consumption
			db.close();
		}

	}
}