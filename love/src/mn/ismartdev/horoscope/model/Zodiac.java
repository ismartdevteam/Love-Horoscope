package mn.ismartdev.horoscope.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Zodiac {
	@DatabaseField
	public int id;
	@DatabaseField
	public int zodiac_id;
	@DatabaseField
	public int zodiac_name;
	@DatabaseField
	public String main;
	@DatabaseField
	public String balance;
	@DatabaseField
	public String mainFlove;
	@DatabaseField
	public String mainMlove;
	@DatabaseField
	public String style;
	@DatabaseField
	public String gift;
	@DatabaseField
	public String mGetLove;
	@DatabaseField
	public String sexM;
	@DatabaseField
	public String sexF;
	@DatabaseField
	public String fBalance;
	@DatabaseField
	public String mX;

}
