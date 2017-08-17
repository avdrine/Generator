package generator;

import java.util.ArrayList;

/**
 * Класс страницы, состоящей из таблицы в виде заголовка и полей класса Field
 * @author avdrine
 */
public class Page {
    /** Максимальная высота страницы */
    private final int mHeight;
    /** Ширина страницы*/
    private final int mWidth;
    /** Поля данных */
    private ArrayList<Field> mFields;
    /** Заголовок */
    private Field mHeader;
    
    /** 
     * Конструктор
     * @param height Максимальная высота страница
     * @param width Максимальная ширина страницы
     */
    Page(int height, int width)
    {
        mHeight = height;
        mWidth = width;
        mFields = new ArrayList<Field>();
    }
    
    /** 
     * Добавить поле в страницу
     * @param field Добавляемое поле
     * @throws java.lang.Exception Возвращает исключение, если не может добавить
     */
    public void addField(Field field) throws Exception
    {
        int currentHeight = calculateCurrentHeight();
        if(currentHeight + 1 + field.getHeight() < mHeight)
            mFields.add(field);
        else
            throw new Exception("No space");
    }
    
    /** 
     * Добавление поля с заголовками на страницу
     * @param field Поле с заголовками
     */
    public void addHeader(Field field)
    {
        mHeader = field;
    }
    
    /** 
     * Рассчитывание текущей высоты страницы
     * @return Высота
     */
    private int calculateCurrentHeight()
    {
        int currentHeight = 0;
        currentHeight += mHeader.getHeight();
        
        for(int i = 0; i < mFields.size(); i++)
        {
            currentHeight += mFields.get(i).getHeight() + 1;
        }
        return currentHeight;
    }
    
    /** 
     * Создание строчки-разделителя
     * @return Строчка-разделитель в виде строки
     */
    private String createSeparator()
    {
        String result = new String();
        for(int i = 0; i < mWidth; i++)
            result += "-";
        result += TransferSign.write();
        return result;
    }
    
    @Override
    /** 
     * Формирование страницы в текстовом виде
     * @return Страница в текстовом виде
     */
    public String toString()
    {
        String result = new String();
        result += mHeader.toString();
        for(int i = 0; i < mFields.size(); i++)
        {
            result += createSeparator();
            result += mFields.get(i).toString();
        }
        
        return result;
    }
}
