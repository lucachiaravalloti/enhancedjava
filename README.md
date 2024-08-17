# enhancedjava
This repository is to share my produced java library to obtain enhanced java application, particularly related to GUI interface.

Importing just 3 classes you can easily display tables with features that aren't readily available using basic Javax classes.

You can immediately have a table with:
1. Multiple column sort visualized with arrow icon and number of the column in the ordering sequence, obtained holding SHIFT key and clicking on the header;
2. Column fitted to its content when you click double-click on the header separation line;
3. One column showing progressive numbers to count the rows in the table;
4. All columns fitted to their content with one double-click on the prog column (see point 3.) if present;
5. Every column content can be aligned very easily to LEFT/CENTER/RIGHT.
6. Many other features will be added soon.

Here is a video on youtube about the above features:

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/lX15lL8aTiw/0.jpg)](https://www.youtube.com/watch?v=lX15lL8aTiw)

The files used in the example are here https://github.com/lucachiaravalloti/enhancedjava/tree/main/com/github/lucachiaravalloti/test/enhancedjava/gui/table1

1. First of all define the fields your table has. Fields are one Enum class:
   1. the order of the enum values will be the order of the columns;
   2. the enum class must have a columnName property that will be the header title of its columns;
   3. the enum class may have a horizontalAlignment property in case you want to set the content alignment of the columns;
   4. Note that if you want the Progessive column to be present, then you have to define the value PROG with columnName property set to null if you want the header title to be empty.
  
In the example:
```
public enum TableFields {

	PROG(null, null), NAME("Name", SwingConstants.RIGHT), LASTNAME("Lastname", SwingConstants.CENTER), CITY("City", null),
	RECORD_DATE("Record Date", SwingConstants.CENTER), STATUS("Status", null);

	public final String columnName;
	public final Integer horizontalAlignment;

	TableFields(String columnName, Integer horizontalAlignment) {

		this.columnName = columnName;
		this.horizontalAlignment = horizontalAlignment;
	}
}
```
2. Second, write a class holding the value 
