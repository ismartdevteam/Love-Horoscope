package mn.ismartdev.horoscope.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static String databaseName = "horoscope.sqlite";
	private static int databaseVersion = 1;
	Dao<Zodiac, Integer> zodDao = null;
	private Context _context;

	public DatabaseHelper(Context context) {
		super(context, databaseName, null, databaseVersion);
		// TODO Auto-generated constructor stub
		_context = context;
		if (!checkDataBase()) {

			try {
				this.getReadableDatabase();
				this.close();
				copyDataBase();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	private boolean checkDataBase() {
		File dbFile = new File(_context.getApplicationInfo().dataDir
				+ "/databases/" + "horoscope.sqlite");
		Log.i("isloca", dbFile.exists() + "");
		return dbFile.exists();
	}

	private void copyDataBase() throws IOException {
		InputStream mInput = _context.getApplicationContext().getAssets()
				.open("horoscope.sqlite");
		String outFileName = _context.getApplicationInfo().dataDir
				+ "/databases/" + "horoscope.sqlite";
		OutputStream mOutput = new FileOutputStream(outFileName);
		byte[] mBuffer = new byte[1024];
		int mLength;
		while ((mLength = mInput.read(mBuffer)) > 0) {
			mOutput.write(mBuffer, 0, mLength);
		}
		mOutput.flush();
		mOutput.close();
		mInput.close();
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		// TODO Auto-generated method stub
		try {
			TableUtils.createTableIfNotExists(connectionSource, Zodiac.class);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

		try {
			TableUtils.createTable(connectionSource, Zodiac.class);
			onCreate(arg0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Dao<Zodiac, Integer> getZodiacDao() throws SQLException {
		if (zodDao == null)

			zodDao = getDao(Zodiac.class);

		return zodDao;
	}

}
