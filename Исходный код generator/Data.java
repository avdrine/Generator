package generator;

import java.util.GregorianCalendar;


/**
 * Класс контейнера данных, хранящий в себе порядковый номер, дату и имя человека
 * @author avdrine
 */
public final class Data {
    
    /** Порядковый номер */
    private int mNumber;
    /** Дата */
    private GregorianCalendar mDate;
    /** Полное имя человека */
    private String mFullName;
    /** Флаг, показывающий указан ли год в дате */
    private boolean mHasYear;
    
    /** 
     * Стандартный конструктор
     */
    Data()
    {
        mNumber = 0;
        mDate = new GregorianCalendar();
        mFullName = new String();
        mHasYear = true;
    }
    
    /** 
     * Инициализирующий конструктор с date в формате строки
     * @param number Порядковый номер
     * @param date Дата
     * @param fullName Полное имя
     */
    Data(int number, String date, String fullName) throws Exception
    {
        setNumber(number);
        setDate(date);
        setFullName(fullName);
    }
    
    /** 
     * Инициализирующий конструктор с date в формате GregorianCalendar
     * @param number Порядковый номер
     * @param date Дата
     * @param fullName Полное имя
     */
    Data(int number, GregorianCalendar date, String fullName)
    {
        setNumber(number);
        setDate(date);
        setFullName(fullName);
    }
    
    /** 
     * Изменение порядкового номера
     * @param number Порядковый номер
     */
    public void setNumber(int number)
    {
        mNumber = number;
    }
    
    /** 
     * Изменение даты
     * @param text Дата
     * @throws java.lang.Exception Некорректная дата
     */
    public void setDate(String text) throws Exception
    {
        mDate = stringToDate(text);
    }
    
    /** 
     * Измнение даты
     * @param date Дата
     */
    public void setDate(GregorianCalendar date)
    {
        mDate = date;
        mHasYear = true;
    }
    
    /** 
     * Изменение полного имени
     * @param fullName Полное имя
     */
    public void setFullName(String fullName)
    {
        mFullName = fullName;
    }
    
    /** 
     * Получение значения порядкового номера
     * @return Порядковый номер
     */
    public int getNumber()
    {
       return mNumber; 
    }
    
    /** 
     * Получение значения даты
     * @return Дата
     */
    public String getDate()
    {
        String Date;
        
        int day = mDate.get(GregorianCalendar.DATE);
        int month = mDate.get(GregorianCalendar.MONTH);
        
        if(mHasYear)
        {
            int year = mDate.get(GregorianCalendar.YEAR);
            Date = day + "/" + month + "/" + year;
        }
        else
        {
            Date = day + "/" + month;
        }
        return Date;
    }
    
    /** 
     * Получение значения полного имени
     * @return Полное имя
     */
    public String getFullName()
    {
        return mFullName;
    }
    
    /** 
     * Получение параметра по названию
     * @param parametr Название параметра
     * Number - Порядковый номер
     * Date - Дата
     * FullName - Полное имя
     * @return Параметр в текстовом виде
     * @throws java.lang.Exception Не существует поля с указанным названием
     */
    public String get(String parametr) throws Exception
    {
        switch(parametr)
        {
            case "Date":
                return getDate();
            case "Number":
                return String.valueOf(getNumber());
            case "FullName":
                return getFullName();
            default:
                throw new Exception("No field with such name");
        }
    }
    
    /** 
     * Получение параметра по номеру параметра
     * @param parametr Номер параметра
     * 0 - Порядковый номер
     * 1 - Дата
     * 2 - Полное имя
     * @return Параметр в текстовом виде
     * @throws java.lang.Exception Не существует поля с указанным номером
     */
    public String get(int parametr) throws Exception
    {
        switch(parametr)
        {
            case 0:
                return String.valueOf(getNumber());
            case 1:
                return getDate();
            case 2:
                return getFullName();
            default:
                throw new Exception("No field with such index");
        }
    }
    
    
    /** 
     * Конвертация даты
     * @param text Дата в текстовом формате
     * @return Дата в формате GregorianCalendar
     * @throws java.lang.Exception Некорректная дата
     */
    private GregorianCalendar stringToDate(String text) throws Exception
    {
        String[] numbers = text.split("/");
        
        int day = 0;
        int month = 0;
        int year = 0;
        switch(numbers.length)
        {
            case 3: 
                year = Integer.parseInt(numbers[2]);
            case 2: 
                month = Integer.parseInt(numbers[1]);
                day = Integer.parseInt(numbers[0]);
                break;
            default: 
                throw new Exception("Bad date in file");
        }
        if(!checkDate(day, month))
            throw new Exception("Bad date in file");
        
        mHasYear = numbers.length == 3;
        GregorianCalendar date;
        if(mHasYear)
            date = new GregorianCalendar(year, month, day);
        else
            date = new GregorianCalendar(1970, month, day);
        return date;
    }
    
    /** 
     * Проверка корректности даты
     * @param day День
     * @param month Месяц
     * @return Корректна дата или нет
     */
    private boolean checkDate(int day, int month)
    {
        if(day > 31 || day < 1)
            return false;
        if(month > 12 || month < 1)
            return false;
        return true;
    }
}
