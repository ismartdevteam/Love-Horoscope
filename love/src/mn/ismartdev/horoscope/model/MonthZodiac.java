package mn.ismartdev.horoscope.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class MonthZodiac {
@DatabaseField
public int id;
@DatabaseField
public String text;
}
